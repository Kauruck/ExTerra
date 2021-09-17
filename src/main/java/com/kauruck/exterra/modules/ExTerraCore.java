package com.kauruck.exterra.modules;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fmllegacy.RegistryObject;

import static com.kauruck.exterra.modules.RegistryManger.BLOCK_REGISTRY;
import static com.kauruck.exterra.modules.RegistryManger.ITEMS_REGISTRY;

public class ExTerraCore {

    public static final RegistryObject<Item> TEST_ITEM = ITEMS_REGISTRY.register("test", () -> new Item(ExTerraShared.DEFAULT_PROPERTIES_ITEM));

    public static final RegistryObject<Item> COMPOUND_BRICK = ITEMS_REGISTRY.register("compound_brick", () -> new Item(ExTerraShared.DEFAULT_PROPERTIES_ITEM));

    public static final RegistryObject<Block> COMPOUND_BRICKS = BLOCK_REGISTRY.register("compound_bricks", () -> new Block(ExTerraShared.DEFAULT_PROPERTIES_STONE));
    public static final RegistryObject<Item> COMPOUND_BRICKS_ITEM = ITEMS_REGISTRY.register("compound_bricks", () -> new BlockItem(COMPOUND_BRICKS.get(), ExTerraShared.DEFAULT_PROPERTIES_ITEM));
}
