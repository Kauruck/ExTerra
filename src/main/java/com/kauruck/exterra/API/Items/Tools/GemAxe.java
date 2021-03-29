package com.kauruck.exterra.API.Items.Tools;

import com.kauruck.exterra.Init.ModTabs;
import com.kauruck.exterra.Item.GemType;
import com.kauruck.exterra.Util.RegistryHandler;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;

public class GemAxe extends AxeItem {
    public GemAxe(GemType tier, float attackDamage, float attackSpeed) {
        super(tier, (int) (attackDamage - (tier.getAttackDamage() + 1)), attackSpeed - 4, new Item.Properties().group(ModTabs.TOOLS));
        RegistryHandler.ITEMS.add(this);
        this.setRegistryName(tier.getRegistryName() + "_axe");
    }
}
