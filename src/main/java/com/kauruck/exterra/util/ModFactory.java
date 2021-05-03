package com.kauruck.exterra.util;

import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;

import java.util.function.Supplier;

public class ModFactory {

    @SuppressWarnings("unchecked")
    public static <T extends Container> ContainerType<T> createContainerType(IModContainerFactory supplier, ScreenManager.IScreenFactory<T, ContainerScreen<T>> screenSupplier){
        ContainerType<T> tmp = (ContainerType<T>) IForgeContainerType.create((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            World world = inv.player.getEntityWorld();
            return supplier.create(windowId,world,pos,inv, inv.player);
        });
        ScreenManager.registerFactory(tmp, screenSupplier);

        return tmp;
    }

    public static <T extends TileEntity> TileEntityType<T> createTileEntityType(Supplier<T> supplier, Block... validBlocks){
        TileEntityType<T> tmp = TileEntityType.Builder.create(supplier, validBlocks).build(null);
        return tmp;
    }


    public interface IModContainerFactory {

        Container create(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player);
    }

}




