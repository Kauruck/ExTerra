package com.kauruck.exterra.datagenenerators;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.data.provider.ShapeDataProvider;
import net.minecraft.data.DataGenerator;

public class Shapes extends ShapeDataProvider {
    public Shapes(DataGenerator generator) {
        super(generator, ExTerra.MOD_ID);
    }

    @Override
    public void registerShapes() {
        this.registerShape(ExTerra.getResource("square"),4)
                .parallel('A', 'B', 'C', 'D')
                .parallel('A', 'D', 'B', 'C');
    }
}
