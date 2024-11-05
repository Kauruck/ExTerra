package com.kauruck.exterra.data.provider;


import com.kauruck.exterra.data.ShapeData;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.*;
import net.minecraft.resources.ResourceLocation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class ShapeDataProvider implements DataProvider {

    private final Map<ResourceLocation, ShapeDataBuilder> shapes = new HashMap<>();

    protected ShapeDataBuilder registerShape(ResourceLocation name, int numberOfPoints){
        ShapeDataBuilder builder = new ShapeDataBuilder(name, numberOfPoints);
        shapes.put(name, builder);
        return builder;
    }

    private final PackOutput output;
    private final CompletableFuture<HolderLookup.Provider> registries;
    private final PackOutput.PathProvider shapePathProvider;


    public ShapeDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        this.output = output;
        this.registries = registries;
        this.shapePathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "shapes");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        return this.registries.thenCompose(provider -> this.run(pOutput, provider));
    }

    public CompletableFuture<?> run(final CachedOutput output, final HolderLookup.Provider registries) {
        shapes.clear();
        registerShapes();
        List<CompletableFuture<?>> toGen = new ArrayList<>();
        for (ResourceLocation loc : shapes.keySet()) {
            toGen.add(DataProvider.saveStable(output, registries, ShapeData.FULL_CODEC, shapes.get(loc).build(), shapePathProvider.json(loc)));
        }
        return CompletableFuture.allOf(toGen.toArray(CompletableFuture[]::new));
    }

    public abstract void registerShapes();

    @Override
    public String getName() {
        return "ExTerra Shapes";
    }
}
