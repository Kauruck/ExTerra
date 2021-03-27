package com.kauruck.exterra.Item;

import com.kauruck.exterra.Util.RegistryHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ItemBase extends Item {


    public ItemBase(String name, ItemGroup itemGroup) {
        super(new Item.Properties().group(itemGroup));
        RegistryHandler.ITEMS.add(this);
        this.setRegistryName(name);
    }
}
