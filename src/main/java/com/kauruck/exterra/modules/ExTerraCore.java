package com.kauruck.exterra.modules;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraftforge.fmllegacy.RegistryObject;

import static com.kauruck.exterra.modules.RegistryManger.BLOCK_REGISTRY;
import static com.kauruck.exterra.modules.RegistryManger.ITEMS_REGISTRY;

public class ExTerraCore {

    //Test Stuff
    public static final RegistryObject<Item> TEST_ITEM = ITEMS_REGISTRY.register("test", () -> new Item(ExTerraShared.DEFAULT_PROPERTIES_ITEM));

    //Compound
    public static final RegistryObject<Item> COMPOUND = ITEMS_REGISTRY.register("compound",() -> new Item(ExTerraShared.DEFAULT_PROPERTIES_ITEM));

    //Compound Brick
    public static final RegistryObject<Item> COMPOUND_BRICK = ITEMS_REGISTRY.register("compound_brick", () -> new Item(ExTerraShared.DEFAULT_PROPERTIES_ITEM));

    public static final RegistryObject<Block> COMPOUND_BRICKS = BLOCK_REGISTRY.register("compound_bricks", () -> new Block(ExTerraShared.DEFAULT_PROPERTIES_STONE));
    public static final RegistryObject<Item> COMPOUND_BRICKS_ITEM = ITEMS_REGISTRY.register("compound_bricks", () -> new BlockItem(COMPOUND_BRICKS.get(), ExTerraShared.DEFAULT_PROPERTIES_ITEM));

    public static final RegistryObject<Block> COMPOUND_BRICKS_STAIR = BLOCK_REGISTRY.register("compound_bricks_stair", () -> new StairBlock(() -> COMPOUND_BRICKS.get().defaultBlockState(), ExTerraShared.DEFAULT_PROPERTIES_STONE));
    public static final RegistryObject<Item> COMPOUND_BRICKS_STAIR_ITEM = ITEMS_REGISTRY.register("compound_bricks_stair", () -> new BlockItem(COMPOUND_BRICKS_STAIR.get(), ExTerraShared.DEFAULT_PROPERTIES_ITEM));

    public static final RegistryObject<Block> COMPOUND_BRICKS_SLAB = BLOCK_REGISTRY.register("compound_bricks_slab", () -> new SlabBlock(ExTerraShared.DEFAULT_PROPERTIES_STONE));
    public static final RegistryObject<Item> COMPOUND_BRICKS_SLAB_ITEM = ITEMS_REGISTRY.register("compound_bricks_slab", () -> new BlockItem(COMPOUND_BRICKS_SLAB.get(), ExTerraShared.DEFAULT_PROPERTIES_ITEM));


    //Compound Framed Glass
    public static final RegistryObject<Block> COMPOUND_FRAMED_GLASS = BLOCK_REGISTRY.register("compound_framed_glass", () -> new GlassBlock(ExTerraShared.DEFAULT_PROPERTIES_GLASS));
    public static final RegistryObject<Item> COMPOUND_FRAMED_GLASS_ITEM = ITEMS_REGISTRY.register("compound_framed_glass", () -> new BlockItem(COMPOUND_FRAMED_GLASS.get(), ExTerraShared.DEFAULT_PROPERTIES_ITEM));
}
