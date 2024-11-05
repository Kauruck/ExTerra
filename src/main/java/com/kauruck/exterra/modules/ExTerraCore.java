package com.kauruck.exterra.modules;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.geometry.IGeometricSerializer;
import com.kauruck.exterra.api.geometry.StaticGeometricSerializer;
import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.api.recipes.*;
import com.kauruck.exterra.blockentities.MatterEmitterEntity;
import com.kauruck.exterra.blockentities.MatterReceiverEntity;
import com.kauruck.exterra.blockentities.RitualStoneEntity;
import com.kauruck.exterra.blocks.DustBlock;
import com.kauruck.exterra.blocks.RitualStone;
import com.kauruck.exterra.blocks.TestEmitterBlock;
import com.kauruck.exterra.blocks.TestReceiverBlock;
import com.kauruck.exterra.geometry.Shape;
import com.kauruck.exterra.geometry.builtin.IntersectAngle;
import com.kauruck.exterra.geometry.builtin.ParallelLine;
import com.kauruck.exterra.geometry.elmental.AND;
import com.kauruck.exterra.ingredients.MatterIngredient;
import com.kauruck.exterra.items.RitualLensItem;
import com.kauruck.exterra.items.RitualMap;
import com.kauruck.exterra.networking.BlockEntityCodecHolder;
import com.kauruck.exterra.networks.matter.MatterNetwork;
import com.kauruck.exterra.recipes.ConversionRecipe;
import com.kauruck.exterra.util.Colors;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.function.Supplier;

import static com.kauruck.exterra.modules.RegistryManger.*;

public class ExTerraCore {

    //Test Stuff
    public static final DeferredHolder<Item, Item> TEST_ITEM = ITEM_REGISTRY.register("test", () -> new Item(ExTerraShared.DEFAULT_PROPERTIES_ITEM));
    public static final DeferredHolder<Matter, Matter> TEST_MATTER = MATTER_REGISTRY.register("test_matter", () -> new Matter(new Matter.MatterProperties().setEnergy(1).setParticleColor(Colors.RED)));
    public static final DeferredHolder<Matter, Matter> TEST_MATTER_2 = MATTER_REGISTRY.register("test_matter_2", () -> new Matter(new Matter.MatterProperties().setEnergy(1).setParticleColor(Colors.BLUE)));

    //Recipes
    public static final Supplier<ExTerraRecipeManager<MatterStack>> CONVERSION_RECIPE_MANGER = ExTerraReloadableResources.registerRecipeManger(ExTerra.getResource("conversion"), (context) -> new ExTerraRecipeManager<>(context, "conversion" , ExTerra.getResource("conversion")));
    public static final DeferredHolder<MapCodec<? extends ExTerraRecipe<?, ?>>, MapCodec<ConversionRecipe>> CONVERSION_RECIPE_SERIALIZER = RECIPE_SERIALIZER_REGISTRY.register("conversion", () -> ConversionRecipe.CODEC);
    public static final DeferredHolder<ExTerraRecipeType<?>, ExTerraRecipeType<ConversionRecipe>> CONVERSION_RECIPE_TYPE = RECIPE_TYPE_REGISTRY.register("conversion",() -> new ExTerraRecipeType<>(ExTerra.getResource("conversion"), CONVERSION_RECIPE_SERIALIZER.getId()));
    public static final DeferredHolder<MapCodec<? extends ExTerraIngredient<?>>, MapCodec<MatterIngredient>> CONST_MATTER_SERIALIZER = INGREDIENT_SERIALIZER_REGISTRY.register("const_matter", () -> MatterIngredient.CODEC);

