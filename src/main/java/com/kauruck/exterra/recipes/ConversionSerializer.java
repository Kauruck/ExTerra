package com.kauruck.exterra.recipes;

import com.google.gson.*;
import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.api.recipes.ExTerraIngredient;
import com.kauruck.exterra.api.recipes.ExTerraRecipeSerializer;
import com.kauruck.exterra.data.ShapeData;
import com.kauruck.exterra.geometry.Shape;
import com.kauruck.exterra.modules.ExTerraRegistries;
import com.kauruck.exterra.modules.ExTerraReloadableResources;
import com.kauruck.exterra.serializers.ExTerraIngredientsSerializer;
import com.kauruck.exterra.serializers.MatterStackSerializer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.lang.reflect.Type;

public class ConversionSerializer implements ExTerraRecipeSerializer<ConversionRecipe> {

    public static final ExTerraRecipeSerializer<ConversionRecipe> INSTANCE = new ConversionSerializer();
    private static final ShapeData.ShapeDataSerializer SHAPE_DATA_SERIALIZER = new ShapeData.ShapeDataSerializer();
    @Override
    public ConversionRecipe fromJson(ResourceLocation recipeId, JsonObject json, ICondition.IContext context) {
        if(!json.has("input") || !json.has("output")){
            throw new JsonParseException("Expected input and output.");
        }


        MatterStack output = MatterStackSerializer.fromJson(json.getAsJsonObject("output"));
        NonNullList<ExTerraIngredient<MatterStack>> ingredients = ExTerraIngredientsSerializer.fromJson(json.getAsJsonArray("input"));

        NonNullList<ResourceLocation> shapes = NonNullList.create();
        if(json.has("shapes") && json.get("shapes").isJsonArray()){
            JsonArray jsonShapes = json.getAsJsonArray("shapes");
            for(JsonElement current : jsonShapes){
                if(!current.isJsonPrimitive()){
                    throw new JsonParseException("Invalid Shape " + jsonShapes+ ". Expcect a resource location of a shape as a string.");
                }
                String resourceString = current.getAsString();
                ResourceLocation shapeLocation = new ResourceLocation(resourceString);
                // We don't load the shapes now, because they are currently not loaded
                shapes.add(shapeLocation);
            }
        }

        return new ConversionRecipe(recipeId, ingredients, shapes, output);
    }

    @Override
    public ConversionRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
        NonNullList<ExTerraIngredient<MatterStack>> ingredients = ExTerraIngredientsSerializer.readNetwork(pBuffer);
        MatterStack output = MatterStackSerializer.readNetwork(pBuffer);
        int size = pBuffer.readInt();
        NonNullList<ShapeData> shapes = NonNullList.create();
        for(int i = 0; i < size; i++){
            ResourceLocation loc = pBuffer.readResourceLocation();
            shapes.add(ExTerraReloadableResources.INSTANCE.getShape(loc));
        }
        return new ConversionRecipe(pRecipeId, ingredients, shapes, output);
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, ConversionRecipe recipe) {
        ExTerraIngredientsSerializer.writeNetwork(pBuffer, recipe.getIngredients());
        MatterStackSerializer.writeNetwork(pBuffer, recipe.getResult());
        NonNullList<ShapeData> shapeData = recipe.getShapes();
        pBuffer.writeInt(shapeData.size());
        for(int i = 0; i < shapeData.size(); i++){
            pBuffer.writeResourceLocation(shapeData.get(i).getName());
        }
    }

    @Override
    public JsonObject toJson(ConversionRecipe recipe) {
        JsonObject out = new JsonObject();
        out.addProperty("type", recipe.getRecipeType().typeId().toString());
        out.add("input", ExTerraIngredientsSerializer.toJson(recipe.getIngredients()));
        out.add("output", MatterStackSerializer.toJson(recipe.getResult()));
        JsonArray shapeData = new JsonArray();
        for(ShapeData currentShape : recipe.getShapes()){
            // I don't know what to put under type and context. So here are some null values
            shapeData.add(currentShape.getName().toString());
        }
        out.add("shapes", shapeData);
        return out;
    }
}
