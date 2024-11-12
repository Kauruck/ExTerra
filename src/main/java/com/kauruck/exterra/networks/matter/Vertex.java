package com.kauruck.exterra.networks.matter;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.exceptions.UnexpectedBehaviorException;
import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.api.networks.matter.INetworkMember;
import com.kauruck.exterra.networking.ExTerraCodecs;
import com.kauruck.exterra.util.NBTUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Vertex {

    public static final Codec<Vertex> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("id").forGetter(Vertex::getId),
                    Codec.list(Codec.INT).fieldOf("loaded_ids").forGetter(Vertex::getLoaded_ids),
                    BlockPos.CODEC.fieldOf("position").forGetter(Vertex::getPosition)
            ).apply(instance, Vertex::new));

    private List<Edge> edges = new ArrayList<>();
    private final BlockPos position;
    private  Level level;

    /**
     * Unique id in the network
     */
    private final int id;
    private INetworkMember member;
    private Integer[] loaded_ids;

    public Vertex(BlockPos pos, Level level, int id){
        this.id = id;
        this.position = pos;
        this.level = level;
        this.loaded_ids = new Integer[0];
    }

    public Vertex(int id, List<Integer> loaded_ids, BlockPos position) {
        this.id = id;
        this.loaded_ids = loaded_ids.toArray(Integer[]::new);
        this.position = position;
    }

    public List<Integer> getLoaded_ids() {
        return List.of(loaded_ids);
    }

    public void loadFromWorld() throws UnexpectedBehaviorException {
        BlockEntity entity = level.getBlockEntity(position);
        if(!(entity instanceof INetworkMember))
            throw new UnexpectedBehaviorException("[MatterVertex] Tried to add a non INetworkMember to the Network");
        this.member = (INetworkMember) entity;
    }

    /**
     * Links all the loaded ids to their corresponding edges in the network.
     * This must be called after loading from tag
     * @param network The network to link against
     */
    public void link(MatterNetwork network, Level level) throws UnexpectedBehaviorException {
        this.level = level;
        this.loadFromWorld();
        for(int currentId : this.loaded_ids){
            edges.add(network.edges.stream()
                    .filter(edge -> edge.getId() == currentId)
                    .findAny()
                    .orElse(null));
        }
    }

    public int getId() {
        return id;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public BlockPos getPosition() {
        return position;
    }

    public Level getLevel() {
        return level;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public void addEdge(Edge edge){
        this.edges.add(edge);
    }

    public void removeEdge(Edge edge){
        this.edges.remove(edge);
    }

    public INetworkMember getMember(){
        return this.member;
    }

    public int getTimesTransported(Matter matter){
        return (int) edges.stream()
                .filter(e -> ArrayUtils.contains(e.getTransportedMatter(), matter))
                .count();
    }

    public MatterStack pullMatterStack(Matter matter, Direction side) {
        if(member.pullsMatter(matter)) {
            return member.pullMatter(matter, side);
        } else {
            return null;
        }
    }



    public void applyBackpressure(List<MatterStack> remainderList) {
        this.member.applyBackpressure(remainderList.toArray(MatterStack[]::new));
    }


}
