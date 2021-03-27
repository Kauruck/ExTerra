package com.kauruck.exterra.Init;

import com.kauruck.exterra.Item.GemItem;
import com.kauruck.exterra.Item.GemType;
import com.kauruck.exterra.Item.Armor.GemArmor;
import com.kauruck.exterra.Item.Tools.*;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;

public class ModItems {

        public static final Item[] GEM_AXES = new Item[GemType.values().length];
        public static final Item[] GEM_SWORDS = new Item[GemType.values().length];
        public static final Item[] GEM_PICKAXES = new Item[GemType.values().length];
        public static final Item[] GEM_HOES = new Item[GemType.values().length];
        public static final Item[] GEM_SHOVELS = new Item[GemType.values().length];
        public static final Item[] GEM_HELMETS = new Item[GemType.values().length];
        public static final Item[] GEM_LEGGINS = new Item[GemType.values().length];
        public static final Item[] GEM_BOOTS = new Item[GemType.values().length];
        public static final Item[] GEM_CHESTPLATES = new Item[GemType.values().length];
        public static final Item[] GEMS = new Item[GemType.values().length];



        public ModItems(){
                for(int i = 0; i < GemType.values().length; i++){
                        GEM_AXES[i] = new GemAxe(GemType.values()[i], 9, 1.0f);
                        GEM_SWORDS[i] = new GemSword(GemType.values()[i], 9, 1.0f);
                        GEM_HOES[i] = new GemHoe(GemType.values()[i], 9, -3);
                        GEM_SHOVELS[i] = new GemShovel(GemType.values()[i], 9, 1.0f);
                        GEM_PICKAXES[i] = new GemPickaxe(GemType.values()[i], 9, 1.0f);
                        GEM_CHESTPLATES[i] = new GemArmor(GemType.values()[i], EquipmentSlotType.CHEST);
                        GEM_LEGGINS[i] = new GemArmor(GemType.values()[i], EquipmentSlotType.LEGS);
                        GEM_HELMETS[i] = new GemArmor(GemType.values()[i], EquipmentSlotType.HEAD);
                        GEM_BOOTS[i] = new GemArmor(GemType.values()[i], EquipmentSlotType.FEET);
                        GEMS[i] = new GemItem(GemType.values()[i]);
                }
        }

}
