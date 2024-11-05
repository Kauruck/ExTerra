package com.kauruck.exterra.networks.matter;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.exceptions.UnexpectedBehaviorException;
import com.kauruck.exterra.api.networks.matter.INetworkMember;
import com.kauruck.exterra.geometry.Shape;
import com.kauruck.exterra.util.NBTUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MatterNetwork {

    public static final Codec<MatterNetwork> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("vertex_id").forGetter(MatterNetwork::getVertex_id),
                    Codec.INT.fieldOf("edge_id").forGetter(MatterNetwork::getEdge_id),
                    Codec.list(Vertex.CODEC).fieldOf("vertices").forGetter(MatterNetwork::getVertices),
                    Codec.list(Edge.CODEC).fieldOf("edges").forGetter(MatterNetwork::getEdges)
            ).apply(instance, MatterNetwork::new)
    );

    public static final Codec<MatterNetwork> NETWORK_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("edge_id").forGetter(MatterNetwork::getEdge_id),
                    Codec.list(Edge.CODEC).fieldOf("edges").forGetter(MatterNetwork::getEdges)
            ).apply(instance, MatterNetwork::new)
    );

    List<Vertex> vertices = new ArrayList<>();
    List<Edge> edges = new ArrayList<>();

    Supplier<List<Shape>> shapeGetter = null;

    private int vertex_id = 0;
    private int edge_id = 0;
    private boolean linked = true;

    private Level level;

    public MatterNetwork(){
    }

    private MatterNetwork(int vertex_id, int edge_id, List<Vertex> vertices, List<Edge> edges) {
        this.vertex_id = vertex_id;
        this.edge_id = edge_id;
        this.vertices = vertices;
        this.edges = edges;
        this.linked = false;
    }

    private MatterNetwork(int edge_id, List<Edge> edges) {
        this.vertex_id = -1;
        this.edge_id = edge_id;
        this.vertices = new ArrayList<>();
        this.edges = edges;
        this.linked = false;
    }

    private MatterNetwork(boolean linked){
        this.linked = linked;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public int getVertex_id() {
        return vertex_id;
    }

    public int getEdge_id() {
        return edge_id;
    }

    private void addEdge(Vertex a, Vertex b, Wire wire){
        Edge edge = new Edge(a,b,edge_id, wire, this);
        if(edges.contains(edge))
            return;
        a.addEdge(edge);
        b.addEdge(edge);
        edges.add(edge);
        edge_id ++;
    }

    public void addEdge(Wire wire){
        Vertex a = vertices.stream()
                .filter(vertex -> vertex.getPosition().equals(wire.getTerminalA()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(wire.getTerminalA().toString()));

        Vertex b = vertices.stream()
                .filter(vertex -> vertex.getPosition().equals(wire.getTerminalB()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(wire.getTerminalB().toString()));
        this.addEdge(a, b, wire);
    }

    private void addVertex(Vertex vertex) throws UnexpectedBehaviorException {
        //Check weather the block can handle being in a matter network
        if(vertex.getLevel().getBlockEntity(vertex.getPosition()) instanceof INetworkMember){
            vertex.loadFromWorld();
            vertices.add(vertex);
        }
        else {
            ExTerra.LOGGER.info("Block at {} is not an INetworkMember", vertex.getPosition());
        }
    }

    public void addVertex(BlockPos pos, Level level) throws UnexpectedBehaviorException {
        addVertex(new Vertex(pos, level, vertex_id));
        vertex_id++;
    }


    public boolean isLinked() {
        return linked;
    }

    public void link(Level level) {
        this.level = level;
        this.vertices.forEach(vertex -> {
            try {
                vertex.link(this, level);
            } catch (UnexpectedBehaviorException e) {
                throw new RuntimeException(e);
            }
        });
        this.edges.forEach(edge -> edge.link(this));
        this.linked = true;
    }

    public void setShapeGetter(Supplier<List<Shape>> shapeGetter) {
        this.shapeGetter = shapeGetter;
    }

    public List<Shape> getShapes() {
        if(this.shapeGetter != null) {
            return shapeGetter.get();
        }
        return new ArrayList<>();
    }

    public Level getLevel() {
        return level;
    }

    public void serverTick(){
        vertices.forEach(Vertex::preServerTick);
        edges.forEach(Edge::serverTick);
        vertices.forEach(Vertex::postServerTick);
    }
    public void animationsTick(ClientLevel level, RandomSource random){
        edges.forEach(edge -> edge.animationTick(level, random));
    }

    public void addRangeVertices(Collection<BlockPos> vertices, Level level) throws UnexpectedBehaviorException {
        for(BlockPos current : vertices)
            this.addVertex(current, level);
    }

    public void addRangeEdge(Collection<Wire> wires){
        for(Wire current : wires)
            this.addEdge(current);
    }


    @Override
    public String toString() {
        return edges.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MatterNetwork that = (MatterNetwork) o;

        if (vertex_id != that.vertex_id) return false;
        if (edge_id != that.edge_id) return false;
        if (linked != that.linked) return false;
        if (!Objects.equals(vertices, that.vertices)) return false;
        return (!Objects.equals(edges, that.edges));
    }

    @Override
    public int hashCode() {
        int result = vertices != null ? vertices.hashCode() : 0;
        result = 31 * result + (edges != null ? edges.hashCode() : 0);
        result = 31 * result + vertex_id;
        result = 31 * result + edge_id;
        result = 31 * result + (linked ? 1 : 0);
        return result;
    }
}
