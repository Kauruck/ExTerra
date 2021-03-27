package com.kauruck.exterra.Util;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.Init.*;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = ExTerra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryHandler {

    public static final ArrayList<Item> ITEMS = new ArrayList<>();
    public static final ArrayList<Block> BLOCKS = new ArrayList<>();
    public static final ArrayList<TileEntityType<?>> TILE_ENTITY_TYPES = new ArrayList<>();
    public static final ArrayList<ContainerType<?>> CONTAINER_TYPES = new ArrayList<>();

    @SubscribeEvent
    public static  void registerBlocks(RegistryEvent.Register<Block> event) {
        new ModTabs();
        new ModBlocks();
        for(Block b : BLOCKS)
        {
            event.getRegistry().register(b);
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {

        new ModItems();
        for(Item i : ITEMS)
        {
            event.getRegistry().register(i);
        }
    }

    @SubscribeEvent
    public static void registerTileEntityTypes(RegistryEvent.Register<TileEntityType<?>> event) {
        new ModTileEntities();
        for(TileEntityType<?> t : TILE_ENTITY_TYPES){
            event.getRegistry().register(t);
        }
    }

    @SubscribeEvent
    public static void registerContainerTypes(RegistryEvent.Register<ContainerType<?>> event) {
        new ModContainers();
        for(ContainerType<?> t : CONTAINER_TYPES){
            event.getRegistry().register(t);
        }
    }


}
