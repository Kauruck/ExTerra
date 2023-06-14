package com.kauruck.exterra.ingredients;


import com.google.gson.JsonObject;
import com.kauruck.exterra.api.recipes.ExTerraIngredient;
import com.kauruck.exterra.api.recipes.ExTerraIngredientSerializer;
import com.kauruck.exterra.serializers.MatterStackSerializer;
import net.minecraft.network.FriendlyByteBuf;

public class MatterIngredientSerializer implements ExTerraIngredientSerializer<MatterIngredient> {
    @Override
    public JsonObject toJson(MatterIngredient object) {
        return MatterStackSerializer.toJson(object.getStack());
    }

    @Override
    public MatterIngredient fromJson(JsonObject json) {
        return new MatterIngredient(MatterStackSerializer.fromJson(json));
    }

    @Override
    public MatterIngredient readNetwork(FriendlyByteBuf buffer) {
        return new MatterIngredient(MatterStackSerializer.readNetwork(buffer));
    }

    @Override
    public void writeNetwork(FriendlyByteBuf buffer, MatterIngredient object) {
        MatterStackSerializer.writeNetwork(buffer, object.getStack());
    }
}
