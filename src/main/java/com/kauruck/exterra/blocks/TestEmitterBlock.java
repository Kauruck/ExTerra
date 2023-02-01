package com.kauruck.exterra.blocks;

import com.kauruck.exterra.api.networks.matter.INetworkMemberBlock;
import com.kauruck.exterra.blockentities.MatterEmitterEntity;
import com.kauruck.exterra.blockentities.RitualStoneEntity;
import com.kauruck.exterra.modules.ExTerraShared;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class TestEmitterBlock extends RitualPlateBlock implements INetworkMemberBlock {

    public TestEmitterBlock() {
        super(ExTerraShared.DEFAULT_PROPERTIES_STONE);
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MatterEmitterEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide){
            return null;
        }
        else {
            return (level1, pos, state1, tile) -> {
                if (tile instanceof MatterEmitterEntity matterEmitterEntity) {
                    matterEmitterEntity.serverTick();
                }
            };
        }
    }

}
