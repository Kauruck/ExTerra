package com.kauruck.exterra.util;

import net.minecraft.inventory.EquipmentSlotType;

public class Utility {

    public static String convertArmorSlotToString(EquipmentSlotType slot){
        switch (slot){
            case FEET:
                return "_boots";
            case LEGS:
                return "_leggins";
            case CHEST:
                return "_chestplate";
            case HEAD:
                return "_helmet";
            default:
                return "";
        }
    }
}
