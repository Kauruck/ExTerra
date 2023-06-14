package com.kauruck.exterra.recipes;

import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.api.recipes.ExTerraIngredient;
import com.kauruck.exterra.api.recipes.ExTerraRecipe;
import com.kauruck.exterra.api.recipes.ExTerraRecipeSerializer;
import com.kauruck.exterra.api.recipes.ExTerraRecipeType;
import com.kauruck.exterra.modules.ExTerraCore;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.List;

public class ConversionRecipe extends ExTerraRecipe<MatterStack, ConversionContainer> {

    private final MatterStack output;
    private final NonNullList<ExTerraIngredient<MatterStack>> ingredients;

    public ConversionRecipe(ResourceLocation id, NonNullList<ExTerraIngredient<MatterStack>> ingredients , MatterStack output) {
        super(id, ExTerraCore.CONVERSION_RECIPE_TYPE.get());
        this.ingredients = ingredients;
        this.output = output;
    }

    @Override
    public NonNullList<MatterStack> getRemainder(ConversionContainer container) {
        return null;
    }

    @Override
    public boolean matches(ConversionContainer container, Level pLevel) {
        return false;
    }

    @Override
    public boolean canBeCraftedInContainer(ConversionContainer container) {
        return false;
    }

    @Override
    public MatterStack assemble(ConversionContainer container) {
        return null;
    }

    @Override
    public MatterStack getResult() {
        return null;
    }

    @Override
    public ItemStack getToastSymbol() {
        return null;
    }

    @Override
    public NonNullList<ExTerraIngredient<MatterStack>> getIngredients() {
        return ingredients;
    }

    @Override
    public String getGroup() {
        return null;
    }

    @Override
    public boolean test(MatterStack stack) {
        return false;
    }
}
