package com.kauruck.exterra.data.provider;


import com.kauruck.exterra.data.ShapeData;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class ShapeDataProvider extends GenericDataProvider {

    private final Map<ResourceLocation, ShapeDataBuilder> shapes = new HashMap<>();

    protected ShapeDataBuilder registerShape(ResourceLocation name, int numberOfPoints){
        ShapeDataBuilder builder = new ShapeDataBuilder(name, numberOfPoints);
        shapes.put(name, builder);
        return builder;
    }

    public ShapeDataProvider(DataGenerator generator, String mod_id) {
        super(generator, PackType.SERVER_DATA, "shapes", mod_id);
    }

    @Override
    public void run(CachedOutput pCache){
        shapes.clear();
        registerShapes();
        for(ResourceLocation currentName : shapes.keySet()){
            ShapeData data = shapes.get(currentName).build();
            this.saveThing(pCache, currentName.getPath(), data);
        }
    }

    public abstract void registerShapes();

    @Override
    public String getName() {
        return "ExTerra Shapes";
    }
}
