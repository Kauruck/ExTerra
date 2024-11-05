package com.kauruck.exterra.api.matter;


import com.kauruck.exterra.modules.ExTerraRegistries;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class Matter{

    public static final Codec<Matter> CODEC = ResourceLocation.CODEC.xmap(
            ExTerraRegistries.MATTER::get,
            ExTerraRegistries.MATTER::getKey
    );

    protected final Vec3 particleColor;
    protected final int energy;

    public Matter(MatterProperties properties) {
        this.particleColor = properties.particleColor;
        this.energy = properties.energy;
    }

    public Vec3 getParticleColor() {
        return particleColor;
    }

    public int getEnergy() {
        return energy;
    }

    public static class MatterProperties {
        private Vec3 particleColor;
        private int energy;

        public MatterProperties setParticleColor(Vec3 particleColor) {
            this.particleColor = particleColor;
            return this;
        }

        public MatterProperties setEnergy(int energy) {
            this.energy = energy;
            return this;
        }
    }

    @Override
    public String toString() {
        Optional<ResourceKey<Matter>> key = ExTerraRegistries.MATTER.getResourceKey(this);
        return key.map(matterResourceKey -> matterResourceKey.location().getPath()).orElse("How did we get here");
    }
}
