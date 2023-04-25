package com.kauruck.exterra.networks.matter;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.exceptions.UnexpectedBehaviorException;
import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.util.NBTUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Edge {

    private Vertex a;
    private Vertex b;

    private int id_a;
    private int id_b;

    /**
     * The id of the edge. This is unique per Network and persistent between save.
     */
    private final int id;

    private Matter[] transportedMatterFromA;
    private Matter[] transportedMatterFromB;

    private final Wire wire;

    public Edge(Vertex a, Vertex b, int id, Wire wire) {
        this.id = id;
        this.a = a;
        this.b = b;
        this.id_a = a.getId();
        this.id_b = b.getId();
        //TODO conversion
        this.transportedMatterFromA = Arrays.stream(a.getMember().pulledMatter())
                .filter(matter ->  b.getMember().acceptsMatter(matter))
                .toArray(Matter[]::new);
        this.transportedMatterFromB = Arrays.stream(b.getMember().pulledMatter())
                .filter(matter ->  a.getMember().acceptsMatter(matter))
                .toArray(Matter[]::new);

        this.wire = wire;
    }

    private Edge(int a_id, int b_id, int id, Wire wire) {
        this.id = id;
        this.id_a = a_id;
        this.id_b = b_id;
        this.wire = wire;
    }

    public static Edge fromTag(CompoundTag tag) {
        int a_id = tag.getInt("a");
        int b_id = tag.getInt("b");
        int id = tag.getInt("id");
        Wire wire = Wire.fromNBT(tag.getCompound("wire"));
        return new Edge(a_id, b_id, id, wire);
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

        //TODO conversion
        this.transportedMatterFromA = Arrays.stream(a.getMember().pulledMatter())
                .filter(matter ->  b.getMember().acceptsMatter(matter))
                .toArray(Matter[]::new);

        this.transportedMatterFromB = Arrays.stream(b.getMember().pulledMatter())
                .filter(matter ->  a.getMember().acceptsMatter(matter))
                .toArray(Matter[]::new);
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
        return ArrayUtils.addAll(transportedMatterFromA, transportedMatterFromB);
    }

    public void serverTick(){
        wire.serverTick();

        //TODO Conversion

        List<MatterStack> remainderList = new ArrayList<>();
        //Move from a
        for(Matter currentMatter : transportedMatterFromA){
            MatterStack stack = a.pullMatterStack(currentMatter);
            if(stack == null)
                continue;
            wire.addInfo(stack.getMatter().getParticleColor());
            //ExTerra.LOGGER.debug("Moving Matter {} from {} to {}", stack, this.a.getMember().getName(), this.b.getMember().getName());
            MatterStack remainder = b.getMember().pushMatter(stack);
            if(remainder != null && remainder.getAmount() != 0){
                remainderList.add(remainder);
            }
        }
        a.applyBackpressure(remainderList);
        remainderList.clear();


        //Move from b
        for(Matter currentMatter : transportedMatterFromB){
            MatterStack stack = b.pullMatterStack(currentMatter);
            if(stack == null)
                continue;
            //ExTerra.LOGGER.debug("Moving Matter {} form {} to {}", stack, this.b.getMember().getName(), this.a.getMember().getName());
            wire.addInfo(stack.getMatter().getParticleColor());
            MatterStack remainder = a.getMember().pushMatter(stack);
            if(remainder != null && remainder.getAmount() != 0){
                remainderList.add(remainder);
            }
        }
        b.applyBackpressure(remainderList);

    }

    public void animationTick(ClientLevel level, RandomSource random) {
        wire.animationTick(level, random);
    }
}
