package com.kauruck.exterra.fx;

import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;

public class VectorHelper {

    public static Vector3f blockPosToVector(BlockPos pos){
        return new Vector3f(pos.getX(), pos.getY(), pos.getZ());
    }
    public static Vector3f blockPosCenterToVector(BlockPos pos){
        return new Vector3f(pos.getX() + .5f, pos.getY() + .5f, pos.getZ() + .5f);
    }

    public static boolean allZero(Vector3f vec){
        return vec.x() == 0 && vec.y() == 0 && vec.z() == 0;
    }

    public static boolean OverShotOnLine(Vector3f start, Vector3f end, Vector3f test){
        return distance(start, end) >= distance(start, test);
    }

    public static float distance(Vector3f a, Vector3f b){
        float dx = b.x() - a.x();
        float dy = b.y() - a.y();
        float dz = b.z() - a.z();
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
