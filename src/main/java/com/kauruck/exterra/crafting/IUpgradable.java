package com.kauruck.exterra.crafting;

import com.kauruck.exterra.API.gem.Gem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Mark a Item as Upgradeable.
 *
 * @author Kauruck
 */
public interface IUpgradable{

    /**
     * Get the upgraded from the base for the gem
     * @param base The base item
     * @param gem The gem to apply
     * @return The upgraded Item
     */
    ItemStack forTool(ItemStack base, Gem gem);
}
