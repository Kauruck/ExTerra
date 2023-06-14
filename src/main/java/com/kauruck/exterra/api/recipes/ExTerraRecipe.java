package com.kauruck.exterra.api.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kauruck.exterra.api.blockentity.IRecipeContainerHolder;
import com.kauruck.exterra.modules.ExTerraRegistries;
import com.kauruck.exterra.modules.ExTerraReloadableResources;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class ExTerraRecipe<T, C extends ExTerraRecipeContainer<T>> implements Predicate<T> {
    private final ResourceLocation id;

    private final ExTerraRecipeType<?> recipeType;
    protected ExTerraRecipe(ResourceLocation id, ExTerraRecipeType<?> recipeType){
        this.id = id;
        this.recipeType = recipeType;
    }

    public abstract NonNullList<T> getRemainder(C container);
    public ExTerraRecipeType<ExTerraRecipe<T, C>> getType(){
        return this.getRecipeType();
    }

    public abstract boolean matches(C container, Level pLevel);

    public abstract boolean canBeCraftedInContainer(C container);

    public boolean canBeCraftedIn(IRecipeContainerHolder<T, C> blockEntity){
        return canBeCraftedInContainer(blockEntity.getContainer(this.recipeType));
    }

    public abstract T assemble(C container);

    public abstract T getResult();

    public abstract ItemStack getToastSymbol();

    public abstract NonNullList<ExTerraIngredient<T>> getIngredients();

    public abstract String getGroup();

    public ResourceLocation getId() {
        return id;
    }

    public ExTerraRecipeType getRecipeType() {
        return recipeType;
    }

    protected ExTerraRecipeSerializer<ExTerraRecipe<T, C>> getRecipeSerializer() {
        return (ExTerraRecipeSerializer<ExTerraRecipe<T, C>>) ExTerraRegistries.RECIPE_SERIALIZER.get().getValue(this.getType().typeId());
    }

}
