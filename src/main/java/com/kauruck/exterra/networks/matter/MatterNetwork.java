package com.kauruck.exterra.networks.matter;

import com.kauruck.exterra.api.exceptions.UnexpectedBehaviorException;
import com.kauruck.exterra.api.networks.matter.INetworkMember;
import com.kauruck.exterra.util.NBTUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MatterNetwork {
    List<Vertex> vertices = new ArrayList<>();
    List<Edge> edges = new ArrayList<>();

    private int vertex_id = 0;
    private int edge_id = 0;
    private boolean linked = true;

    public MatterNetwork(){}
    private MatterNetwork(boolean linked){
        this.linked = linked;
    }

    private void addEdge(Vertex a, Vertex b){
        Edge edge = new Edge(a,b,edge_id);
        a.addEdge(edge);
        b.addEdge(edge);
        if(edges.contains(edge))
            return;
        edges.add(edge);
        edge_id ++;
    }

    public void reset(){
        vertices.clear();
        edges.clear();
    }

    private void addVertex(Vertex vertex) throws UnexpectedBehaviorException {
        //Check weather the block can handle being in a matter network
        if(vertex.getLevel().getBlockEntity(vertex.getPosition()) instanceof INetworkMember){
            vertex.loadFromWorld();
            vertices.add(vertex);
            //TODO Remove this later, it ist just for test purposes
            vertices.stream()
                    .filter(e -> e != vertex)
                    .forEach(e -> addEdge(vertex, e));
        }
    }

    public void addVertex(BlockPos pos, Level level) throws UnexpectedBehaviorException {
        addVertex(new Vertex(pos, level, vertex_id));
        vertex_id++;
    }

    public static MatterNetwork loadTag(CompoundTag tag){
        MatterNetwork network = new MatterNetwork(false);
        network.vertex_id = tag.getInt("vertex_id");
        network.edge_id = tag.getInt("edge_id");
        if(tag.contains("edges"))
            network.edges = List.of(NBTUtil.compoundTagToCompoundTagArray((CompoundTag) tag.get("edges"))).stream()
                .map(Edge::fromTag)
                .collect(Collectors.toList());

        if(tag.contains("vertex"))
            network.vertices = List.of(NBTUtil.compoundTagToCompoundTagArray((CompoundTag) tag.get("vertex"))).stream()
                .map(Vertex::fromTag)
                .collect(Collectors.toList());


        return network;
    }

    public boolean isLinked() {
        return linked;
    }

    public void link(){
        this.vertices.forEach(vertex -> {
            try {
                vertex.link(this);
            } catch (UnexpectedBehaviorException e) {
                throw new RuntimeException(e);
            }
        });
        this.edges.forEach(edge -> edge.link(this));
        this.linked = true;
    }

    public CompoundTag saveTag(){
        CompoundTag tag = new CompoundTag();
        tag.putInt("vertex_id", vertex_id);
        tag.putInt("edge_id", edge_id);
        CompoundTag[] edge_tags =  edges.stream()
                .map(Edge::toTag)
                .toArray(CompoundTag[]::new);
        tag.put("edges", NBTUtil.compoundTagArrayToCompoundTag(edge_tags));

        CompoundTag[] vertex_tags =  vertices.stream()
                .map(Vertex::toTag)
                .toArray(CompoundTag[]::new);
        tag.put("vertex", NBTUtil.compoundTagArrayToCompoundTag(vertex_tags));
        return tag;
    }

    public void serverTick(){
        vertices.forEach(Vertex::preServerTick);
        edges.forEach(Edge::serverTick);
        vertices.forEach(Vertex::postServerTick);
    }
}
