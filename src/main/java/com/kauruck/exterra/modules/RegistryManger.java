package com.kauruck.exterra.modules;

import com.kauruck.exterra.ExTerra;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryManger {

    public static final DeferredRegister<Item> ITEMS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ExTerra.MOD_ID);
    public static final DeferredRegister<Block> BLOCK_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, ExTerra.MOD_ID);

    public static void doRegistry(){
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS_REGISTRY.register(bus);
        ExTerra.LOGGER.debug("Registered {} Items", ITEMS_REGISTRY.getEntries().size());
        BLOCK_REGISTRY.register(bus);
        ExTerra.LOGGER.debug("Registered {} Blocks", ITEMS_REGISTRY.getEntries().size());
    }
}
