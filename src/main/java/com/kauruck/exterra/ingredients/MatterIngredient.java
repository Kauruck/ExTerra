package com.kauruck.exterra.ingredients;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.api.recipes.ExTerraIngredient;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

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
        return this.stack.getMatter() == stack.getMatter() && this.stack.getAmount() < stack.getAmount();
    }

    @Override
    public String toString() {
        return "Ingredient: " + stack.toString();
    }

    public boolean testMatter(Matter matter) {
        return this.stack.getMatter() == matter;
    }

    public boolean testMatters(List<Matter> matters) {
        return matters.stream().anyMatch(m -> m == stack.getMatter());
    }

    public int reduceAmount() {
        return stack.getAmount();
    }
}
