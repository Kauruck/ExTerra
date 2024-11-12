package com.kauruck.exterra.blockentities;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.api.networks.matter.INetworkMember;
import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.modules.ExTerraRegistries;
import com.kauruck.exterra.modules.NetworkInbuilt;
import com.kauruck.exterra.modules.RegistryManger;
import com.kauruck.exterra.networking.BaseBlockEntity;
import com.kauruck.exterra.networking.BlockEntityProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import static com.kauruck.exterra.networking.BlockEntityPropertySide.*;

public class MatterReceiverEntity extends BaseBlockEntity implements INetworkMember {
    private BlockEntityProperty<Integer> receivedMatter = createProperty(Requestable, "receivedMatter", 0, NetworkInbuilt.PROPERTY_INTEGER.get());
    private BlockEntityProperty<ResourceLocation> receivedMatterName = createProperty(Requestable, "receivedMatterName",
            ExTerra.getResource("none"), NetworkInbuilt.PROPERTY_RESOURCE_LOCATION.get());

    public MatterReceiverEntity(BlockPos pPos, BlockState pBlockState) {
        super(ExTerraCore.RECEIVER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public String getName() {
        return "Test Matter Receiver";
    }

    @Override
    public boolean acceptsMatter(Matter matter, Direction direction) {
        return matter == ExTerraCore.TEST_MATTER.get() || matter == ExTerraCore.TEST_MATTER_2.get();
    }

    @Override
    public Matter[] acceptedMatter() {
        Matter[] out = new Matter[2];
        out[0] =  ExTerraCore.TEST_MATTER.get();
        out[1] = ExTerraCore.TEST_MATTER_2.get();
        return out;
    }

    @Override
    public MatterStack pushMatter(MatterStack matterStack, Direction direction) {
        if (ExTerraRegistries.MATTER.getKey(matterStack.getMatter()) ==  receivedMatterName.get()) {
            receivedMatter.set(receivedMatter.get() + matterStack.getAmount());
        } else {
            receivedMatterName.set(ExTerraRegistries.MATTER.getKey(matterStack.getMatter()));
            receivedMatter.set(matterStack.getAmount());
        }
        this.setChanged();
        return MatterStack.EMPTY;
    }

    public void updateInfo(){
        requestProperty(receivedMatter);
        requestProperty(receivedMatterName);
    }

    @Override
    public Matter[] pulledMatter(Direction direction) {
        return new Matter[0];
    }

    @Override
    public boolean pullsMatter(Matter matter) {
        return false;
    }

    @Override
    public MatterStack pullMatter(Matter matter, Direction direction) {
        return null;
    }

    @Override
    public void applyBackpressure(MatterStack[] matters) {

    }
    public int getReceivedMatter() {
        return receivedMatter.get();
    }

    public ResourceLocation getMatterName() {
        return receivedMatterName.get();
    }

    public void clearMatter() {
        this.receivedMatter.set(0);
        this.receivedMatterName.set(ExTerra.getResource("none"));
        this.setChanged();
    }
}
