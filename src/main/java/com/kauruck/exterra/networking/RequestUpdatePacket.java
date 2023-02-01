package com.kauruck.exterra.networking;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.charset.Charset;
import java.util.function.Supplier;
public class RequestUpdatePacket {
    private final BlockPos target;
    private final String propertyName;

    public RequestUpdatePacket(BlockPos target, String propertyName) {
        this.target = target;
        this.propertyName = propertyName;
    }

    public void encoder(FriendlyByteBuf buffer){
        buffer.writeBlockPos(target);
        buffer.writeUtf(propertyName);
    }

    public static RequestUpdatePacket decoder(FriendlyByteBuf buffer) {
        BlockPos pos = buffer.readBlockPos();
        String propertyName = buffer.readUtf();
        return new RequestUpdatePacket(pos, propertyName);
    }

    public void messageConsumer(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level level = ctx.get().getSender().getLevel();
            if(level.isLoaded(target)){
                BlockEntity blockEntity = level.getBlockEntity(target);
                if(blockEntity instanceof BaseBlockEntity entity){
                    entity.handelRequestProperty(propertyName, ctx.get().getSender());
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
