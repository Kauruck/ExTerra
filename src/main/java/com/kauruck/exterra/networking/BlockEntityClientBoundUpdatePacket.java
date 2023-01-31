package com.kauruck.exterra.networking;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BlockEntityClientBoundUpdatePacket {

    private final CompoundTag tag;
    private final BlockPos targetPos;

    public BlockEntityClientBoundUpdatePacket(CompoundTag tag, BlockPos pos){
        this.tag = tag;
        this.targetPos = pos;
    }

    public void encoder(FriendlyByteBuf buffer) {
        buffer.writeNbt(tag);
        buffer.writeBlockPos(this.targetPos);
    }

    public static BlockEntityClientBoundUpdatePacket decoder(FriendlyByteBuf buffer) {
        CompoundTag tag = buffer.readNbt();
        BlockPos pos = buffer.readBlockPos();
        return new BlockEntityClientBoundUpdatePacket(tag, pos);
    }

    public void messageConsumer(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> BlockEntityClientUpdateHandler.handleUpdate(tag, targetPos));
        });
        ctx.get().setPacketHandled(true);
    }
}
