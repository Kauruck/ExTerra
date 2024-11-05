package com.kauruck.exterra.api.geometry;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kauruck.exterra.networking.ExTerraCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public abstract class GeometricRule implements IGeometricTest{

    protected Character[] expectedBlockPos;
    protected float epsilon = 0.5f;

    public static final MapCodec<GeometricRuleData> INNER_CODEC = RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            Codec.FLOAT.fieldOf("epsilon").forGetter(GeometricRuleData::epsilon),
                            Codec.list(ExTerraCodecs.CODEC_CHARACTER).fieldOf("expectedBlockPos").forGetter(GeometricRuleData::expectedBlockPos)
                    ).apply(instance, GeometricRuleData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, GeometricRuleData> INNER_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            GeometricRuleData::epsilon,
            ByteBufCodecs.<RegistryFriendlyByteBuf, Character>list().apply(ExTerraCodecs.STREAM_CHARACTER),
            GeometricRuleData::expectedBlockPos,
            GeometricRuleData::new
    );

    public record GeometricRuleData(Float epsilon, List<Character> expectedBlockPos) {};

    protected GeometricRule() {

    }

    protected GeometricRule(GeometricRuleData data) {
        this.epsilon = data.epsilon;
        this.expectedBlockPos = data.expectedBlockPos.toArray(Character[]::new);
    }

    protected abstract Map<String, JsonElement> getParameters();

    public abstract void offerParameter(String parameterName, JsonElement jsonElement);

    public Character[] expectedBlockPosNames(){
        return expectedBlockPos;
    }

    public List<Character> getExpectedBlockPos() {
        return ImmutableList.copyOf(expectedBlockPos);
    }

    public GeometricRuleData getData() {
        return new GeometricRuleData(this.epsilon, ImmutableList.copyOf(expectedBlockPos));
    }

    public float getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(float epsilon) {
        this.epsilon = epsilon;
    }
}
