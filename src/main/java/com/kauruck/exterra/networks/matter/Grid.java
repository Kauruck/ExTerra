package com.kauruck.exterra.networks.matter;

import com.kauruck.exterra.api.blockentity.NotIterableInProperty;
import com.kauruck.exterra.api.networks.matter.INetworkMemberBlock;
import com.kauruck.exterra.networking.ExTerraCodecs;
import com.kauruck.exterra.util.NBTUtil;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class Grid implements Iterable<GridCellType>, NotIterableInProperty {

    public static final Codec<Grid> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("size").forGetter(Grid::getSize),
                    BlockPos.CODEC.fieldOf("center").forGetter(Grid::getCenter),
                    Codec.list(Codec.list(BlockState.CODEC)).fieldOf("grid_data").forGetter(Grid::getGridAsList),
                    ExTerraCodecs.LEVEL_CODEC.fieldOf("level").forGetter(Grid::getLevel)
            ).apply(instance, Grid::new));

    private final int size;
    private BlockState[][] grid;
    private final BlockPos center;
    private final Level level;

    public Grid(int size, BlockPos center, Level level) {
        this.size = size;
        grid = new BlockState[2 * size + 1][2 * size + 1];
        this.center = center;
        this.level = level;
    }


    public Grid(int size, BlockPos center, List<List<BlockState>> gridList, Level level) {
        this.center = center;
        BlockState[][] grid = new BlockState[size][size];
        for(int x = -size; x < size + 1; x++){
            List<BlockState> inner = gridList.get(x);
            for(int y = -size; y < size + 1; y++){
                BlockState state = inner.get(y);
                grid[x][y] = state;
            }
        }
        this.grid = grid;
        this.size = size;
        this.level = level;
    }

    public int getSize() {
        return size;
    }

    public BlockPos getCenter() {
        return center;
    }

    public BlockPos localToBlockPos(int x, int z){
        int dX = center.getX() - x;
        int dZ = center.getZ() - z;
        return new BlockPos(dX, center.getY(), dZ);
    }

    public Tuple<Integer, Integer> blockPosToLocal(BlockPos pos){
        int dX = center.getX() - pos.getX();
        int dZ = center.getZ() - pos.getZ();

        return new Tuple<>(dX, dZ);
    }

    public boolean has(int x, int z){
        return x >= -size && x < size+1 && z >= -size && z < size+1;
    }

    public void setCell(BlockPos pos, BlockState state) {
        Tuple<Integer, Integer> localPos = blockPosToLocal(pos);
        int dX = localPos.getA() + size;
        int dZ = localPos.getB() + size;

        if (dX < 0 || dX > 2 * size || dZ < 0 || dZ > 2 * size) {
            throw new IndexOutOfBoundsException("Tried to access Cell that is not in the grid");
        }

        grid[dX][dZ] = state;
    }

    public void setCell(int pX, int pY, BlockState state) {
        int dX = pX + size;
        int dZ = pY + size;

        if (dX < 0 || dX > 2 * size || dZ < 0 || dZ > 2 * size) {
            throw new IndexOutOfBoundsException("Tried to access Cell that is not in the grid");
        }

        grid[dX][dZ] = state;
    }

    public GridCellType getCell(int pX, int pZ) {
        int dX = pX + size;
        int dZ = pZ + size;

        if (dX < 0 || dX > 2 * size || dZ < 0 || dZ > 2 * size) {
            throw new IndexOutOfBoundsException("Tried to access Cell that is not in the grid");
        }

        return grid[dX][dZ] == null ? GridCellType.Air : GridCellType.match(grid[dX][dZ]);
    }

    public GridCellType getCell(BlockPos pos) {
        Tuple<Integer, Integer> localPos = blockPosToLocal(pos);

        return getCell(localPos.getA(), localPos.getB());
    }

    public BlockState getBlockStateAt(int pX, int pZ){
        int dX = pX + size;
        int dZ = pZ + size;

        if (dX < 0 || dX > 2 * size || dZ < 0 || dZ > 2 * size) {
            throw new IndexOutOfBoundsException("Tried to access Cell that is not in the grid");
        }

        return grid[dX][dZ];
    }

    public BlockState getBlockStateAt(BlockPos pos){
        Tuple<Integer, Integer> localPos = blockPosToLocal(pos);
        return getBlockStateAt(localPos.getA(), localPos.getB());
    }

    public boolean canConnectTo(int wX, int wY, int tX, int tY) {
        Direction dir = this.getDeltaDirection(wX, wY, tX, tY);
        if (dir == null) { // Pos are not neighbours
            return false;
        }

        Block block = this.getBlockStateAt(tX, tY).getBlock();
        if (block instanceof INetworkMemberBlock memberBlock) {
            return memberBlock.canConnectTo(dir, this.getBlockStateAt(tX, tY), level);
        } else {
            return false;
        }
    }

    public Direction getDeltaDirection(int wX, int wY, int tX, int tY) {
        if (!((Math.abs(wX - tX) == 1 && wY == tY) || (Math.abs(wY - tY) == 1 && wX == tX))) {
            return null;
        }

        if (wY == tY) {
            if (wX - 1 == tX) {
                return Direction.SOUTH;
            } else {
                return Direction.NORTH;
            }
        } else {
            if (wY - 1 == tY) {
                return Direction.WEST;
            } else {
                return Direction.EAST;
            }
        }
    }


    public MutableComponent forChat() {
        MutableComponent comp = Component.literal("Grid\n");
        for (int x = -size; x < size + 1; x++) {
            for (int z = -size; z < size + 1; z++) {
                String cellString = this.getCell(x, z).toString();
                comp.append(cellString);
            }
            comp.append("\n");
        }
        return comp;
    }

    @NotNull
    @Override
    public Iterator<GridCellType> iterator() {
        return new GridIterator(this);
    }

    public int size() {
        return size;
    }

    public Level getLevel() {
        return level;
    }

    public List<List<BlockState>> getGridAsList() {
        List<List<BlockState>> outer = new ArrayList<>();
        for(int x = -size; x < size + 1; x++){
            List<BlockState> inner = new ArrayList<>();
            for(int y = -size; y < size + 1; y++){
                BlockState state = this.getBlockStateAt(x,y);
                inner.add(state);
            }
            outer.add(inner);
        }
        return outer;
    }


    public static class GridIterator implements Iterator<GridCellType>{

        private final Grid grid;
        private final int size;
        private int x;
        private int z;

        public GridIterator(Grid grid) {
            this.grid = grid;
            this.size = grid.size;
            x=-size;
            z=-size;
        }

        @Override
        public boolean hasNext() {
            return z<2*size+1;
        }

        @Override
        public GridCellType next() {
            GridCellType cell = grid.getCell(x, z);
            x++;
            if(x >= 2*size+1){
                x=-size;
                z++;
            }
            return cell;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Grid that = (Grid) o;

        if (size != that.size) return false;
        if (!Arrays.deepEquals(grid, that.grid)) return false;
        return Objects.equals(center, that.center);
    }

    @Override
    public int hashCode() {
        int result = size;
        result = 31 * result + Arrays.deepHashCode(grid);
        result = 31 * result + (center != null ? center.hashCode() : 0);
        return result;
    }
}
