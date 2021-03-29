package com.kauruck.exterra.API.Items.Tools;

import com.kauruck.exterra.Init.ModTabs;
import com.kauruck.exterra.Item.GemType;
import com.kauruck.exterra.Util.RegistryHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ShovelItem;

public class GemShovel extends ShovelItem {
    public GemShovel(GemType tier, float attackDamage, float attackSpeed) {
        super(tier, (attackDamage - ((float)tier.getAttackDamage() + 1f)), attackSpeed - 4, new Item.Properties().group(ModTabs.TOOLS));
        RegistryHandler.ITEMS.add(this);
        this.setRegistryName(tier.getRegistryName() + "_shovel");
    }
}
