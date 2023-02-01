package com.kauruck.exterra.blockentities;

import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.api.networks.matter.INetworkMember;
import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.networking.BaseBlockEntity;
import com.kauruck.exterra.networking.BlockEntityProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import static com.kauruck.exterra.networking.BlockEntityPropertySide.*;

public class MatterReceiverEntity extends BaseBlockEntity implements INetworkMember {
    private BlockEntityProperty<Integer> receivedMatter = createProperty(Requestable, "receivedMatter", 0);

    public MatterReceiverEntity(BlockPos pPos, BlockState pBlockState) {
        super(ExTerraCore.RECEIVER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public String getName() {
        return "Test Matter Receiver";
    }

    @Override
    public boolean acceptsMatter(Matter matter) {
        return matter == ExTerraCore.TEST_MATTER.get();
    }

    @Override
    public Matter[] acceptedMatter() {
        Matter[] out = new Matter[1];
        out[0] =  ExTerraCore.TEST_MATTER.get();
        return out;
    }

    @Override
    public MatterStack pushMatter(MatterStack matterStack) {
        receivedMatter.set(receivedMatter.get() + matterStack.getAmount());
        this.setChanged();
        return MatterStack.EMPTY;
    }

    public void infoToPlayer(Player player){
        player.sendSystemMessage(Component.literal(Integer.toString(receivedMatter.get())));
    }

    public void updateInfo(){
        requestProperty(receivedMatter);
    }

    @Override
    public Matter[] pulledMatter() {
        return new Matter[0];
    }

    @Override
    public boolean pullsMatter(Matter matter) {
        return false;
    }

    @Override
    public MatterStack[] pullMatter() {
        return new MatterStack[0];
    }

    @Override
    public void applyBackpressure(MatterStack[] matters) {

    }
    public int getReceivedMatter() {
        return receivedMatter.get();
    }

    public void clearMatter() {
        this.receivedMatter.set(0);
        this.setChanged();
    }
}
