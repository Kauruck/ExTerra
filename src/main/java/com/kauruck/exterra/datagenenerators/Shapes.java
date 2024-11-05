package com.kauruck.exterra.datagenenerators;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.data.provider.ShapeDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class Shapes extends ShapeDataProvider {
    public Shapes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public void registerShapes() {
        this.registerShape(ExTerra.getResource("square"),4)
                .parallel('A', 'B', 'C', 'D')
                .parallel('A', 'D', 'B', 'C');
    }
}
