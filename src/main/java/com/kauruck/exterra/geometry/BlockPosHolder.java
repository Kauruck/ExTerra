package com.kauruck.exterra.geometry;

import com.kauruck.exterra.util.PositionsUtil;
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
        int index = PositionsUtil.arrayIndexFromName(blockPosName);

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
                .map(PositionsUtil::arrayIndexFromName)
                .map((i) -> blockPoss[i])
                .toArray(BlockPos[]::new);
        return new BlockPosHolder(subPoss);
    }
}
