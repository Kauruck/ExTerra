package com.kauruck.exterra.api.geometry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface IGeometricSerializer {

    MapCodec<IGeometricTest> getCodec();

    StreamCodec< RegistryFriendlyByteBuf,IGeometricTest> getStreamCodec();
}

