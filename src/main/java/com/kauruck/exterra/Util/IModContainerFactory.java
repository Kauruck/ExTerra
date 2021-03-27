package com.kauruck.exterra.Util;

import com.kauruck.exterra.GUIs.Machines.BaseContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IModContainerFactory {

    Container create(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player);
}
