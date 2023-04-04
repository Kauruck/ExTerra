package com.kauruck.exterra.networks.matter;

import com.kauruck.exterra.api.blockentity.NotIterableInProperty;
import com.kauruck.exterra.util.NBTUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Grid implements Iterable<GridCellType>, NotIterableInProperty {

    private final int size;
    private BlockState[][] grid;
    private final BlockPos center;
    private List<Wire> wires = new ArrayList<>();

    public Grid(int size, BlockPos center) {
        this.size = size;
        grid = new BlockState[2 * size + 1][2 * size + 1];
        this.center = center;
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

    public void setWires(List<Wire> wires){
        this.wires = wires;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("\n");
        for (int x = -size; x < size + 1; x++) {
            for (int z = -size; z < size + 1; z++) {
                String cellString = this.getCell(x, z).toString();
                builder.append(cellString);
            }
            builder.append('\n');
        }
        return builder.toString();
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
        comp.append(Integer.toString(wires.size()));
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

    public Tag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("size", this.size);
        tag.put("center", NBTUtil.blockPosToNBT(this.center));
        for(int x = -size; x < size + 1; x++){
            CompoundTag rowTag = new CompoundTag();
            for(int y = -size; y < size + 1; y++){
                BlockState state = this.getBlockStateAt(x,y);
                if(state != null)
                    rowTag.put(Integer.toString(y), NbtUtils.writeBlockState(state));
            }
            tag.put(Integer.toString(x), rowTag);
        }
        tag.put("wires", wires.stream()
                .map(Wire::toNBT)
                .collect(NBTUtil.toSingleCompoundTag()));
        return tag;
    }

    public static Grid fromNBT(Tag nbt) {
        CompoundTag tag = (CompoundTag) nbt;
        int size = tag.getInt("size");
        BlockPos center = NBTUtil.blockPosFromNBT(tag.getCompound("center"));
        Grid out = new Grid(size, center);
        for(int x = -size; x < size + 1; x++){
            CompoundTag rowTag = tag.getCompound(Integer.toString(x));
            for(int y = -size; y < size + 1; y++){
                BlockState state = null;
                if(rowTag.contains(Integer.toString(y)))
                    state = NbtUtils.readBlockState(rowTag.getCompound(Integer.toString(y)));
                out.setCell(x,y, state);
            }
            tag.put(Integer.toString(x), rowTag);
        }
        List<Wire> wires = NBTUtil.fromSingleCompoundTag(tag.getCompound("wires"))
                .map(current -> Wire.fromNBT(current, out))
                .toList();
        out.wires = wires;
        return out;
    }

    public void animationsTick(ClientLevel level, RandomSource random){
        wires.forEach(wire -> wire.emitParticles(new Vec3(1, 0, 0), level, random));
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
}
