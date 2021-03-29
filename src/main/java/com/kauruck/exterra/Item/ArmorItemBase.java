package com.kauruck.exterra.Item;

import com.kauruck.exterra.Util.RegistryHandler;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ArmorItemBase extends ArmorItem {

    public ArmorItemBase(IArmorMaterial materialIn, EquipmentSlotType slot, String name, ItemGroup group) {
        super(materialIn, slot, new Item.Properties().group(group));
        this.setRegistryName(name);
        RegistryHandler.ITEMS.add(this);
    }
}
