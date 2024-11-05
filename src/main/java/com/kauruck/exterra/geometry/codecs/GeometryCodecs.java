package com.kauruck.exterra.geometry.codecs;

import com.kauruck.exterra.api.geometry.IGeometricSerializer;
import com.kauruck.exterra.api.geometry.IGeometricTest;
import com.kauruck.exterra.modules.ExTerraRegistries;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class GeometryCodecs {

    public static final Codec<IGeometricTest> CODEC_GEOMETRIC_TEST = ExTerraRegistries.GEOMETRIC_SERIALIZER.byNameCodec()
            .dispatch(
                    IGeometricTest::getSerializer,
                    IGeometricSerializer::getCodec
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, IGeometricTest> STREAM_CODEC_GEOMETRIC_TEST
            = ByteBufCodecs.registry(ExTerraRegistries.GEOMETRIC_SERIALIZER_KEY)
            .dispatch(
                    IGeometricTest::getSerializer,
                    IGeometricSerializer::getStreamCodec
            );
}
