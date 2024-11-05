package com.kauruck.exterra.geometry.elmental;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kauruck.exterra.api.geometry.IGeometricSerializer;
import com.kauruck.exterra.api.geometry.IGeometricTest;
import com.kauruck.exterra.geometry.codecs.GeometryCodecs;
import com.kauruck.exterra.modules.ExTerraRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public abstract class ElementalOperation implements IGeometricTest {

    protected List<IGeometricTest> parts = new ArrayList<>();
    private Character[] expectedBlockPosNames;

    private void fillBlockPos() {
        this.expectedBlockPosNames = generateExpectedBlockPoses();
    }

    public ElementalOperation(List<IGeometricTest> parts) {
        this.parts = parts;
        this.expectedBlockPosNames = generateExpectedBlockPoses();
    }


    private Character[] generateExpectedBlockPoses(){
        List<Character> characters = new ArrayList<>();
        for(IGeometricTest current : parts){
            for(Character currentChar : current.expectedBlockPosNames()){
                if(!characters.contains(currentChar))
                    characters.add(currentChar);
            }
        }
        return characters.toArray(new Character[0]);
    }

    @Override
    public Character[] expectedBlockPosNames() {
        return expectedBlockPosNames;
    }

    public List<IGeometricTest> getParts() {
        return parts;
    }

    public void setParts(List<IGeometricTest> parts) {
        this.parts = parts;
    }
}
