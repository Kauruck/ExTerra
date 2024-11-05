package com.kauruck.exterra.api.geometry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record StaticGeometricSerializer<A extends IGeometricTest>(
        MapCodec<A> codec,
        StreamCodec<RegistryFriendlyByteBuf,A> streamCodec
) implements IGeometricSerializer{
    @SuppressWarnings("unchecked")
    @Override
    public MapCodec<IGeometricTest> getCodec() {
        return (MapCodec<IGeometricTest>) codec;
    }

    @SuppressWarnings("unchecked")
    @Override
    public StreamCodec<RegistryFriendlyByteBuf, IGeometricTest> getStreamCodec() {
        return (StreamCodec<RegistryFriendlyByteBuf, IGeometricTest>) streamCodec;
    }
}
