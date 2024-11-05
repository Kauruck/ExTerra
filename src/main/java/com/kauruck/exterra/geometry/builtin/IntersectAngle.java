package com.kauruck.exterra.geometry.builtin;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.geometry.GeometricRule;
import com.kauruck.exterra.geometry.BlockPosHolder;
import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.util.MathUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class IntersectAngle extends GeometricRule {

    private float angle;

    public static final MapCodec<IntersectAngle> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    INNER_CODEC.fieldOf("inner").forGetter(GeometricRule::getData),
                    Codec.FLOAT.fieldOf("angle").forGetter(IntersectAngle::getAngle)
            ).apply(instance, IntersectAngle::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, IntersectAngle> STREAM_CODEC = StreamCodec.composite(
            INNER_STREAM_CODEC,
            GeometricRule::getData,
            ByteBufCodecs.FLOAT,
            IntersectAngle::getAngle,
            IntersectAngle::new
    );

    public IntersectAngle(GeometricRuleData data, Float angle) {
        super(data);
        this.angle = angle;
    }

    public IntersectAngle(Character pointA, Character pointB, Character pointC, float angle){
        this.angle = angle;
        this.expectedBlockPos = new Character[]{pointA, pointB, pointC};
    }

    @Override
    public boolean test(BlockPosHolder positions) {
        //AB
        float A_Dx = positions.getBlockPos('B').getX() - positions.getBlockPos('A').getX();
        float A_Dy = positions.getBlockPos('B').getY() - positions.getBlockPos('A').getY();
        float A_Dz = positions.getBlockPos('B').getZ() - positions.getBlockPos('A').getZ();

        //AC
        float B_Dx = positions.getBlockPos('C').getX() - positions.getBlockPos('A').getX();
        float B_Dy = positions.getBlockPos('C').getY() - positions.getBlockPos('A').getY();
        float B_Dz = positions.getBlockPos('C').getZ() - positions.getBlockPos('A').getZ();

        float dotProduct = A_Dx * B_Dx + A_Dy * B_Dy + A_Dz * B_Dz;
        float distanceA = (float) Math.sqrt(A_Dx * A_Dx + A_Dy * A_Dy + A_Dz * A_Dz);
        float distanceB = (float) Math.sqrt(B_Dx * B_Dx + B_Dy * B_Dy + B_Dz * B_Dz);

        float angle = (float) Math.acos(dotProduct/(distanceA * distanceB));

        return MathUtil.almostEqual(angle, this.angle, this.epsilon);

    }

    @Override
    public ResourceLocation getName() {
        return ExTerra.getResource( "angle");
    }

    @Override
    public ResourceLocation getSerializerLocation() {
        return ExTerraCore.GEOMETRIC_INTERSECT.getKey().location();
    }

    @Override
    protected Map<String, JsonElement> getParameters() {
        Map<String, JsonElement> out = new HashMap<>();
        out.put("angle", new JsonPrimitive(angle));
        return out;
    }

    @Override
    public void offerParameter(String parameterName, JsonElement jsonElement) {
        if(parameterName.equals("angle")){
            this.angle = jsonElement.getAsFloat();
        }
    }

    public float getAngle() {
        return angle;
    }
}