    //Ritual Lense
    public static final DeferredHolder<Item, Item> RITUAL_LENS = ITEM_REGISTRY.register("ritual_lens", RitualLensItem::new);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> COMPONENT_CENTER_POS = DATA_COMPONENTS_REGISTRY.register("center_pos", () ->
            new DataComponentType.Builder<BlockPos>().persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC).build());

    //Compound
    public static final DeferredHolder<Item, Item> COMPOUND = ITEM_REGISTRY.register("compound",() -> new Item(ExTerraShared.DEFAULT_PROPERTIES_ITEM));

    //Compound Brick
    public static final DeferredHolder<Item, Item> COMPOUND_BRICK = ITEM_REGISTRY.register("compound_brick", () -> new Item(ExTerraShared.DEFAULT_PROPERTIES_ITEM));

    public static final DeferredHolder<Block, Block> COMPOUND_BRICKS = BLOCK_REGISTRY.register("compound_bricks", () -> new Block(ExTerraShared.DEFAULT_PROPERTIES_STONE));
    public static final DeferredHolder<Item, Item> COMPOUND_BRICKS_ITEM = ITEM_REGISTRY.register("compound_bricks", () -> new BlockItem(COMPOUND_BRICKS.get(), ExTerraShared.DEFAULT_PROPERTIES_ITEM));

    public static final DeferredHolder<Block, Block> COMPOUND_BRICKS_STAIR = BLOCK_REGISTRY.register("compound_bricks_stair", () -> new StairBlock(COMPOUND_BRICKS.get().defaultBlockState(), ExTerraShared.DEFAULT_PROPERTIES_STONE));
    public static final DeferredHolder<Item, Item> COMPOUND_BRICKS_STAIR_ITEM = ITEM_REGISTRY.register("compound_bricks_stair", () -> new BlockItem(COMPOUND_BRICKS_STAIR.get(), ExTerraShared.DEFAULT_PROPERTIES_ITEM));

    public static final DeferredHolder<Block, Block> COMPOUND_BRICKS_SLAB = BLOCK_REGISTRY.register("compound_bricks_slab", () -> new SlabBlock(ExTerraShared.DEFAULT_PROPERTIES_STONE));
    public static final DeferredHolder<Item, Item> COMPOUND_BRICKS_SLAB_ITEM = ITEM_REGISTRY.register("compound_bricks_slab", () -> new BlockItem(COMPOUND_BRICKS_SLAB.get(), ExTerraShared.DEFAULT_PROPERTIES_ITEM));

    //Compound Framed Glass
    public static final DeferredHolder<Block, Block> COMPOUND_FRAMED_GLASS = BLOCK_REGISTRY.register("compound_framed_glass", () -> new TransparentBlock(ExTerraShared.DEFAULT_PROPERTIES_GLASS));
    public static final DeferredHolder<Item, Item> COMPOUND_FRAMED_GLASS_ITEM = ITEM_REGISTRY.register("compound_framed_glass", () -> new BlockItem(COMPOUND_FRAMED_GLASS.get(), ExTerraShared.DEFAULT_PROPERTIES_ITEM));

    // Compound Plate
    public static final DeferredHolder<Item, Item> COMPOUND_PLATE = ITEM_REGISTRY.register("compound_plate", () -> new Item(ExTerraShared.DEFAULT_PROPERTIES_ITEM));

    //Dusts
    public static final DeferredHolder<Block, Block> CALCITE_DUST = BLOCK_REGISTRY.register("calcite_dust", () -> new DustBlock(ExTerraShared.DEFAULT_PROPERTIES_DUST));
    public static final DeferredHolder<Item, Item> CALCITE_DUST_ITEM = ITEM_REGISTRY.register("calcite_dust", () -> new BlockItem(CALCITE_DUST.get(), ExTerraShared.DEFAULT_PROPERTIES_ITEM));

    //Rituals
    public static final DeferredHolder<Block, Block> RITUAL_STONE = BLOCK_REGISTRY.register("ritual_stone", RitualStone::new);
    public static final DeferredHolder<Item, Item> RITUAL_STONE_ITEM = ITEM_REGISTRY.register("ritual_stone", () -> new BlockItem(RITUAL_STONE.get(), ExTerraShared.DEFAULT_PROPERTIES_ITEM));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RitualStoneEntity>> RITUAL_STONE_ENTITY = BLOCK_ENTITY_TYPE_REGISTRY.register("ritual_stone", () -> BlockEntityType.Builder.of(RitualStoneEntity::new, RITUAL_STONE.get()).build(null));

    //-- Emitter
    public static final DeferredHolder<Block, Block> EMITTER_BLOCK = BLOCK_REGISTRY.register("emitter_block", TestEmitterBlock::new);
    public static final DeferredHolder<Item, Item> EMITTER_BLOCK_ITEM = ITEM_REGISTRY.register("emitter_block", () -> new BlockItem(EMITTER_BLOCK.get(), ExTerraShared.DEFAULT_PROPERTIES_ITEM));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MatterEmitterEntity>> EMITTER_BLOCK_ENTITY = BLOCK_ENTITY_TYPE_REGISTRY.register("emitter_block", () -> BlockEntityType.Builder.of(MatterEmitterEntity::new, EMITTER_BLOCK.get()).build(null));

    //-- Receiver
    public static final DeferredHolder<Block, Block> RECEIVER_BLOCK = BLOCK_REGISTRY.register("receiver_block", TestReceiverBlock::new);
    public static final DeferredHolder<Item, Item> RECEIVER_BLOCK_ITEM = ITEM_REGISTRY.register("receiver_block", () -> new BlockItem(RECEIVER_BLOCK.get(), ExTerraShared.DEFAULT_PROPERTIES_ITEM));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MatterReceiverEntity>> RECEIVER_BLOCK_ENTITY = BLOCK_ENTITY_TYPE_REGISTRY.register("receiver_block", () -> BlockEntityType.Builder.of(MatterReceiverEntity::new, RECEIVER_BLOCK.get()).build(null));


    public static final DeferredHolder<Item, RitualMap> RITUAL_MAP = ITEM_REGISTRY.register("ritual_map", RitualMap::new);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<Shape>>> COMPONENT_SHAPES = DATA_COMPONENTS_REGISTRY.register("shape_list", () ->
            new DataComponentType.Builder<List<Shape>>()
                    .persistent(Codec.list(Shape.CODEC))
                    .networkSynchronized(Shape.STREAM_CODEC.apply(ByteBufCodecs.list()))
                    .build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> COMPONENT_CURRENT_SHAPE = DATA_COMPONENTS_REGISTRY.register("current_shape", () ->
            new DataComponentType.Builder<Integer>()
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.INT)
                    .build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> COMPONENT_SELECT_MODE = DATA_COMPONENTS_REGISTRY.register("select_mode", () ->
                    new DataComponentType.Builder<Boolean>()
                    .persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
                    .build()
            );

    // Geometry
    public static final DeferredHolder<IGeometricSerializer, StaticGeometricSerializer<AND>> GEOMETRIC_AND = GEOMETRIC_SERIALIZER_REGISTRY.register("and", () -> new StaticGeometricSerializer<>(AND.CODEC, AND.STREAM_CODEC));
    public static final DeferredHolder<IGeometricSerializer, StaticGeometricSerializer<IntersectAngle>> GEOMETRIC_INTERSECT = GEOMETRIC_SERIALIZER_REGISTRY.register("intersect", () -> new StaticGeometricSerializer<>(IntersectAngle.CODEC, IntersectAngle.STREAM_CODEC));
    public static final DeferredHolder<IGeometricSerializer, StaticGeometricSerializer<ParallelLine>> GEOMETRIC_PARALLEL = GEOMETRIC_SERIALIZER_REGISTRY.register("parallel", () -> new StaticGeometricSerializer<>(ParallelLine.CODEC, ParallelLine.STREAM_CODEC));

   // Property functions
   public static final DeferredHolder<BlockEntityCodecHolder<?>, BlockEntityCodecHolder<List<Shape>>> PROPERTY_SHAPE_LIST = BLOCK_ENTITY_PROPERTY_CODEC_REGISTRY.register("shape_list", () -> new BlockEntityCodecHolder<>(Codec.list(Shape.CODEC)));
   public static final DeferredHolder<BlockEntityCodecHolder<?>, BlockEntityCodecHolder<MatterNetwork>> PROPERTY_MATTER_NETWORK = BLOCK_ENTITY_PROPERTY_CODEC_REGISTRY.register("matter_network", () -> new BlockEntityCodecHolder<>(MatterNetwork.CODEC, MatterNetwork.NETWORK_CODEC));


   // Creative Tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CORE_TAB = CREATIVE_MODE_TAB_REGISTRY.register("core", () -> CreativeModeTab.builder()
           .title(Component.translatable("itemGroup." + ExTerra.MOD_ID + ".core"))
           .icon(() -> new ItemStack(RITUAL_MAP.get()))
           .displayItems((p, o) -> {
               o.accept(RITUAL_MAP.get());
               o.accept(RITUAL_STONE.get());
               o.accept(TEST_ITEM.get());
               o.accept(RITUAL_LENS.get());
               o.accept(COMPOUND.get());
               o.accept(COMPOUND_BRICKS.get());
               o.accept(COMPOUND_BRICKS_SLAB.get());
               o.accept(COMPOUND_BRICKS_STAIR.get());
               o.accept(COMPOUND_FRAMED_GLASS.get());
               o.accept(COMPOUND_PLATE.get());
               o.accept(CALCITE_DUST.get());
               o.accept(EMITTER_BLOCK.get());
               o.accept(RECEIVER_BLOCK.get());
           })
           .build());
}
