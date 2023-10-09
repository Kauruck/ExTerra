package com.kauruck.exterra.api.recipes;

import net.minecraft.resources.ResourceLocation;

/**
 * Type of a recipe
 * @param <T> The type of all recipes
 * @since 0.1
 * @author Kauruck
 */
public class ExTerraRecipeType<T extends  ExTerraRecipe<?, ?>> {

    private final ResourceLocation loc;

    public ExTerraRecipeType(ResourceLocation id) {
        this.loc = id;
    }
    /**
     * The id for the type. Used in translation:
     * recipe.(id.namespace).(id.path)
     * @return The Id
     */
    public ResourceLocation typeId(){
        return loc;
    }

    @Override
    public String toString() {
        return loc.toString();
    }
}
