package com.kauruck.exterra.data;

import com.google.gson.*;
import com.kauruck.exterra.api.geometry.GeometricRule;
import com.kauruck.exterra.api.geometry.IGeometricTest;
import com.kauruck.exterra.geometry.BlockPosHolder;
import com.kauruck.exterra.geometry.codecs.GeometryCodecs;
import com.kauruck.exterra.modules.ExTerraRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ShapeData {

    public static Codec<ShapeData> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                ResourceLocation.CODEC.fieldOf("name").forGetter(ShapeData::getName),
                Codec.INT.fieldOf("numberOfPoints").forGetter(ShapeData::getNumberOfPoints),
                Codec.list(GeometryCodecs.CODEC_GEOMETRIC_TEST).fieldOf("parts").forGetter(ShapeData::getParts)
        ).apply(instance, ShapeData::new)
    );

    public static StreamCodec<RegistryFriendlyByteBuf, ShapeData> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            ShapeData::getName,
            ByteBufCodecs.INT,
            ShapeData::getNumberOfPoints,
            GeometryCodecs.STREAM_CODEC_GEOMETRIC_TEST.apply(ByteBufCodecs.list()),
            ShapeData::getParts,
            ShapeData::new
    );

    private final ResourceLocation name;
    private final int numberOfPoints;
    private List<IGeometricTest> parts = new ArrayList<>();

    public ShapeData(ResourceLocation name, int numberOfPoints){
        this.name = name;
        this.numberOfPoints = numberOfPoints;
    }

    private ShapeData(ResourceLocation name, int numberOfPoints, List<IGeometricTest> parts){
        this.name = name;
        this.numberOfPoints = numberOfPoints;
        this.parts = parts;
    }

    public ResourceLocation getName() {
        return name;
    }

    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public List<IGeometricTest> getParts() {
        return parts;
    }

    public boolean test(BlockPosHolder positions){
        if(positions.size() != numberOfPoints)
            return false;
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
    public String toString() {
        return "Shape: " + name;
    }

    public String getTranslationKey()
    {
        return "shape." + name.getNamespace() + "." + name.getPath();
    }

    public void setParts(List<IGeometricTest> parts) {
        this.parts = parts;
    }

}
