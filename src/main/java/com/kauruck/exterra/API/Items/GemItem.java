package com.kauruck.exterra.API.Items;

import com.kauruck.exterra.API.Gems.BlockBreakReturner;
import com.kauruck.exterra.Init.ModTabs;
import com.kauruck.exterra.Item.GemType;
import com.kauruck.exterra.Item.ItemBase;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GemItem extends ItemBase {
    private final GemType gemType;

    public GemItem(GemType gemType) {
        super(gemType.getRegistryName(), ModTabs.RESOURCES);
        this.gemType = gemType;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        gemType.getActionHandler().onInventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        gemType.getActionHandler().onItemUse(worldIn, livingEntityIn, stack, count);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        BlockBreakReturner res = gemType.getActionHandler().onBlockBreak(stack, worldIn, state, pos, entityLiving, super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving));
        if(res.dropOverride != null){
            if(!worldIn.isRemote){
                worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), res.dropOverride));
            }
        }

        return res.addToStatistics;
    }
}
