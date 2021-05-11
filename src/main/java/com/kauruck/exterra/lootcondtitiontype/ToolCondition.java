package com.kauruck.exterra.lootcondtitiontype;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.kauruck.exterra.API.gem.Gem;
import com.kauruck.exterra.API.item.tool.BaseGemTool;
import com.kauruck.exterra.ExTerraRegistries;
import com.kauruck.exterra.modules.ExTerraTools;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;

/**
 * The Condition for filtering ExTerra tools.
 * Use "condition" : "exterra:match_gem" with "gem": [the gem]
 *
 * @author Kauruck
 */
public class ToolCondition implements ILootCondition {

    private final Gem filter;

    public ToolCondition(Gem filter){
        this.filter = filter;
    }

    @Override
    public LootConditionType func_230419_b_() {
        return ExTerraTools.TOOL_CONDITION;
    }

    @Override
    public boolean test(LootContext context) {
        if(filter == null)
            return false;
        if(context.has(LootParameters.TOOL)){
            ItemStack tool = context.get(LootParameters.TOOL);
            if(tool == null)
                return false;
            if(tool.getItem() instanceof BaseGemTool){
                return ((BaseGemTool)tool.getItem()).getGemType(tool).equals(filter);
            }
        }

        return false;
    }

    public static class Serializer implements ILootSerializer<ToolCondition> {


        @Override
        public void serialize(JsonObject json, ToolCondition toJson, JsonSerializationContext context) {
            if(toJson.filter != null)
            json.addProperty("gem", toJson.filter.getRegistryName().toString());
        }

        @Override
        public ToolCondition deserialize(JsonObject json, JsonDeserializationContext context) {
            if(json.has("gem")){
                ResourceLocation gem = new ResourceLocation(json.get("gem").getAsString());

                return new ToolCondition(ExTerraRegistries.GEM_REGISTRY.get().getValue(gem));
            }
            return new ToolCondition(null);
        }
    }
}
