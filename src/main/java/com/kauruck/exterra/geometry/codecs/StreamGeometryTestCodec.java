package com.kauruck.exterra.geometry.codecs;

import com.kauruck.exterra.api.exceptions.NetworkException;
import com.kauruck.exterra.api.geometry.IGeometricSerializer;
import com.kauruck.exterra.api.geometry.IGeometricTest;
import com.kauruck.exterra.modules.ExTerraRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamDecoder;
import net.minecraft.network.codec.StreamEncoder;
import net.minecraft.resources.ResourceLocation;

public class StreamGeometryTestCodec implements StreamEncoder<RegistryFriendlyByteBuf, IGeometricTest>,
        StreamDecoder<RegistryFriendlyByteBuf, IGeometricTest> {

    @Override
    public IGeometricTest decode(RegistryFriendlyByteBuf pBuffer) {
        ResourceLocation testLocation = pBuffer.readResourceLocation();
        IGeometricSerializer serializer = ExTerraRegistries.GEOMETRIC_SERIALIZER.get(testLocation);
        if (serializer == null) {
            throw new NetworkException("No geometric test found for " + testLocation);
        }
        return serializer.getStreamCodec().decode(pBuffer);
    }

    @Override
    public void encode(RegistryFriendlyByteBuf pBuffer, IGeometricTest pValue) {
        pBuffer.writeResourceLocation(pValue.getSerializerLocation());
        pValue.getSerializer().getStreamCodec().encode(pBuffer, pValue);
    }
}
