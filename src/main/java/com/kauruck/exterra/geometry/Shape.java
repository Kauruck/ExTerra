package com.kauruck.exterra.geometry;

import com.kauruck.exterra.util.NBTUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Shape {

    List<BlockPos> positions = new ArrayList<>();

    public Shape(){

    }

    public Shape(CompoundTag nbt){
        this.fromNBT(nbt);
    }

    public void addPoint(BlockPos point){
        positions.add(point);
    }

    public boolean containsPoint(BlockPos point){
        return positions.contains(point);
    }

    @Override
    public String toString() {
        return "contains: " + positions.size() + " positions";
    }

    public void fromNBT(CompoundTag tag){
        int size = tag.getInt("size");
        this.positions.clear();
        for(int i = 0; i < size; i++){
            BlockPos pos = NBTUtil.blockPosFromNBT(tag.getCompound(Integer.toString(i)));
            this.positions.add(pos);
        }
    }


    public CompoundTag toNBT(){
        CompoundTag tag = new CompoundTag();
        tag.putInt("size", positions.size());
        for(int i = 0; i < positions.size(); i++){
            tag.put(Integer.toString(i), NBTUtil.blockPosToNBT(positions.get(i)));
        }
        return tag;
    }



    public List<BlockPos> getActualPositions(BlockPos center) {
        List<BlockPos> out = new ArrayList<>();
        for(BlockPos current : positions){
            out.add(current.offset(center));
        }
        return out;
    }
}
