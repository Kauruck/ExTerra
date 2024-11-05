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

    private final Map<Matter, List<MatterStack>> availableMatters = new HashMap<>();

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

    public MatterStack pullMatterStack(Matter matter){
        int timesTransported = getTimesTransported(matter);
        if(availableMatters.containsKey(matter)){
            List<MatterStack> stacks = availableMatters.get(matter);
            if(stacks.size() == 0){
                availableMatters.remove(matter);
                return null;
            }
            MatterStack stack = availableMatters.get(matter).get(0);
            availableMatters.get(matter).remove(0);
            return stack;
        }
        return null;
    }

    public void preServerTick(){
        // Add all the matters this node offers
        availableMatters.clear();
        availableMatters.putAll(Arrays.stream(member.pullMatter())
                        .filter(Objects::nonNull)
                        .filter(current -> current.getAmount() != 0)
                        .map(stack -> new Tuple<Matter, List<MatterStack>>(stack.getMatter(), splitStack(stack)))
                        .collect(
                        Collectors.toMap(
                                Tuple::getA,
                                Tuple::getB
                        )
        ));
    }

    private List<MatterStack> splitStack(MatterStack stack){
        int timesTransported = getTimesTransported(stack.getMatter());
        List<MatterStack> out = new ArrayList<>();
        if(timesTransported == 0){
            out.add(stack);
            return out;
        }
        int remainder = stack.getAmount() % timesTransported;
        int stackSize = (stack.getAmount() - remainder) / timesTransported;
        for(int i = 0; i < timesTransported; i++){
            if(remainder > 0) {
                out.add(new MatterStack(stack.getMatter(), stackSize + 1));
                remainder--;
            }
            else{
                out.add(new MatterStack(stack.getMatter(), stackSize));
            }
        }
        return out;
    }

    public void postServerTick(){
        MatterStack[] remainder = availableMatters.values().stream()
                .filter(list -> list.size() > 0)
                .map(list -> new MatterStack(list.get(0).getMatter(), list.stream().map(MatterStack::getAmount).reduce(0, Integer::sum)))
                .filter(stack -> stack.getAmount() > 0)
                .toArray(MatterStack[]::new);

        // Apply backpressure
        if(remainder.length != 0){
            member.applyBackpressure(remainder);
        }
    }

    public void applyBackpressure(List<MatterStack> remainderList) {
        for(MatterStack currentStack : remainderList){
            if(availableMatters.containsKey(currentStack.getMatter())){
                //There should not be a delta here
                availableMatters.get(currentStack.getMatter()).add(currentStack);
            }
            //If it is not in the availableMatters Map I don't know how it got there
        }
    }


}
