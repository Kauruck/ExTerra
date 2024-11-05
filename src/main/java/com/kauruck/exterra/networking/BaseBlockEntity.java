package com.kauruck.exterra.networking;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.modules.ExTerraRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
public abstract class BaseBlockEntity extends BlockEntity {

    private final Map<String, BlockEntityProperty<?>> properties = new HashMap<>();
    public BaseBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }


    //---Saving/Loading
    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        saveProperties(properties, pTag);
    }

    private void saveProperties(Map<String, BlockEntityProperty<?>> properties, CompoundTag tag){
        for(String key : properties.keySet()){
            tag.put(key, properties.get(key).toTag(false));
        }
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        for(String key : pTag.getAllKeys()){
            if(properties.containsKey(key)){
                    properties.get(key).setFromTag(pTag.get(key), false);
            }
        }
    }

    //Properties

    protected <T> BlockEntityProperty<T> createProperty(BlockEntityPropertySide side, String name, T data, ResourceLocation codeHolderLocation){
        BlockEntityCodecHolder<T> codecHolder = (BlockEntityCodecHolder<T>) ExTerraRegistries.BLOCK_ENTITY_PROPERTY_CODEC.get(codeHolderLocation);
        return this.createProperty(side, name, data, codecHolder);
    }


    protected <T> BlockEntityProperty<T> createProperty(BlockEntityPropertySide side, String name, T data, BlockEntityCodecHolder<T> codecHolder){
        BlockEntityProperty<T> out = new BlockEntityProperty<>(data, name, codecHolder, side);
        properties.put(name, out);
        return out;
    }

    protected void requestProperty(BlockEntityProperty<?> property){
        requestProperty(property.getName());
    }

    protected void requestProperty(String name){
        PacketDistributor.sendToServer(new RequestUpdatePacket(this.getBlockPos(), name));
    }

    void handelRequestProperty(String name, ServerPlayer player){
        if(this.properties.containsKey(name)){
            CompoundTag tag = new CompoundTag();
            tag.put(name, this.properties.get(name).toTag(true));
            PacketDistributor.sendToPlayer(player, new BlockEntityClientBoundUpdatePacket(tag, this.getBlockPos()));
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if(!this.getLevel().isClientSide()) {
            CompoundTag tag = generateUpdateTag();
            BlockEntityClientBoundUpdatePacket packet = new BlockEntityClientBoundUpdatePacket(tag, this.getBlockPos());
            PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) this.getLevel(), this.getLevel().getChunkAt(this.getBlockPos()).getPos(), packet);
            properties.values().forEach(BlockEntityProperty::confirmSend);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag out = super.getUpdateTag(pRegistries);
        CompoundTag syncTag = new CompoundTag();
        for (BlockEntityProperty<?> current : properties.values()) {
            if (current.getSide() == BlockEntityPropertySide.Synced || current.isShouldBeUpdated())
                out.put(current.getName(), current.toTag(true));
            else if (current.getSide() == BlockEntityPropertySide.Requestable)
                syncTag.putBoolean(current.getName(), true);
            out.put("syncTag", syncTag);
        }
        return out;
    }

    private CompoundTag generateUpdateTag(){
        CompoundTag out = new CompoundTag();
        CompoundTag syncTag = new CompoundTag();
        for(BlockEntityProperty<?> current : properties.values()){
            if(current.getSide() == BlockEntityPropertySide.Synced || current.isShouldBeUpdated())
                out.put(current.getName(), current.toTag(true));
            else if(current.getSide() == BlockEntityPropertySide.Requestable)
                syncTag.putBoolean(current.getName(), true);
        }
        out.put("syncTag", syncTag);
        return out;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        handleUpdateTag(tag);
    }

    public void handleUpdateTag(CompoundTag tag) {
        for(String key : tag.getAllKeys()){
            if(properties.containsKey(key)){
                properties.get(key).setFromTag(tag.get(key), true);
            }
        }
    }

    public Collection<BlockEntityProperty<?>> getProperties() {
        return this.properties.values();
    }
}
