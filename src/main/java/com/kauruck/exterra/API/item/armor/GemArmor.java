package com.kauruck.exterra.API.item.armor;

import com.kauruck.exterra.item.ArmorItemBase;
import com.kauruck.exterra.API.gem.Gem;
import com.kauruck.exterra.modules.ExTerraTools;
import com.kauruck.exterra.util.Utility;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;

public class GemArmor extends ArmorItemBase {

    public GemArmor(Gem materialIn, EquipmentSlotType slot) {
        super(ArmorMaterial.LEATHER, slot, materialIn.getRegistryName() + Utility.convertArmorSlotToString(slot), ExTerraTools.TOOLS_TAB);
    }


}
