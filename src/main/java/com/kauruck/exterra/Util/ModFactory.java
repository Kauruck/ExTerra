package com.kauruck.exterra.Util;

import com.kauruck.exterra.GUIs.Machines.BaseContainer;
import com.kauruck.exterra.GUIs.Machines.GemWorkbenchContainer;
import com.kauruck.exterra.TileEntities.GemWorkbenchTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;

import java.util.function.Supplier;

public class ModFactory {

    public static <T extends Container> ContainerType<T> createContainerType(String registryName, IModContainerFactory supplier, ScreenManager.IScreenFactory<T, ContainerScreen<T>> screenSupplier){
        ContainerType<T> tmp = (ContainerType<T>) IForgeContainerType.create((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            World world = inv.player.getEntityWorld();
            return supplier.create(windowId,world,pos,inv, inv.player);
        });
        tmp.setRegistryName(registryName);
        ScreenManager.registerFactory(tmp, screenSupplier);
        RegistryHandler.CONTAINER_TYPES.add(tmp);


        return tmp;
    }

    public static <T extends TileEntity> TileEntityType<T> createTileEntityType(String registryName, Supplier<T> supplier, Block... validBlocks){
        TileEntityType<T> tmp = TileEntityType.Builder.create(supplier, validBlocks).build(null);
        tmp.setRegistryName(registryName);
        RegistryHandler.TILE_ENTITY_TYPES.add(tmp);
        return tmp;
    }
}


