package com.kauruck.exterra.data.provider;

import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.api.recipes.ExTerraIngredient;
import com.kauruck.exterra.ingredients.MatterIngredient;
import com.kauruck.exterra.recipes.ConversionRecipe;
import com.kauruck.exterra.util.StreamHelper;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConversionRecipeBuilder {

    private final ResourceLocation name;
    private final MatterStack output;

    private final List<ExTerraIngredient<MatterStack>> ingredients = new ArrayList<>();
    private final List<ResourceLocation> shapes = new ArrayList<>();

    public ConversionRecipeBuilder(ResourceLocation name, MatterStack output) {
        this.name = name;
        this.output = output;
    }

    public ConversionRecipeBuilder withMatterStack(MatterStack stack) {
        this.ingredients.add(new MatterIngredient(stack));
        return this;
    }

    public ConversionRecipeBuilder addIngredients(ExTerraIngredient<MatterStack>... ingredients){
        this.ingredients.addAll(Arrays.asList(ingredients));
        return this;
    }

    public ConversionRecipeBuilder withShapes(ResourceLocation... shapes){
        this.shapes.addAll(Arrays.asList(shapes));
        return this;
    }

    ConversionRecipe build() {
        if (ingredients.isEmpty()) {
            throw new IllegalStateException("There must at least be one ingredient");
        }
        return new ConversionRecipe(this.ingredients.stream().collect(StreamHelper.toNonNullList()),
                this.shapes, this.output);
    }
}
