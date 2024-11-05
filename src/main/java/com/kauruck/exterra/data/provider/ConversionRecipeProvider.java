package com.kauruck.exterra.data.provider;

import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.api.recipes.ExTerraRecipe;
import com.kauruck.exterra.api.recipes.ExTerraRecipeManager;
import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.recipes.ConversionRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class ConversionRecipeProvider implements DataProvider {

    private final PackOutput output;
    private final CompletableFuture<HolderLookup.Provider> registries;
    private final PackOutput.PathProvider shapePathProvider;

    private final Map<ResourceLocation, ConversionRecipeBuilder> recipes = new HashMap<>();

    public ConversionRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        this.output = output;
        this.registries = registries;
        this.shapePathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "conversion");
    }

    protected ConversionRecipeBuilder registerRecipe(ResourceLocation name, MatterStack output){
        ConversionRecipeBuilder builder = new ConversionRecipeBuilder(name, output);
        recipes.put(name, builder);
        return builder;
    }

    protected abstract void registerRecipes();

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        return this.registries.thenCompose(provider -> this.run(pOutput, provider));
    }

    public CompletableFuture<?> run(final CachedOutput output, final HolderLookup.Provider registries) {
        recipes.clear();
        registerRecipes();
        List<CompletableFuture<?>> toGen = new ArrayList<>();
        for (ResourceLocation loc : recipes.keySet()) {
            toGen.add(DataProvider.saveStable(output, registries, ExTerraRecipeManager.CODEC,  recipes.get(loc).build(), shapePathProvider.json(loc)));
        }
        return CompletableFuture.allOf(toGen.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "ExTerra Conversion Recipes";
    }
}
