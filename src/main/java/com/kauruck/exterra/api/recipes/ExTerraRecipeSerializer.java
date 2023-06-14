package com.kauruck.exterra.api.recipes;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;

public interface ExTerraRecipeSerializer<T extends ExTerraRecipe<?, ?>> {

    T fromJson(ResourceLocation recipeId, JsonObject json, ICondition.IContext context);

    T fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer);

    void toNetwork(FriendlyByteBuf pBuffer, T pRecipe);

    JsonObject toJson(T recipe);
}
