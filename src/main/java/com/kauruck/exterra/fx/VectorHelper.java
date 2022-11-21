package com.kauruck.exterra.fx;

import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;

public class VectorHelper {

    public static Vector3f blockPosToVector(BlockPos pos){
        return new Vector3f(pos.getX(), pos.getY(), pos.getZ());
    }
}
