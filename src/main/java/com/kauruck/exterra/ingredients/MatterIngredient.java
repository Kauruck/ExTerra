package com.kauruck.exterra.ingredients;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.api.recipes.ExTerraIngredient;
import com.kauruck.exterra.modules.ExTerraCore;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class MatterIngredient extends ExTerraIngredient<MatterStack> {

    public static final MapCodec<MatterIngredient> CODEC = MatterStack.CODEC
            .xmap(MatterIngredient::new, MatterIngredient::getStack)
            .fieldOf("matter");

    private final MatterStack stack;

    public MatterIngredient(MatterStack stack) {
        this.stack = stack;
    }

    public MatterStack getStack() {
        return stack;
    }


    @Override
    public boolean test(MatterStack stack) {
        return this.stack.getMatter() == stack.getMatter() && this.stack.getAmount() <= stack.getAmount();
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

    @Override
    public MapCodec<? extends ExTerraIngredient<MatterStack>> type() {
        return ExTerraCore.CONST_MATTER_SERIALIZER.get();
    }
}
