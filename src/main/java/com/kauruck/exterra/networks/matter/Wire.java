package com.kauruck.exterra.networks.matter;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.kauruck.exterra.fx.ParticleHelper;
import com.kauruck.exterra.util.NBTUtil;
import com.mojang.math.Vector3f;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.kauruck.exterra.blocks.DustBlock.*;

public class Wire {

    private final List<BlockPos> positions = new ArrayList<>();
    private BlockPos terminalA;
    private BlockPos terminalB;

    public static final Map<Direction, EnumProperty<RedstoneSide>> PROPERTY_BY_DIRECTION = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST));

    private Grid grid;

    public Wire(Grid grid){
        this.grid = grid;
    }

    public void appendBlock(BlockPos pos){
        positions.add(pos);
    }

    public void emitParticles(Vec3 color, ClientLevel pLevel, RandomSource pRandom) {
        if(grid == null)
            return;
        for(BlockPos currentPos : positions) {
            BlockState pState = grid.getBlockStateAt(currentPos);
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                RedstoneSide redstoneside = pState.getValue(PROPERTY_BY_DIRECTION.get(direction));
                switch (redstoneside) {
                    case UP:
                        ParticleHelper.spawnParticlesAlongLine(pLevel, pRandom, currentPos, color, direction, Direction.UP, -0.5F, 0.5F);
                    case SIDE:
                        ParticleHelper.spawnParticlesAlongLine(pLevel, pRandom, currentPos, color, Direction.DOWN, direction, 0.0F, 0.5F);
                        break;
                    case NONE:
                    default:
                        ParticleHelper.spawnParticlesAlongLine(pLevel, pRandom, currentPos, color, Direction.DOWN, direction, 0.0F, 0.3F);
                }
            }
        }
    }

    public void setTerminalA(BlockPos terminalA) {
        this.terminalA = terminalA;
    }

    public void setTerminalB(BlockPos terminalB) {
        this.terminalB = terminalB;
    }

    public List<BlockPos> getPositions() {
        return positions;
    }

    public BlockPos getTerminalA() {
        return terminalA;
    }

    public BlockPos getTerminalB() {
        return terminalB;
    }

    public CompoundTag toNBT(){
        CompoundTag out = new CompoundTag();
        int index = 0;
        for(BlockPos currentPos : positions){
            out.put(Integer.toString(index), NBTUtil.blockPosToNBT(currentPos));
            index++;
        }
        out.putInt("length", index);
        return out;
    }

    public static Wire fromNBT(CompoundTag tag, Grid grid){
        int length = tag.getInt("length");
        Wire out = new Wire(grid);
        for(int i = 0; i < length; i++){
            out.appendBlock(NBTUtil.blockPosFromNBT(tag.getCompound(Integer.toString(i))));
        }
        return out;
    }

    public void bindGrid(Grid grid) {
        this.grid = grid;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof  Wire))
            return false;
        Wire other = (Wire) obj;
        boolean terminalEquals = false;
        terminalEquals = (other.terminalA.equals(this.terminalA) && other.terminalB.equals(this.terminalB)) ||
                (other.terminalA.equals(this.terminalB) && other.terminalB.equals(this.terminalA));

        boolean lengthSame = this.positions.size() == other.positions.size();

        //TODO do we need the HashSet?
        boolean allElementsIncluded = new HashSet<>(this.positions).containsAll(other.positions);

        return terminalEquals && lengthSame && allElementsIncluded;
    }
}
