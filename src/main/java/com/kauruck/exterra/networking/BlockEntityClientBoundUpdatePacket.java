package com.kauruck.exterra.networking;

import com.kauruck.exterra.ExTerra;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public class BlockEntityClientBoundUpdatePacket implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<BlockEntityClientBoundUpdatePacket> TYPE = new CustomPacketPayload
            .Type<>(ExTerra.getResource("packet_clientbound_update"));

    public static final StreamCodec<RegistryFriendlyByteBuf, BlockEntityClientBoundUpdatePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG,
            BlockEntityClientBoundUpdatePacket::getDataToSet,
            BlockPos.STREAM_CODEC,
            BlockEntityClientBoundUpdatePacket::getTargetPos,
            BlockEntityClientBoundUpdatePacket::new
    );


    private final CompoundTag dataToSet;
    private final BlockPos targetPos;

    public BlockEntityClientBoundUpdatePacket(CompoundTag dataToSet, BlockPos pos){
        this.dataToSet = dataToSet;
        this.targetPos = pos;
    }

    public CompoundTag getDataToSet() {
        return dataToSet;
    }

    public BlockPos getTargetPos() {
        return targetPos;
    }

    public static void handle(final BlockEntityClientBoundUpdatePacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Level clientLevel = Minecraft.getInstance().level;
            if (clientLevel == null) {
                ExTerraNetworking.LOGGER.error("Tried to update block entity data, but client level is null");
            } else {
                BlockEntity entity = clientLevel.getBlockEntity(data.targetPos);
                 if (entity instanceof BaseBlockEntity baseBlockEntity) {
                     baseBlockEntity.handleUpdateTag(data.getDataToSet());
                 }
            }
        }).exceptionally(e -> {
            ExTerraNetworking.LOGGER.error("Error when updating block entity", e);
            return null;
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
