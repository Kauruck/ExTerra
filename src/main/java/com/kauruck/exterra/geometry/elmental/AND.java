package com.kauruck.exterra.geometry.elmental;


import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.geometry.GeometricRule;
import com.kauruck.exterra.api.geometry.IGeometricSerializer;
import com.kauruck.exterra.api.geometry.IGeometricTest;
import com.kauruck.exterra.geometry.BlockPosHolder;
import com.kauruck.exterra.geometry.codecs.GeometryCodecs;
import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.modules.ExTerraRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.List;


public class AND extends ElementalOperation {

    public static final MapCodec<AND> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.list(GeometryCodecs.CODEC_GEOMETRIC_TEST).fieldOf("parts").forGetter(AND::getParts)
            ).apply(instance, AND::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AND> STREAM_CODEC = StreamCodec.composite(
            GeometryCodecs.STREAM_CODEC_GEOMETRIC_TEST.apply(ByteBufCodecs.list()),
            AND::getParts,
            AND::new
    );

    public AND(List<IGeometricTest> parts) {
        super(parts);
    }

    public AND() {
        super(List.of());
    }

    @Override
    public boolean test(BlockPosHolder positions) {
        for(IGeometricTest currentTest : this.parts){
            BlockPosHolder subHolder;
            if(currentTest instanceof GeometricRule){
                subHolder = positions.subHolder(currentTest.expectedBlockPosNames());
            }else{
                subHolder = positions;
            }

            if(!currentTest.test(subHolder))
                return false;
        }

        return true;
    }

    @Override
    public ResourceLocation getName() {
        return ExTerra.getResource("and");
    }

    @Override
    public ResourceLocation getSerializerLocation() {
        return ExTerraCore.GEOMETRIC_AND.getDelegate().getKey().location();
    }

}
