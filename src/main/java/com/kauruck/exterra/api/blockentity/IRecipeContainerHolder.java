package com.kauruck.exterra.api.blockentity;

import com.kauruck.exterra.api.recipes.ExTerraRecipe;
import com.kauruck.exterra.api.recipes.ExTerraRecipeContainer;
import com.kauruck.exterra.api.recipes.ExTerraRecipeType;

public interface IRecipeContainerHolder<T,C extends ExTerraRecipeContainer<T>> {
    /**
     * Gets the RecipeContainer of the BlockEntity for recipe Type
     * @return The RecipeContainer
     */
    C getContainer(ExTerraRecipeType recipeType);
}
