package com.kauruck.exterra.blocks;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.blockentities.RitualStoneEntity;
import com.kauruck.exterra.geometry.ShapeCollection;
import com.kauruck.exterra.items.RitualMap;
import com.kauruck.exterra.modules.ExTerraShared;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class RitualStone extends Block implements EntityBlock {
    public RitualStone() {
        super(ExTerraShared.DEFAULT_PROPERTIES_STONE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RitualStoneEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(level.isClientSide()){
            return null;
        }else{
            return (level1, pos, state1, tile) -> {
                if (tile instanceof RitualStoneEntity ritualStoneEntity) {
                    ritualStoneEntity.serverTick();
                }
            };
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (be instanceof RitualStoneEntity ritualStoneEntity) {
                if (!pPlayer.isCrouching()) {
                    if(pPlayer.getInventory().getSelected().getItem().getClass().equals(RitualMap.class)) {
                        ritualStoneEntity.setShapes(new ShapeCollection(pPlayer.getInventory().getSelected().getTag().getCompound(RitualMap.TAG_SHAPES)));
                        ritualStoneEntity.buildRitual();
                        return InteractionResult.SUCCESS;
                    }
                } else {
                    ritualStoneEntity.infoToPlayer(pPlayer);
                    return InteractionResult.PASS;
                }
            }
        }
        return InteractionResult.PASS;
    }
}
