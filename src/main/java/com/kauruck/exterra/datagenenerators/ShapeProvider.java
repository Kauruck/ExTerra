package com.kauruck.exterra.datagenenerators;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.data.ShapeData;
import com.kauruck.exterra.data.provider.ShapeDataProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.server.packs.PackType;

public class ShapeProvider extends ShapeDataProvider {
    public ShapeProvider(DataGenerator generator) {
        super(generator, ExTerra.MOD_ID);
    }

    @Override
    public void registerShapes() {
        this.registerShape("square",4)
                .parallel('A', 'B', 'C', 'D')
                .parallel('A', 'C', 'B', 'D');
    }
}
