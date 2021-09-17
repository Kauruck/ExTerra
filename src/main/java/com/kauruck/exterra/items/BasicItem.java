package com.kauruck.exterra.items;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class BasicItem extends Item {

    public BasicItem(CreativeModeTab tab) {
        super(new Item.Properties().tab(tab));
    }
}
