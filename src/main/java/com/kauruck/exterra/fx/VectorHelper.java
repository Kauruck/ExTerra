package com.kauruck.exterra.fx;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

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

    public static Vector3f fromPhysicsVec(Vec3 vec3) {
        return new Vector3f((float) vec3.x(), (float) vec3.y(), (float) vec3.z());
    }

    public static Vector3f copy(Vector3f vec) {
        return new Vector3f(vec.x(), vec.y(), vec.z());
    }
}
