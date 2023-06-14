package com.kauruck.exterra.modules;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.api.recipes.ExTerraIngredientSerializer;
import com.kauruck.exterra.api.recipes.ExTerraRecipeSerializer;
import com.kauruck.exterra.api.recipes.ExTerraRecipeType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryManger {

    public static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ExTerra.MOD_ID);
    public static final DeferredRegister<Block> BLOCK_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, ExTerra.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ExTerra.MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPE_REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ExTerra.MOD_ID);
    public static final DeferredRegister<Matter> MATTER_REGISTRY = DeferredRegister.create(ExTerra.getResource("matter"), ExTerra.MOD_ID);
    public static final DeferredRegister<ExTerraRecipeType<?>> RECIPE_TYPE_REGISTRY = DeferredRegister.create(ExTerra.getResource("recipe_type"), ExTerra.MOD_ID);
    public static final DeferredRegister<ExTerraRecipeSerializer<?>> RECIPE_SERIALIZER_REGISTRY = DeferredRegister.create(ExTerra.getResource("recipe_serializer"), ExTerra.MOD_ID);;
    public static final DeferredRegister<ExTerraIngredientSerializer<?>> INGREDIENT_SERIALIZER_REGISTRY = DeferredRegister.create(ExTerra.getResource("ingredient_serializer"), ExTerra.MOD_ID);

    public static void doRegistry(){
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
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
        ExTerra.LOGGER.info("Registered {} RECIPE_TYPE", RECIPE_TYPE_REGISTRY.getEntries().size());
        RECIPE_SERIALIZER_REGISTRY.register(bus);
        ExTerra.LOGGER.info("Registered {} RECIPE_SERIALIZER", RECIPE_SERIALIZER_REGISTRY.getEntries().size());
        INGREDIENT_SERIALIZER_REGISTRY.register(bus);
        ExTerra.LOGGER.info("Registered {} INGREDIENT_SERIALIZER_REGISTRY", INGREDIENT_SERIALIZER_REGISTRY.getEntries().size());
    }
}
