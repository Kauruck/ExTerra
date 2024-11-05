package com.kauruck.exterra.networking;

import com.kauruck.exterra.ExTerra;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class RequestUpdatePacket implements CustomPacketPayload {

    private final BlockPos target;
    private final String propertyName;

    public static final Type<RequestUpdatePacket> TYPE = new CustomPacketPayload
            .Type<>(ExTerra.getResource("packet_serverbound_request"));

    public static final StreamCodec<ByteBuf, RequestUpdatePacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            RequestUpdatePacket::getTarget,
            ByteBufCodecs.STRING_UTF8,
            RequestUpdatePacket::getPropertyName,
            RequestUpdatePacket::new
    );

    public BlockPos getTarget() {
        return target;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public RequestUpdatePacket(BlockPos target, String propertyName) {
        this.target = target;
        this.propertyName = propertyName;
    }


    public static void handle(final RequestUpdatePacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = context.player().level();
            if(level.isLoaded(data.target)){
                BlockEntity blockEntity = level.getBlockEntity(data.target);
                if(blockEntity instanceof BaseBlockEntity entity){
                    entity.handelRequestProperty(data.propertyName, (ServerPlayer) context.player());
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
