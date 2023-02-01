package com.kauruck.exterra.blocks;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.networks.matter.INetworkMemberBlock;
import com.kauruck.exterra.blockentities.MatterEmitterEntity;
import com.kauruck.exterra.blockentities.MatterReceiverEntity;
import com.kauruck.exterra.modules.ExTerraShared;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class TestReceiverBlock extends RitualPlateBlock implements INetworkMemberBlock {

    public TestReceiverBlock() {
        super(ExTerraShared.DEFAULT_PROPERTIES_STONE);
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MatterReceiverEntity(pPos, pState);
    }

    @Override
    public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        super.destroy(pLevel, pPos, pState);
        ExTerra.LOGGER.info("Destroyed block");
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide()) {
            if (pPlayer.getItemInHand(pHand) == ItemStack.EMPTY) {
                if(!pPlayer.isCrouching()) {
                    BlockEntity entity = pLevel.getBlockEntity(pPos);
                    if (entity instanceof MatterReceiverEntity me) {
                        me.updateInfo();
                    }
                    return InteractionResult.SUCCESS;
                }
                else {
                    BlockEntity entity = pLevel.getBlockEntity(pPos);
                    if (entity instanceof MatterReceiverEntity me) {
                        me.clearMatter();
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }
}
