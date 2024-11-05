package com.kauruck.exterra.modules;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class ExTerraShared {

    public static boolean never(BlockState state, BlockGetter getter, BlockPos pos) {
        return false;
    }

    public static Boolean never(BlockState pState, BlockGetter pBlockGetter, BlockPos pPos, EntityType<?> pEntity) {
        return false;
    }

    public static final BlockBehaviour.Properties DEFAULT_PROPERTIES_STONE = BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(2.0f).requiresCorrectToolForDrops();
    public static final BlockBehaviour.Properties DEFAULT_PROPERTIES_DUST = BlockBehaviour.Properties.of().instabreak().noCollission();
    public static final BlockBehaviour.Properties DEFAULT_PROPERTIES_GLASS = BlockBehaviour.Properties.of().strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(ExTerraShared::never).isRedstoneConductor(ExTerraShared::never).isSuffocating(ExTerraShared::never).isViewBlocking(ExTerraShared::never).requiresCorrectToolForDrops();

    public static final Item.Properties DEFAULT_PROPERTIES_ITEM = new Item.Properties();

}
