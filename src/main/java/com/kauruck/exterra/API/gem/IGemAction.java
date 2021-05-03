package com.kauruck.exterra.API.gem;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IGemAction {

    BlockBreakReturner onBlockBreak(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving, boolean supperResult);
    void onInventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected);
    void onItemUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count);

}
