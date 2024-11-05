package com.kauruck.exterra.data.loader;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.*;
import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.data.ShapeData;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShapeReloadListener extends SimpleJsonResourceReloadListener {

    public BiMap<ResourceLocation, ShapeData> shapes = HashBiMap.create();

    public ShapeReloadListener() {
        super(new GsonBuilder()
                .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create(), "shapes");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        shapes.clear();
        pProfiler.push("Load Shapes");
        for(ResourceLocation currentLocation : pObject.keySet()){
            if(pObject.get(currentLocation).isJsonObject()){
                JsonObject jsonObject = pObject.get(currentLocation).getAsJsonObject();
                ShapeData shape = ShapeData.CODEC.parse(JsonOps.INSTANCE, jsonObject).getOrThrow(JsonParseException::new);
                shapes.put(shape.getName(), shape);
            }
        }

        pProfiler.pop();
    }
}
