package com.kauruck.exterra.API.Items.Armor;

import com.kauruck.exterra.Init.ModTabs;
import com.kauruck.exterra.Item.ArmorItemBase;
import com.kauruck.exterra.Item.GemType;
import com.kauruck.exterra.Util.Utility;
import net.minecraft.inventory.EquipmentSlotType;

public class GemArmor extends ArmorItemBase {

    public GemArmor(GemType materialIn, EquipmentSlotType slot) {
        super(materialIn, slot, materialIn.getRegistryName() + Utility.convertArmorSlotToString(slot), ModTabs.TOOLS);
    }


}
