package com.kauruck.exterra.lootcondtitiontype;

import net.minecraft.loot.LootConditionType;

public class ExTerraLootConditionType {

    public static final LootConditionType TOOL_CONDITION = new LootConditionType(new ToolCondition.Serializer());
}
