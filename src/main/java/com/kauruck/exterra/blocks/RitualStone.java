package com.kauruck.exterra.blocks;

import com.kauruck.exterra.blockentities.RitualStoneEntity;
import com.kauruck.exterra.geometry.ShapeCollection;
import com.kauruck.exterra.items.RitualMap;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class RitualStone extends RitualPlateBlock implements EntityBlock {

    public RitualStone() {
        super(BlockBehaviour.Properties.of(Material.STONE).
                sound(SoundType.STONE)
                .strength(2.0f)
                .requiresCorrectToolForDrops());
    }




    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RitualStoneEntity(pPos, pState);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        BlockEntity entity = pLevel.getBlockEntity(pPos);
        if(entity instanceof RitualStoneEntity ritualStoneEntity){
            ritualStoneEntity.animationTick((ClientLevel) pLevel, pRandom);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(level.isClientSide()){
            return (level1, pos, state1, tile) -> {
                if (tile instanceof RitualStoneEntity ritualStoneEntity) {
                    ritualStoneEntity.clientTick((ClientLevel) level1);
                }
            };
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
                        CompoundTag tag = pPlayer.getInventory().getSelected().getTag();
                        if(tag != null)
                            ritualStoneEntity.setShapes(new ShapeCollection(tag.getCompound(RitualMap.TAG_SHAPES)));
                        else
                            ritualStoneEntity.setShapes(new ShapeCollection());
                        ritualStoneEntity.buildRitual((ServerPlayer) pPlayer);
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
