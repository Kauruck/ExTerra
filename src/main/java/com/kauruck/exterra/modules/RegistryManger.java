package com.kauruck.exterra.modules;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.geometry.IGeometricSerializer;
import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.api.recipes.*;
import com.kauruck.exterra.networking.BlockEntityCodecHolder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegistryManger {

    public static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(BuiltInRegistries.ITEM, ExTerra.MOD_ID);
    public static final DeferredRegister<Block> BLOCK_REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK, ExTerra.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE_REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ExTerra.MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPE_REGISTRY = DeferredRegister.create(BuiltInRegistries.MENU, ExTerra.MOD_ID);
    public static final DeferredRegister<Matter> MATTER_REGISTRY = DeferredRegister.create(ExTerraRegistries.MATTER, ExTerra.MOD_ID);
    public static final DeferredRegister<ExTerraRecipeType<?>> RECIPE_TYPE_REGISTRY = DeferredRegister.create(ExTerraRegistries.RECIPE_TYPE, ExTerra.MOD_ID);
    public static final DeferredRegister<MapCodec<? extends ExTerraRecipe<?, ?>>> RECIPE_SERIALIZER_REGISTRY = DeferredRegister.create(ExTerraRegistries.RECIPE_SERIALIZER, ExTerra.MOD_ID);;
    public static final DeferredRegister<MapCodec<? extends ExTerraIngredient<?>>> INGREDIENT_SERIALIZER_REGISTRY = DeferredRegister.create(ExTerraRegistries.INGREDIENT_SERIALIZER, ExTerra.MOD_ID);
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS_REGISTRY = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, ExTerra.MOD_ID);
    public static final DeferredRegister<IGeometricSerializer> GEOMETRIC_SERIALIZER_REGISTRY = DeferredRegister.create(ExTerraRegistries.GEOMETRIC_SERIALIZER, ExTerra.MOD_ID);
    public static final DeferredRegister<BlockEntityCodecHolder<?>> BLOCK_ENTITY_PROPERTY_CODEC_REGISTRY = DeferredRegister.create(ExTerraRegistries.BLOCK_ENTITY_PROPERTY_CODEC, ExTerra.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB_REGISTRY = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, ExTerra.MOD_ID);

    public static void doRegistry(IEventBus bus){
        ITEM_REGISTRY.register(bus);
        ExTerra.LOGGER.info("Registered {} Items", ITEM_REGISTRY.getEntries().size());
        BLOCK_REGISTRY.register(bus);
        ExTerra.LOGGER.info("Registered {} Blocks", BLOCK_REGISTRY.getEntries().size());
        BLOCK_ENTITY_TYPE_REGISTRY.register(bus);
        ExTerra.LOGGER.info("Registered {} Blockentities", BLOCK_ENTITY_TYPE_REGISTRY.getEntries().size());
        MENU_TYPE_REGISTRY.register(bus);
        ExTerra.LOGGER.info("Registered {} Containers", MENU_TYPE_REGISTRY.getEntries().size());
        MATTER_REGISTRY.register(bus);
        ExTerra.LOGGER.info("Registered {} Matters", MATTER_REGISTRY.getEntries().size());
        RECIPE_TYPE_REGISTRY.register(bus);
        ExTerra.LOGGER.info("Registered {} recipe types", RECIPE_TYPE_REGISTRY.getEntries().size());
        RECIPE_SERIALIZER_REGISTRY.register(bus);
        ExTerra.LOGGER.info("Registered {} recipe serializer", RECIPE_SERIALIZER_REGISTRY.getEntries().size());
        INGREDIENT_SERIALIZER_REGISTRY.register(bus);
        ExTerra.LOGGER.info("Registered {} ingredient serializer", INGREDIENT_SERIALIZER_REGISTRY.getEntries().size());
        DATA_COMPONENTS_REGISTRY.register(bus);
        ExTerra.LOGGER.info("Registered {} Data Components", DATA_COMPONENTS_REGISTRY.getEntries().size());
        GEOMETRIC_SERIALIZER_REGISTRY.register(bus);
        ExTerra.LOGGER.info("Registered {} geometric serializer", GEOMETRIC_SERIALIZER_REGISTRY.getEntries().size());
        BLOCK_ENTITY_PROPERTY_CODEC_REGISTRY.register(bus);
        ExTerra.LOGGER.info("Registered {} block entity property codecs", BLOCK_ENTITY_PROPERTY_CODEC_REGISTRY.getEntries().size());
        CREATIVE_MODE_TAB_REGISTRY.register(bus);
        ExTerra.LOGGER.info("Registered {} creative tabs", CREATIVE_MODE_TAB_REGISTRY.getEntries().size());
    }
}
