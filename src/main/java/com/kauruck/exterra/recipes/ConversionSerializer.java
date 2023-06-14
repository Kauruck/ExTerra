package com.kauruck.exterra.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.api.recipes.ExTerraIngredient;
import com.kauruck.exterra.api.recipes.ExTerraRecipeSerializer;
import com.kauruck.exterra.serializers.ExTerraIngredientsSerializer;
import com.kauruck.exterra.serializers.MatterStackSerializer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;

public class ConversionSerializer implements ExTerraRecipeSerializer<ConversionRecipe> {

    public static final ExTerraRecipeSerializer<ConversionRecipe> INSTANCE = new ConversionSerializer();
    @Override
    public ConversionRecipe fromJson(ResourceLocation recipeId, JsonObject json, ICondition.IContext context) {
        if(!json.has("input") || !json.has("output")){
            throw new JsonParseException("Expected input and output.");
        }

        MatterStack output = MatterStackSerializer.fromJson(json.getAsJsonObject("output"));
        NonNullList<ExTerraIngredient<MatterStack>> ingredients = ExTerraIngredientsSerializer.fromJson(json.getAsJsonArray("input"));

        return new ConversionRecipe(recipeId, ingredients, output);
    }

    @Override
    public ConversionRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
        NonNullList<ExTerraIngredient<MatterStack>> ingredients = ExTerraIngredientsSerializer.readNetwork(pBuffer);
        MatterStack output = MatterStackSerializer.readNetwork(pBuffer);
        return new ConversionRecipe(pRecipeId, ingredients, output);
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, ConversionRecipe recipe) {
        ExTerraIngredientsSerializer.writeNetwork(pBuffer, recipe.getIngredients());
        MatterStackSerializer.writeNetwork(pBuffer, recipe.getResult());
    }

    @Override
    public JsonObject toJson(ConversionRecipe recipe) {
        JsonObject out = new JsonObject();
        out.addProperty("type", recipe.getRecipeType().typeId().toString());
        out.add("input", ExTerraIngredientsSerializer.toJson(recipe.getIngredients()));
        out.add("output", MatterStackSerializer.toJson(recipe.getResult()));
        return out;
    }
}
