package com.kauruck.exterra.blocks;

import com.kauruck.exterra.blockentities.RitualStoneEntity;
import com.kauruck.exterra.geometry.Shape;
import com.kauruck.exterra.items.RitualMap;
import com.kauruck.exterra.modules.ExTerraCore;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.List;

public class RitualStone extends RitualPlateBlock implements EntityBlock {

    public static final BooleanProperty PROPERTY_ACTIVE = CommonBlockstates.PROPERTY_ACTIVE;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public RitualStone() {
        super(BlockBehaviour.Properties.of().
                sound(SoundType.STONE)
                .strength(2.0f)
                .requiresCorrectToolForDrops());

        this.registerDefaultState(stateDefinition.any()
                .setValue(PROPERTY_ACTIVE, true)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(PROPERTY_ACTIVE, FACING);
    }

    @Override
    public @org.jetbrains.annotations.Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction direction = pContext.getHorizontalDirection().getOpposite();
        return this.defaultBlockState()
                .setValue(FACING, direction);
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
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide()) {
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (be instanceof RitualStoneEntity ritualStoneEntity) {
                if (pPlayer.isCrouching()) {
                    ritualStoneEntity.infoToPlayer(pPlayer);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide()) {
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (be instanceof RitualStoneEntity ritualStoneEntity) {
                if (!pPlayer.isCrouching()) {
                    if (pPlayer.getItemInHand(pHand).getItem().getClass().equals(RitualMap.class)) {
                        List<Shape> shapes = pPlayer.getItemInHand(pHand).get(ExTerraCore.COMPONENT_SHAPES);
                        ritualStoneEntity.buildRitual(shapes);
                        return ItemInteractionResult.SUCCESS;
                    }
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
