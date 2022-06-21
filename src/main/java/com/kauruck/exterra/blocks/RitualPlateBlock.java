package com.kauruck.exterra.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;


public abstract class RitualPlateBlock extends Block {
    protected static final VoxelShape SHAPE = net.minecraft.world.level.block.Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

    public RitualPlateBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }
}
