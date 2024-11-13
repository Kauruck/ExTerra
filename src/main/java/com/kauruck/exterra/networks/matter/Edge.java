package com.kauruck.exterra.networks.matter;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.api.recipes.ExTerraRecipeManager;
import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.recipes.ConversionContainer;
import com.kauruck.exterra.recipes.ConversionHelper;
import com.kauruck.exterra.recipes.ConversionRecipe;
import com.kauruck.exterra.util.OptionalEither;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Edge {

    public static final int CACHE_SIZE = 20;

    public static final Codec<Edge> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("a").forGetter(e -> e.id_a),
                    Codec.INT.fieldOf("b").forGetter(e -> e.id_b),
                    Codec.INT.fieldOf("id").forGetter(Edge::getId),
                    Wire.CODEC.fieldOf("wire").forGetter(Edge::getWire)
            ).apply(instance, Edge::new));

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

    private MatterNetwork network;

    /**
     * last used recipe.
     */
    private ConversionRecipe cachedRecipe = null;
    private Map<Matter[], OptionalEither<Matter, ConversionRecipe>> cachedTransport = new LinkedHashMap<>(CACHE_SIZE+1, .75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Matter[], OptionalEither<Matter, ConversionRecipe>> eldest) {
            return size() > CACHE_SIZE;
        }
    };

    @SuppressWarnings("unchecked")
    public Edge(Vertex a, Vertex b, int id, Wire wire, MatterNetwork network) {
        this.id = id;
        this.a = a;
        this.b = b;
        this.id_a = a.getId();
        this.id_b = b.getId();
        this.network = network;

        this.transportedMatterFromA = Arrays.stream(ConversionHelper
                        .convertWithConversion(a.getMember().pulledMatter(wire.getDirectionA()), network.getShapes()))
                .filter(matter ->  b.getMember().acceptsMatter(matter.getA(), wire.getDirectionB()))
                .toArray(Tuple[]::new);
        this.transportedMatterFromB = Arrays.stream(ConversionHelper
                        .convertWithConversion(b.getMember().pulledMatter(wire.getDirectionB()), network.getShapes()))
                .filter(matter ->  a.getMember().acceptsMatter(matter.getA(), wire.getDirectionA()))
                .toArray(Tuple[]::new);

        this.wire = wire;
    }


    private Edge(int a_id, int b_id, int id, Wire wire) {
        this.id = id;
        this.id_a = a_id;
        this.id_b = b_id;
        this.wire = wire;
    }

    /**
     * Links all the loaded ids to their corresponding edges in the network.
     * This must be called after loading from tag
     * @param network The network to link against
     */
    @SuppressWarnings("unchecked")
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
                        .convertWithConversion(a.getMember().pulledMatter(wire.getDirectionA()), network.getShapes()))
                .filter(matter ->  b.getMember().acceptsMatter(matter.getA(), wire.getDirectionB()))
                .toArray(Tuple[]::new);
        this.transportedMatterFromB = Arrays.stream(ConversionHelper
                        .convertWithConversion(b.getMember().pulledMatter(wire.getDirectionB()), network.getShapes()))
                .filter(matter ->  a.getMember().acceptsMatter(matter.getA(), wire.getDirectionA()))
                .toArray(Tuple[]::new);

        this.network = network;
    }

    public int getId() {
        return id;
    }

    public Wire getWire() {
        return wire;
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

        doTransfer(a, transportedMatterFromA, b, wire.getDirectionA(), wire.getDirectionB());
        doTransfer(b, transportedMatterFromB, a, wire.getDirectionB(), wire.getDirectionA());

    }

    private void doTransfer(Vertex from, Tuple<Matter, Matter[]>[] maybeTransport, Vertex to, Direction fromDirection, Direction toDirection) {
        ExTerraRecipeManager<MatterStack> conversion = ExTerraCore.CONVERSION_RECIPE_MANGER.get();

        List<MatterStack> remainderList = new ArrayList<>();
        boolean hadFlagMiss = false;
        boolean transportedMatter = false;
        //Move from a
        for(Tuple<Matter, Matter[]> currentMatterTransfer : maybeTransport) {
            boolean flagCacheHit = false;
            if (cachedTransport.containsKey(currentMatterTransfer.getB())) {
                OptionalEither<Matter, ConversionRecipe> cacheEntry = cachedTransport.get(currentMatterTransfer.getB());
                if (cacheEntry.isPresent()) {
                    if (cacheEntry.isRightPresent()) {
                        Matter toPull = cacheEntry.getRight();
                        MatterStack stack = from.pullMatterStack(toPull, fromDirection);
                        if (stack != null) {
                            wire.addInfo(stack.getMatter().getParticleColor());

                            MatterStack remainder = to.getMember().pushMatter(stack, toDirection);
                            if (remainder != null && remainder.getAmount() != 0) {
                                remainderList.add(remainder);
                            }
                            flagCacheHit = true;
                        }
                    } else { // Left must be present
                        Set<MatterStack> presentStacks = Arrays.stream(currentMatterTransfer.getB())
                                .map(m -> from.pullMatterStack(m, fromDirection))
                                .filter(Objects::nonNull)
                                .filter(m -> m.getAmount() > 0)
                                .collect(Collectors.toSet());
                        ConversionContainer container = new ConversionContainer(presentStacks, new HashSet<>(network.getShapes()));
                        ConversionRecipe recipe = cacheEntry.getLeft();
                        if (recipe.matches(container, network.getLevel())) {
                            MatterStack output = recipe.assembleAsMuchAsPossible(container);

                            for (MatterStack input : presentStacks) {
                                wire.addInfo(input.getMatter().getParticleColor(), 0.5f, from != b);
                            }
                            wire.addInfo(output.getMatter().getParticleColor(), 0.5f, from == b);
                            MatterStack remainder = to.getMember().pushMatter(output, toDirection);
                            if (remainder != null && remainder.getAmount() != 0) {
                                remainderList.add(remainder);
                            }

                            remainderList.addAll(container.getAll());
                            flagCacheHit = true;
                        }
                    }
                }
            }
            if (!flagCacheHit) {
                hadFlagMiss = true;
                List<MatterStack> presentStacks = new ArrayList<>();
                boolean flagJump = false;
                for (Matter currentMatter : currentMatterTransfer.getB()) {
                    MatterStack stack = from.pullMatterStack(currentMatter, fromDirection);
                    if (stack == null) {
                        flagJump = true;
                        break;
                    }

                    presentStacks.add(stack);
                }

                if (flagJump || presentStacks.isEmpty()) {
                    continue;
                }

                boolean flagFoundRecipe = false;
                ConversionContainer container = new ConversionContainer(new HashSet<>(presentStacks), new HashSet<>(network.getShapes()));
                Optional<ConversionRecipe> recipeOptional = conversion.getRecipeFor(ExTerraCore.CONVERSION_RECIPE_TYPE.get(), container, network.getLevel());
                if (recipeOptional.isPresent()) {
                    ConversionRecipe recipe = recipeOptional.get();
                    if (recipe.matches(container, network.getLevel())) {
                        MatterStack output = recipe.assembleAsMuchAsPossible(container);
                        for (MatterStack input : presentStacks) {
                            wire.addInfo(input.getMatter().getParticleColor(), 0.5f, from != b);
                        }
                        wire.addInfo(output.getMatter().getParticleColor(), 0.5f, from == b);
                        MatterStack remainder = to.getMember().pushMatter(output, toDirection);
                        if (remainder != null && remainder.getAmount() != 0) {
                            remainderList.add(remainder);
                        }

                        remainderList.addAll(container.getAll());
                        flagFoundRecipe = true;
                        transportedMatter = true;
                        cachedTransport.put(currentMatterTransfer.getB(), OptionalEither.left(recipe));
                    }

                }


                // Transport without conversion
                if (!flagFoundRecipe && presentStacks.size() == 1 && presentStacks.get(0).getMatter() == currentMatterTransfer.getA()) {
                    MatterStack stack = presentStacks.get(0);
                    wire.addInfo(stack.getMatter().getParticleColor());

                    MatterStack remainder = to.getMember().pushMatter(stack, toDirection);
                    transportedMatter = true;
                    cachedTransport.put(currentMatterTransfer.getB(), OptionalEither.right(stack.getMatter()));
                    if (remainder != null && remainder.getAmount() != 0) {
                        remainderList.add(remainder);
                    }
                }

            }
        }
        if (hadFlagMiss && transportedMatter) {
            ExTerra.LOGGER.info("Cache Miss");
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
