package com.kauruck.exterra.fx;

import com.mojang.math.Vector3f;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class ParticleHelper {

    // From Minecraft Redstone Wire
    public static void spawnParticlesAlongLine(ParticleOptions particleOptions, Level pLevel, RandomSource pRandom, Vector3f pPos, Direction pXDirection, Direction pZDirection, float pMin, float pMax) {
        float f = pMax - pMin;
        if (pRandom == null || (!(pRandom.nextFloat() >= 0.2F * f))) {
            float f2 = pRandom != null? pMin + f * pRandom.nextFloat() : pMin + f;
            double d0 = 0.5D + (double)(0.4375F * (float)pXDirection.getStepX()) + (double)(f2 * (float)pZDirection.getStepX());
            double d1 = 0.5D + (double)(0.4375F * (float)pXDirection.getStepY()) + (double)(f2 * (float)pZDirection.getStepY());
            double d2 = 0.5D + (double)(0.4375F * (float)pXDirection.getStepZ()) + (double)(f2 * (float)pZDirection.getStepZ());
            pLevel.addParticle(particleOptions, (double)pPos.x() + d0, (double)pPos.y() + d1, (double)pPos.z() + d2, 0.0D, 0.0D, 0.0D);
        }
    }

    public static void spawnParticlesAlongLine(Level pLevel, RandomSource pRandom, BlockPos pPos, Vec3 pParticleVec, Direction pXDirection, Direction pZDirection, float pMin, float pMax) {
        float f = pMax - pMin;
        if (!(pRandom.nextFloat() >= 0.2F * f)) {
            float f1 = 0.4375F;
            float f2 = pMin + f * pRandom.nextFloat();
            double d0 = 0.5D + (double)(0.4375F * (float)pXDirection.getStepX()) + (double)(f2 * (float)pZDirection.getStepX());
            double d1 = 0.5D + (double)(0.4375F * (float)pXDirection.getStepY()) + (double)(f2 * (float)pZDirection.getStepY());
            double d2 = 0.5D + (double)(0.4375F * (float)pXDirection.getStepZ()) + (double)(f2 * (float)pZDirection.getStepZ());
            pLevel.addParticle(new DustParticleOptions(new Vector3f(pParticleVec), 1.0F), (double)pPos.getX() + d0, (double)pPos.getY() + d1, (double)pPos.getZ() + d2, 0.0D, 0.0D, 0.0D);
        }
    }

    public static void emitParticlesOnLine(ClientLevel level, Vector3f start, Vector3f end, Vector3f color, float scale, RandomSource random){
        ParticleOptions particleOptions = new DustParticleOptions(color, scale);
        Vector3f delta = end.copy();
        delta.sub(start);
        delta.normalize();
        for(Vector3f currentPos = start.copy(); VectorHelper.OverShotOnLine(start, end, currentPos); currentPos.add(delta)){
            for(Direction direction : Direction.Plane.HORIZONTAL) {
                spawnParticlesAlongLine(particleOptions, level, random, currentPos, Direction.DOWN, direction, 0F, .5F);
            }
        }
    }

    public static void emitParticlesOnLine(ClientLevel level, Vector3f start, Vector3f end, Vector3f color, float scale){
        ParticleOptions particleOptions = new DustParticleOptions(color, scale);
        Vector3f delta = end.copy();
        delta.sub(start);
        delta.normalize();
        if(VectorHelper.allZero(delta)){
            spawnParticlesAlongLine(particleOptions, level, null, start, Direction.DOWN, Direction.UP, 0F, .5F);
        }
        else{
            for(Vector3f currentPos = start.copy(); VectorHelper.OverShotOnLine(start, end, currentPos); currentPos.add(delta)){
                spawnParticlesAlongLine(particleOptions, level, null, currentPos, Direction.DOWN, Direction.UP, 0F, .5F);
            }
        }
    }

    public static void emitParticlePoint(ClientLevel level, Vector3f point, Vector3f color, float scale){
        ParticleOptions particleOptions = new DustParticleOptions(color, scale);
        level.addParticle(particleOptions, (double)point.x() +0.5, (double)point.y() + 1, (double)point.z() + 0.5, 0.0D, 0.0D, 0.0D);
    }
}
