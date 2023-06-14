package com.kauruck.exterra.ingredients;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.api.recipes.ExTerraIngredient;
import net.minecraft.resources.ResourceLocation;

public class MatterIngredient extends ExTerraIngredient<MatterStack> {

    private final MatterStack stack;

    public MatterIngredient(MatterStack stack) {
        this.stack = stack;
    }

    public MatterStack getStack() {
        return stack;
    }

    @Override
    public ResourceLocation getSerializerLocation() {
        return ExTerra.getResource("const_matter");
    }

    @Override
    public boolean test(MatterStack stack) {
        return this.stack == stack;
    }
}
