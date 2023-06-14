package com.kauruck.exterra.api.recipes;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;

public interface ExTerraIngredientSerializer<R extends ExTerraIngredient<?>> {

    JsonObject toJson(R object);
    R fromJson(JsonObject json);

    void writeNetwork(FriendlyByteBuf buffer, R object);
    R readNetwork(FriendlyByteBuf buffer);
    
}
