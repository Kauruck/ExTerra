package com.kauruck.exterra.Init;

import com.kauruck.exterra.LootModifiers.Autosmelt;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;

public class ModGlobalLootModifiers {
    public static final GlobalLootModifierSerializer<Autosmelt> AUTOSMELT = new Autosmelt.Serializer();
}
