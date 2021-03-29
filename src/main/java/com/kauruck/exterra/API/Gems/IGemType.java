package com.kauruck.exterra.API.Gems;

import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;

public interface IGemType extends IArmorMaterial, IItemTier {
    com.kauruck.exterra.API.Gems.IGemAction getActionHandler();
}
