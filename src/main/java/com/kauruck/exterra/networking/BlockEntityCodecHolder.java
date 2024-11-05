package com.kauruck.exterra.networking;

import com.kauruck.exterra.modules.ExTerraRegistries;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class BlockEntityCodecHolder<A> {

    private final Codec<A> codec;
    private final Codec<A> networkCodec;
    private ResourceLocation registryLocation;

    public BlockEntityCodecHolder(Codec<A> codec) {
        this.codec = codec;
        this.networkCodec = codec;
    }

    public BlockEntityCodecHolder(Codec<A> codec, Codec<A> networkCodec) {
        this.codec = codec;
        this.networkCodec = networkCodec;
    }

    public Codec<A> getCodec() {
        return codec;
    }

    public Codec<A> getNetworkCodec() {
        return networkCodec;
    }

    public ResourceLocation getRegistryLocation() {
        if (registryLocation == null) {
            registryLocation = ExTerraRegistries.BLOCK_ENTITY_PROPERTY_CODEC.getKey(this);
            if (registryLocation == null) {
                throw new NullPointerException("Used a a BlockEntityPropertyCodec that was not registered. "
                + this.getClass().getCanonicalName());
            }
        }
        return registryLocation;
    }
}
