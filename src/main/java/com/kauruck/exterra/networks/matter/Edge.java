package com.kauruck.exterra.networks.matter;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.exceptions.UnexpectedBehaviorException;
import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.api.networks.matter.INetworkMember;
import com.kauruck.exterra.api.recipes.ExTerraRecipeManager;
import com.kauruck.exterra.geometry.Shape;
import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.modules.ExTerraReloadableResources;
import com.kauruck.exterra.recipes.ConversionContainer;
import com.kauruck.exterra.recipes.ConversionHelper;
import com.kauruck.exterra.recipes.ConversionRecipe;
import com.kauruck.exterra.util.NBTUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

public class Edge {

    private Vertex a;
    private Vertex b;

    private int id_a;
    private int id_b;

    /**
     * The id of the edge. This is unique per Network and persistent between save.
     */
    private final int id;

    private Tuple<Matter, Matter[]>[] transportedMatterFromA;
    private Tuple<Matter, Matter[]>[] transportedMatterFromB;

    private final Wire wire;

    private final MatterNetwork network;

    /**
     * last used recipe.
     */
    private ConversionRecipe cachedRecipe = null;

    public Edge(Vertex a, Vertex b, int id, Wire wire, MatterNetwork network) {
        this.id = id;
        this.a = a;
        this.b = b;
        this.id_a = a.getId();
        this.id_b = b.getId();
        this.network = network;

        this.transportedMatterFromA = Arrays.stream(ConversionHelper
                        .convertWithConversion(a.getMember().pulledMatter(), network.getShapes()))
                .filter(matter ->  b.getMember().acceptsMatter(matter.getA()))
                .toArray(Tuple[]::new);
        this.transportedMatterFromB = Arrays.stream(ConversionHelper
                        .convertWithConversion(b.getMember().pulledMatter(), network.getShapes()))
                .filter(matter ->  a.getMember().acceptsMatter(matter.getA()))
                .toArray(Tuple[]::new);

        this.wire = wire;
    }


    private Edge(int a_id, int b_id, int id, Wire wire, MatterNetwork network) {
        this.id = id;
        this.id_a = a_id;
        this.id_b = b_id;
        this.wire = wire;
        this.network = network;
    }

    public static Edge fromTag(CompoundTag tag, MatterNetwork network) {
        int a_id = tag.getInt("a");
        int b_id = tag.getInt("b");
        int id = tag.getInt("id");
        Wire wire = Wire.fromNBT(tag.getCompound("wire"));
        return new Edge(a_id, b_id, id, wire, network);
    }

    public CompoundTag toTag(){
        CompoundTag tag = new CompoundTag();
        tag.putInt("a", a.getId());
        tag.putInt("b", b.getId());
        tag.putInt("id", id);
        tag.put("wire", wire.toNBT());
        return tag;
    }

    /**
     * Links all the loaded ids to their corresponding edges in the network.
     * This must be called after loading from tag
     * @param network The network to link against
     */
    public void link(MatterNetwork network){
        a = network.vertices.stream()
                .filter(v -> v.getId() == id_a)
                .findFirst()
                .orElse(null);

        b = network.vertices.stream()
                .filter(v -> v.getId() == id_b)
                .findFirst()
                .orElse(null);

        this.transportedMatterFromA = Arrays.stream(ConversionHelper
                        .convertWithConversion(a.getMember().pulledMatter(), network.getShapes()))
                .filter(matter ->  b.getMember().acceptsMatter(matter.getA()))
                .toArray(Tuple[]::new);
        this.transportedMatterFromB = Arrays.stream(ConversionHelper
                        .convertWithConversion(b.getMember().pulledMatter(), network.getShapes()))
                .filter(matter ->  a.getMember().acceptsMatter(matter.getA()))
                .toArray(Tuple[]::new);
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Edge edge){
            return (edge.a == this.a && edge.b == this.b) || (edge.a == this.b && edge.b == this.a);
        }
        return false;
    }

    public Matter[] getTransportedMatter() {
        return ArrayUtils.addAll(Arrays.stream(transportedMatterFromA).map(Tuple::getA).toArray(Matter[]::new),
                Arrays.stream(transportedMatterFromB).map(Tuple::getA).toArray(Matter[]::new));
    }

    public void serverTick(){
        wire.serverTick();

        doTransfer(a, transportedMatterFromA, b);
        doTransfer(b, transportedMatterFromB, a);

    }

    private void doTransfer(Vertex from, Tuple<Matter, Matter[]>[] maybeTransport, Vertex to) {
        ExTerraRecipeManager<MatterStack> conversion = ExTerraCore.CONVERSION_RECIPE_MANGER.get();

        List<MatterStack> remainderList = new ArrayList<>();
        //Move from a
        for(Tuple<Matter, Matter[]> currentMatterTransfer : maybeTransport) {
            List<MatterStack> presentStacks = new ArrayList<>();
            boolean flagJump = false;
            for (Matter currentMatter : currentMatterTransfer.getB()) {
                MatterStack stack = from.pullMatterStack(currentMatter);

                if(stack == null) {
                    flagJump = true;
                    break;
                }

                presentStacks.add(stack);
            }

            if (flagJump || presentStacks.isEmpty()) {
                continue;
            }


            // Transport without conversion
            if (presentStacks.size() == 1 && presentStacks.get(0).getMatter() == currentMatterTransfer.getA()) {
                MatterStack stack = presentStacks.get(0);
                wire.addInfo(stack.getMatter().getParticleColor());

                MatterStack remainder = to.getMember().pushMatter(stack);
                if(remainder != null && remainder.getAmount() != 0){
                    remainderList.add(remainder);
                }
            } else {
                ConversionContainer container = new ConversionContainer(new HashSet<>(presentStacks), new HashSet<>(network.getShapes()));
                ConversionRecipe recipe;
                if (cachedRecipe != null && cachedRecipe.matches(container, network.getLevel())) {
                    recipe = cachedRecipe;
                } else {
                    Optional<ConversionRecipe> recipeOptional = conversion.getRecipeFor(ExTerraCore.CONVERSION_RECIPE_TYPE.get(), container, network.getLevel());

                    if (recipeOptional.isEmpty()) {
                        continue;
                    }
                    recipe = recipeOptional.get();
                    cachedRecipe = recipe;
                }

                if(!recipe.matches(container, network.getLevel())) {
                    continue;
                }

                MatterStack output = recipe.assembleAsMuchAsPossible(container);

                for (MatterStack input : presentStacks) {
                    wire.addInfo(input.getMatter().getParticleColor(), 0.5f, from != b);
                }
                wire.addInfo(output.getMatter().getParticleColor(), 0.5f, from == b);
                MatterStack remainder = to.getMember().pushMatter(output);
                if(remainder != null && remainder.getAmount() != 0){
                    remainderList.add(remainder);
                }

                remainderList.addAll(container.getAll());
            }

        }
        from.applyBackpressure(remainderList);
    }

    public void animationTick(ClientLevel level, RandomSource random) {
        wire.animationTick(level, random);
    }

    @Override
    public String toString() {
        return "Edge(" + id + ")" +
                "{" +
                "wire=" + wire +
                '}';
    }
}
