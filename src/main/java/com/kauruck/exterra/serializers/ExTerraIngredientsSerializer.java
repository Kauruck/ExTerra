package com.kauruck.exterra.serializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.kauruck.exterra.api.recipes.ExTerraIngredient;
import com.kauruck.exterra.api.recipes.ExTerraIngredientSerializer;
import com.kauruck.exterra.modules.ExTerraRegistries;
import com.kauruck.exterra.modules.ExTerraReloadableResources;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ExTerraIngredientsSerializer {

    public static <T> JsonArray toJson(NonNullList<ExTerraIngredient<T>> ingredients){
        JsonArray ingredientData = new JsonArray();
        for(ExTerraIngredient<T> ingredient: ingredients){
            com.kauruck.exterra.api.recipes.ExTerraIngredientSerializer<ExTerraIngredient<T>> serializer = (ExTerraIngredientSerializer<ExTerraIngredient<T>>) ExTerraRegistries.INGREDIENT_SERIALIZER.get().getValue(ingredient.getSerializerLocation());
            if(serializer == null){
                throw new JsonParseException("No Serializer found with location" + ingredient.getSerializerLocation());
            }
            JsonObject serializedData = serializer.toJson(ingredient);
            serializedData.addProperty("type", ingredient.getSerializerLocation().toString());
            ingredientData.add(serializedData);
        }
        return ingredientData;
    }

    public static <T> NonNullList<ExTerraIngredient<T>> fromJson(JsonArray array){
        NonNullList<ExTerraIngredient<T>> out = NonNullList.create();
        for(JsonElement currentElement : array){
            if(!currentElement.isJsonObject()){
                throw new JsonParseException("Non json object given in ingredient list");
            }
            JsonObject current = (JsonObject) currentElement;
            if(!current.has("type")){
                throw new JsonParseException("No type for ingredient given");
            }
            ResourceLocation serializerLoc = new ResourceLocation(current.get("type").getAsString());
            ExTerraIngredientSerializer<ExTerraIngredient<T>> serializer = (ExTerraIngredientSerializer<ExTerraIngredient<T>>) ExTerraRegistries.INGREDIENT_SERIALIZER.get().getValue(serializerLoc);
            if(serializer == null){
                throw new JsonParseException("No Serializer found with location" + serializerLoc);
            }
            ExTerraIngredient<T> ingredient = serializer.fromJson(current);
            if(ingredient == null){
                throw new JsonParseException("Got null for ingredient.");
            }
            out.add(ingredient);
        }
        return out;
    }

    public static <T> void writeNetwork(FriendlyByteBuf buf, NonNullList<ExTerraIngredient<T>> ingredients){
        buf.writeInt(ingredients.size());
        for(ExTerraIngredient<T> current : ingredients){
            ExTerraIngredientSerializer<ExTerraIngredient<T>> serializer = (ExTerraIngredientSerializer<ExTerraIngredient<T>>) ExTerraRegistries.INGREDIENT_SERIALIZER.get().getValue(current.getSerializerLocation());
            if(serializer == null)
                continue;
            buf.writeResourceLocation(current.getSerializerLocation());
            serializer.writeNetwork(buf, current);
        }
    }

    public static <T> NonNullList<ExTerraIngredient<T>> readNetwork(FriendlyByteBuf buf){
        int size = buf.readInt();
        NonNullList<ExTerraIngredient<T>> out = NonNullList.create();
        for(int index = 0; index < size; index++){
            ResourceLocation serializerLoc = buf.readResourceLocation();
            ExTerraIngredientSerializer<ExTerraIngredient<T>> serializer = (ExTerraIngredientSerializer<ExTerraIngredient<T>>) ExTerraRegistries.INGREDIENT_SERIALIZER.get().getValue(serializerLoc);
            // serializer cannot be null hear, because should only be send by writeNetwork
            // We are gone test any way
            if(serializer == null){
                ExTerraReloadableResources.LOGGER.error("Got send ingredient without a valid serializer. Things will break now (I think). Have fun!");
            }
            ExTerraIngredient<T> ingredient = serializer.readNetwork(buf);
            if(ingredient == null){
                ExTerraReloadableResources.LOGGER.warn("Got null ingredient over the network. Ignoring it.");
                continue;
            }
            out.add(ingredient);
        }
        return out;
    }
}
