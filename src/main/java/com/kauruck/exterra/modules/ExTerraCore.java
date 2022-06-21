package com.kauruck.exterra.modules;

import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.blockentities.RitualStoneEntity;
import com.kauruck.exterra.blocks.DustBlock;
import com.kauruck.exterra.blocks.RitualStone;
import com.kauruck.exterra.items.RitualMap;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fmllegacy.RegistryObject;

import java.awt.*;

import static com.kauruck.exterra.modules.RegistryManger.*;

public class ExTerraCore {

    //Test Stuff
    public static final RegistryObject<Item> TEST_ITEM = ITEM_REGISTRY.register("test", () -> new Item(ExTerraShared.DEFAULT_PROPERTIES_ITEM));
    public static final RegistryObject<Matter> TEST_MATTER = MATTER_REGISTRY.register("test_matter", () -> new Matter(new Matter.MatterProperties().setEnergy(1).setParticleColor(Color.RED)));

    //Compound
    public static final RegistryObject<Item> COMPOUND = ITEM_REGISTRY.register("compound",() -> new Item(ExTerraShared.DEFAULT_PROPERTIES_ITEM));

    //Compound Brick
    public static final RegistryObject<Item> COMPOUND_BRICK = ITEM_REGISTRY.register("compound_brick", () -> new Item(ExTerraShared.DEFAULT_PROPERTIES_ITEM));

    public static final RegistryObject<Block> COMPOUND_BRICKS = BLOCK_REGISTRY.register("compound_bricks", () -> new Block(ExTerraShared.DEFAULT_PROPERTIES_STONE));
    public static final RegistryObject<Item> COMPOUND_BRICKS_ITEM = ITEM_REGISTRY.register("compound_bricks", () -> new BlockItem(COMPOUND_BRICKS.get(), ExTerraShared.DEFAULT_PROPERTIES_ITEM));

    public static final RegistryObject<Block> COMPOUND_BRICKS_STAIR = BLOCK_REGISTRY.register("compound_bricks_stair", () -> new StairBlock(() -> COMPOUND_BRICKS.get().defaultBlockState(), ExTerraShared.DEFAULT_PROPERTIES_STONE));
    public static final RegistryObject<Item> COMPOUND_BRICKS_STAIR_ITEM = ITEM_REGISTRY.register("compound_bricks_stair", () -> new BlockItem(COMPOUND_BRICKS_STAIR.get(), ExTerraShared.DEFAULT_PROPERTIES_ITEM));

    public static final RegistryObject<Block> COMPOUND_BRICKS_SLAB = BLOCK_REGISTRY.register("compound_bricks_slab", () -> new SlabBlock(ExTerraShared.DEFAULT_PROPERTIES_STONE));
    public static final RegistryObject<Item> COMPOUND_BRICKS_SLAB_ITEM = ITEM_REGISTRY.register("compound_bricks_slab", () -> new BlockItem(COMPOUND_BRICKS_SLAB.get(), ExTerraShared.DEFAULT_PROPERTIES_ITEM));

    //Compound Framed Glass
    public static final RegistryObject<Block> COMPOUND_FRAMED_GLASS = BLOCK_REGISTRY.register("compound_framed_glass", () -> new GlassBlock(ExTerraShared.DEFAULT_PROPERTIES_GLASS));
    public static final RegistryObject<Item> COMPOUND_FRAMED_GLASS_ITEM = ITEM_REGISTRY.register("compound_framed_glass", () -> new BlockItem(COMPOUND_FRAMED_GLASS.get(), ExTerraShared.DEFAULT_PROPERTIES_ITEM));

    //Dusts
    public static final RegistryObject<Block> CALCITE_DUST = BLOCK_REGISTRY.register("calcite_dust", () -> new DustBlock(ExTerraShared.DEFAULT_PROPERTIES_DUST));
    public static final RegistryObject<Item> CALCITE_DUST_ITEM = ITEM_REGISTRY.register("calcite_dust", () -> new BlockItem(CALCITE_DUST.get(), ExTerraShared.DEFAULT_PROPERTIES_ITEM));

    //Rituals
    public static final RegistryObject<Block> RITUAL_STONE = BLOCK_REGISTRY.register("ritual_stone", RitualStone::new);
    public static final RegistryObject<Item> RITUAL_STONE_ITEM = ITEM_REGISTRY.register("ritual_stone", () -> new BlockItem(RITUAL_STONE.get(), ExTerraShared.DEFAULT_PROPERTIES_ITEM));
    public static final RegistryObject<BlockEntityType<RitualStoneEntity>> RITUAL_STONE_ENTITY = BLOCK_ENTITY_REGISTRY.register("ritual_stone", () -> BlockEntityType.Builder.of(RitualStoneEntity::new, RITUAL_STONE.get()).build(null));

    public static final RegistryObject<Item> RITUAL_MAP = ITEM_REGISTRY.register("ritual_map", RitualMap::new);

}
