package com.kauruck.exterra.datapacks;

import com.google.gson.*;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.lang.reflect.Type;

//Copied straight form TinkersConstruct https://github.com/SlimeKnights/TinkersConstruct/blob/a4161f0d2dd9689801675f6ff5b97c3dd13e3b8c/src/main/java/slimeknights/tconstruct/library/materials/MaterialManager.java#L206
class ConditionSerializer implements JsonDeserializer<ICondition>, JsonSerializer<ICondition> {
    @Override
    public ICondition deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        return CraftingHelper.getCondition(JSONUtils.getJsonObject(json, "condition"));
    }

    @Override
    public JsonElement serialize(ICondition condition, Type type, JsonSerializationContext context) {
        return CraftingHelper.serialize(condition);
    }
}
