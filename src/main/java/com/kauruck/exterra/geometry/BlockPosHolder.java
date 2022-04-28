package com.kauruck.exterra.geometry;

import net.minecraft.core.BlockPos;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BlockPosHolder {

    private final BlockPos[] blockPoss;

    public BlockPosHolder(int size){
        this.blockPoss = new BlockPos[size];
    }

    public BlockPosHolder(BlockPos... blockPoss){
        this.blockPoss = blockPoss;
    }


    public BlockPos getBlockPos(Character blockPosName){
        //Force all characters to be uppercase
        blockPosName = Character.toUpperCase(blockPosName);
        //convert it to an int
        int index = blockPosName - 65;
        //ensure it is in range
        if(index >= 90 || index < 0)
            throw new IllegalArgumentException("The argument can only be A-Z");

        if(index >= blockPoss.length)
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for length " + blockPoss.length);

        //return the corresponding blockpos
        return blockPoss[index];
    }

    public int size(){
        return blockPoss.length;
    }

    public BlockPosHolder subHolder(Character... blockPosNames){
        BlockPos[] subPoss = Arrays.stream(blockPosNames)
                .map((c) -> c - 65)
                .filter((i) -> i >= 0 && i <= blockPoss.length && i < 90)
                .map((i) -> blockPoss[i])
                .toArray(BlockPos[]::new);
        return new BlockPosHolder(subPoss);
    }
}
