package com.kauruck.exterra.blockentities;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.api.networks.matter.INetworkMember;
import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.modules.ExTerraRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;

public class MatterEmitterEntity extends BlockEntity implements INetworkMember {


    private int tick = 0;
    private MatterStack localStack;

    private Matter transportetMatter = ExTerraCore.TEST_MATTER.get();

    public MatterEmitterEntity(BlockPos pPos, BlockState pBlockState) {
        super(ExTerraCore.EMITTER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putString("matter", ExTerraRegistries.MATTER.get().getKey(transportetMatter).toString());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if(pTag.contains("matter"))
            transportetMatter = ExTerraRegistries.MATTER.get().getValue(ResourceLocation.tryParse(pTag.getString("matter")));
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
        if(localStack == null){
            localStack = new MatterStack(transportetMatter, 0);
        }
        if(tick >= 40){
            localStack.addMatter(10);
            tick = 0;
        }
        tick++;
    }

    @Override
    public Matter[] pulledMatter() {
        Matter[] out = new Matter[2];
        out[0] =  ExTerraCore.TEST_MATTER.get();
        out[1] =  ExTerraCore.TEST_MATTER_2.get();
        return out;
    }

    @Override
    public boolean pullsMatter(Matter matter) {
        return matter == transportetMatter;
    }

    @Override
    public MatterStack[] pullMatter() {
        MatterStack[] out = new MatterStack[1];
        out[0] =  localStack;
        localStack = new MatterStack(transportetMatter, 0);
        return out;
    }

    @Override
    public void applyBackpressure(MatterStack[] matters) {
        //ExTerra.LOGGER.info("Voiding matter: {}", Arrays.toString(matters));
        // Just void them I don't care
    }

    public void cycleMatter(){
        if(transportetMatter == ExTerraCore.TEST_MATTER.get())
            transportetMatter = ExTerraCore.TEST_MATTER_2.get();
        else
            transportetMatter = ExTerraCore.TEST_MATTER.get();
    }
}
