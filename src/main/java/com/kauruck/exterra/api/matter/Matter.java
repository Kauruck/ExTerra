package com.kauruck.exterra.api.matter;


import com.kauruck.exterra.modules.ExTerraRegistries;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Optional;

public class Matter{

    protected final Color particleColor;
    protected final int energy;

    public Matter(MatterProperties properties) {
        this.particleColor = properties.particleColor;
        this.energy = properties.energy;
    }


    public static class MatterProperties {
        private Color particleColor;
        private int energy;

        public MatterProperties setParticleColor(Color particleColor) {
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
        Optional<ResourceKey<Matter>> key = ExTerraRegistries.MATTER.get().getResourceKey(this);
        return key.map(matterResourceKey -> matterResourceKey.location().getPath()).orElse("How did we get here");
    }
}
