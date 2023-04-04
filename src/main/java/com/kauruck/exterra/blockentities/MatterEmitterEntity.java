package com.kauruck.exterra.blockentities;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.api.networks.matter.INetworkMember;
import com.kauruck.exterra.modules.ExTerraCore;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;

public class MatterEmitterEntity extends BlockEntity implements INetworkMember {


    private int tick = 0;
    private MatterStack localStack = new MatterStack(ExTerraCore.TEST_MATTER.get(), 0);

    public MatterEmitterEntity(BlockPos pPos, BlockState pBlockState) {
        super(ExTerraCore.EMITTER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public String getName() {
        return "Test Emitter";
    }

    @Override
    public boolean acceptsMatter(Matter matter) {
        return false;
    }

    @Override
    public Matter[] acceptedMatter() {
        return new Matter[0];
    }

    @Override
    public MatterStack pushMatter(MatterStack matterStack) {
        return null;
    }

    public void serverTick(){
        if(tick >= 40){
            localStack.addMatter(10);
            tick = 0;
        }
        tick++;
    }

    @Override
    public Matter[] pulledMatter() {
        Matter[] out = new Matter[1];
        out[0] =  ExTerraCore.TEST_MATTER.get();
        return out;
    }

    @Override
    public boolean pullsMatter(Matter matter) {
        return matter == ExTerraCore.TEST_MATTER.get();
    }

    @Override
    public MatterStack[] pullMatter() {
        MatterStack[] out = new MatterStack[1];
        out[0] =  localStack;
        localStack = new MatterStack(ExTerraCore.TEST_MATTER.get(), 0);
        return out;
    }

    @Override
    public void applyBackpressure(MatterStack[] matters) {
        //ExTerra.LOGGER.info("Voiding matter: {}", Arrays.toString(matters));
        // Just void them I don't care
    }
}
