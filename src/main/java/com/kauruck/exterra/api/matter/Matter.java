package com.kauruck.exterra.api.matter;


import java.awt.*;

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
}
