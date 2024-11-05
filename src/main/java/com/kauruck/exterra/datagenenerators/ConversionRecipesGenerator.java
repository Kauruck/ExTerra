package com.kauruck.exterra.datagenenerators;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.data.provider.ConversionRecipeProvider;
import com.kauruck.exterra.modules.ExTerraCore;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public class ConversionRecipesGenerator extends ConversionRecipeProvider {

    public ConversionRecipesGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void registerRecipes() {
        this.registerRecipe(ExTerra.getResource("test"), new MatterStack(ExTerraCore.TEST_MATTER_2.get(), 1))
                .withMatterStack(new MatterStack(ExTerraCore.TEST_MATTER.get(), 10))
                .withShapes(ExTerra.getResource("square"));
    }
}
