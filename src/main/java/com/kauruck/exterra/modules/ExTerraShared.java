package com.kauruck.exterra.modules;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class ExTerraShared {

    public static boolean never(BlockState state, BlockGetter getter, BlockPos pos) {
        return false;
    }

    public static Boolean never(BlockState pState, BlockGetter pBlockGetter, BlockPos pPos, EntityType<?> pEntity) {
        return false;
    }

    public static final BlockBehaviour.Properties DEFAULT_PROPERTIES_STONE = BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.STONE).strength(2.0f).requiresCorrectToolForDrops();
    public static final BlockBehaviour.Properties DEFAULT_PROPERTIES_DUST = BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noCollission();
    public static final BlockBehaviour.Properties DEFAULT_PROPERTIES_GLASS = BlockBehaviour.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(ExTerraShared::never).isRedstoneConductor(ExTerraShared::never).isSuffocating(ExTerraShared::never).isViewBlocking(ExTerraShared::never).requiresCorrectToolForDrops();

    public static final Item.Properties DEFAULT_PROPERTIES_ITEM = new Item.Properties().tab(CreativeModeTab.TAB_MISC);

}
