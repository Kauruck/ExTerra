package com.kauruck.exterra.modules;

import com.kauruck.exterra.blocks.controllers.Generator;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fmllegacy.RegistryObject;

import static com.kauruck.exterra.modules.RegistryManger.ITEMS_REGISTRY;
import static com.kauruck.exterra.modules.RegistryManger.BLOCK_REGISTRY;

public class ExTerraPower {

    public static final RegistryObject<Block> GENERATOR = BLOCK_REGISTRY.register("generator", Generator::new);
    public static final RegistryObject<BlockItem> GENERATOR_ITEM = ITEMS_REGISTRY.register("generator", () -> new BlockItem(GENERATOR.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
}
