package com.kauruck.exterra.modules;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class ExTerraShared {

    public static final BlockBehaviour.Properties DEFAULT_PROPERTIES_STONE = BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.STONE).strength(2.0f);
    public static final Item.Properties DEFAULT_PROPERTIES_ITEM = new Item.Properties().tab(CreativeModeTab.TAB_MISC);

}
