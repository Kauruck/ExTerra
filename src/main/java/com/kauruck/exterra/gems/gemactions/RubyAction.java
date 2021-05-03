package com.kauruck.exterra.gems.gemactions;

import com.kauruck.exterra.API.gem.BlockBreakReturner;
import com.kauruck.exterra.API.gem.IGemAction;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RubyAction implements IGemAction {


    @Override
    public BlockBreakReturner onBlockBreak(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving, boolean supperResult) {
        return new BlockBreakReturner(supperResult);
    }

    @Override
    public void onInventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

    }

    @Override
    public void onItemUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {

    }

}
