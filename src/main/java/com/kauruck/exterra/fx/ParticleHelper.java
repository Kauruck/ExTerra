package com.kauruck.exterra.fx;

import com.mojang.math.Vector3f;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;

public class ParticleHelper {

    public static void spawnLineWithColor(ClientLevel level, Vector3f start, Vector3f end, Vector3f color,float scale, float speed, int tick){
        DustParticleOptions particleOptions = new DustParticleOptions(color, scale);
        Vector3f delta = end.copy();
        delta.sub(start);
        delta.normalize();
        delta.mul(speed);
        level.addParticle(particleOptions, start.x(), start.y(), start.z(), delta.x(), delta.y(), delta.z());
    }
}
