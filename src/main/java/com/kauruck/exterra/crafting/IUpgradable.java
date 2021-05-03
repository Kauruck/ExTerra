package com.kauruck.exterra.crafting;

import com.kauruck.exterra.API.gem.Gem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IUpgradable<T extends Item> {

    ItemStack forTool(ItemStack base, Gem gem);
}
