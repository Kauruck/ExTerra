package com.kauruck.exterra.Item.Tools;


import com.kauruck.exterra.Init.ModTabs;
import com.kauruck.exterra.Item.GemType;
import com.kauruck.exterra.Util.RegistryHandler;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SwordItem;

public class GemSword extends SwordItem {
    public GemSword(GemType tier, float attackDamage, float attackSpeed) {
        super(tier, (int) (attackDamage - (tier.getAttackDamage() + 1)), attackSpeed - 4, new Item.Properties().group(ModTabs.TOOLS));
        RegistryHandler.ITEMS.add(this);
        this.setRegistryName(tier.getRegistryName() + "_sword");
    }
}
