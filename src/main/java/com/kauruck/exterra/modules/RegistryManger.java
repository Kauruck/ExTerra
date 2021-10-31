package com.kauruck.exterra.modules;

import com.kauruck.exterra.ExTerra;
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
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ExTerra.MOD_ID);
    public static final DeferredRegister<MenuType<?>> CONTAINER_REGISTRY = DeferredRegister.create(ForgeRegistries.CONTAINERS, ExTerra.MOD_ID);

    public static void doRegistry(){
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEM_REGISTRY.register(bus);
        ExTerra.LOGGER.debug("Registered {} Items", ITEM_REGISTRY.getEntries().size());
        BLOCK_REGISTRY.register(bus);
        ExTerra.LOGGER.debug("Registered {} Blocks", BLOCK_REGISTRY.getEntries().size());
        BLOCK_ENTITY_REGISTRY.register(bus);
        ExTerra.LOGGER.debug("Registered {} Blockentities", BLOCK_ENTITY_REGISTRY.getEntries().size());
        CONTAINER_REGISTRY.register(bus);
        ExTerra.LOGGER.debug("Registered {} Containers", CONTAINER_REGISTRY.getEntries().size());
    }
}
