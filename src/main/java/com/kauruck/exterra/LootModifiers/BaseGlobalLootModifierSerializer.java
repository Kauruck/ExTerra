package com.kauruck.exterra.LootModifiers;

import com.google.gson.JsonObject;
import com.kauruck.exterra.Util.RegistryHandler;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

public abstract class BaseGlobalLootModifierSerializer<T extends LootModifier> extends GlobalLootModifierSerializer<T> {

    public BaseGlobalLootModifierSerializer(String registryName){
        this.setRegistryName(registryName);
        RegistryHandler.GLMS.add(this);
    }
}
