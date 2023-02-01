package com.kauruck.exterra.networking;

import com.kauruck.exterra.ExTerra;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
public abstract class BaseBlockEntity extends BlockEntity {

    private final Map<String, BlockEntityProperty<?>> properties = new HashMap<>();
    public BaseBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    //---Saving/Loading
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        saveProperties(properties, pTag);
    }

    private void saveProperties(Map<String, BlockEntityProperty<?>> properties, CompoundTag tag){
        for(String key : properties.keySet()){
            tag.put(key, properties.get(key).toNBT());
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        for(String key : pTag.getAllKeys()){
            if(properties.containsKey(key)){
                    properties.get(key).setFromTag(pTag.get(key));
            }
        }
    }

    //Properties
    protected  <T> BlockEntityProperty<T> createProperty(BlockEntityPropertySide side, String name, @NotNull T data){
        if(data instanceof Iterable<?> iData){
            if(iData.iterator().hasNext()){
                Object value = iData.iterator().next();
                if(value != null){
                    return createProperty(side, name, data, (Class<T>) data.getClass(), value.getClass());
                }
            }
        }
        return createProperty(side, name, data, (Class<T>) data.getClass(), null);
    }

    protected <T> BlockEntityProperty<T> createProperty(BlockEntityPropertySide side, String name, T data, Class<?> clazz){
        if (data instanceof Iterable<?>){
            return createProperty(side, name, data, (Class<T>) data.getClass(), clazz);
        }
        return createProperty(side, name, data, (Class<T>)clazz, null);
    }


    protected <T> BlockEntityProperty<T> createProperty(BlockEntityPropertySide side, String name, T data, Class<T> dataClass, Class<?> innerClass){
        BlockEntityProperty<T> out = new BlockEntityProperty<>(data, name, dataClass, innerClass, side);
        properties.put(name, out);
        return out;
    }

    protected void requestProperty(BlockEntityProperty<?> property){
        requestProperty(property.getName());
    }

    protected void requestProperty(String name){
        ExTerraNetworking.INSTANCE.sendToServer(new RequestUpdatePacket(this.getBlockPos(), name));
    }

    void handelRequestProperty(String name, ServerPlayer player){
        if(this.properties.containsKey(name)){
            CompoundTag tag = new CompoundTag();
            tag.put(name, this.properties.get(name).toNBT());
            ExTerraNetworking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new BlockEntityClientBoundUpdatePacket(tag, this.getBlockPos()));
        }
    }

    private boolean isSomethingToSync(){
        return properties.values().stream()
                .anyMatch((e) -> e.changed);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if(!this.getLevel().isClientSide() && this.isSomethingToSync()) {
            CompoundTag tag = generateUpdateTag();
            BlockEntityClientBoundUpdatePacket packet = new BlockEntityClientBoundUpdatePacket(tag, this.getBlockPos());
            ExTerraNetworking.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> this.getLevel().getChunkAt(this.getBlockPos())), packet);
            properties.values().forEach((current) -> current.changed = false);
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag out = super.getUpdateTag();
        CompoundTag syncTag = new CompoundTag();
        for (BlockEntityProperty<?> current : properties.values()) {
            if (current.getSide() == BlockEntityPropertySide.Synced)
                out.put(current.getName(), current.toNBT());
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
            if(current.changed) {
                if(current.getSide() == BlockEntityPropertySide.Synced)
                    out.put(current.getName(), current.toNBT());
                else if(current.getSide() == BlockEntityPropertySide.Requestable)
                    syncTag.putBoolean(current.getName(), true);
            }
        }
        out.put("syncTag", syncTag);
        return out;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.load(tag);
        for(String key : tag.getAllKeys()){
            if(properties.containsKey(key)){
                properties.get(key).setFromTag(tag.get(key));
            }
        }
    }

    public void handelClientUpdateTag(CompoundTag update){
        for(String currentKey : update.getAllKeys()){
            if(currentKey.equals("syncTag")) {
                CompoundTag syncTag = update.getCompound("syncTag");
                for(String syncKey : syncTag.getAllKeys()){
                    if(this.properties.containsKey(syncKey)){
                        this.properties.get(syncKey).outOfSync = true;
                    }
                }
            }
            else if(this.properties.containsKey(currentKey)){
                this.properties.get(currentKey).setFromTag(update.get(currentKey));
            }
        }
    }

    public Collection<BlockEntityProperty<?>> getProperties() {
        return this.properties.values();
    }
}
