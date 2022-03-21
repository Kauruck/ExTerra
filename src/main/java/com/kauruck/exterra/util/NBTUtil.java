package com.kauruck.exterra.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class NBTUtil {

    public static CompoundTag blockPosToNBT(BlockPos blockPos){
        CompoundTag tag = new CompoundTag();
        tag.putInt("x", blockPos.getX());
        tag.putInt("y", blockPos.getY());
        tag.putInt("z", blockPos.getZ());
        return tag;
    }

    public static BlockPos blockPosFromNBT(CompoundTag nbt){
        int x = nbt.getInt("x");
        int y = nbt.getInt("y");
        int z = nbt.getInt("z");

        return new BlockPos(x,y,z);
    }

    public static CompoundTag blockPosListToNBT(List<BlockPos> list){
        CompoundTag tag = new CompoundTag();
        tag.putInt("size", list.size());
        for(int i = 0; i < list.size(); i++){
            tag.put(Integer.toString(i), blockPosToNBT(list.get(i)));
        }
        return tag;
    }

    public static List<BlockPos> blockPosListFromNBT(CompoundTag tag){
        List<BlockPos> out = new ArrayList<>();
        int size = tag.getInt("size");
        for(int i = 0; i < size; i++){
            out.add(blockPosFromNBT(tag.getCompound(Integer.toString(i))));
        }
        return out;
    }
}
