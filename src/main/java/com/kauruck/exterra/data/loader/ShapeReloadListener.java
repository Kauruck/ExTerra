package com.kauruck.exterra.data.loader;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.data.ShapeData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShapeReloadListener extends SimpleJsonResourceReloadListener {

    public static Map<String, ShapeData> shapes = new HashMap<>();

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
                if(jsonObject.has("name") && jsonObject.has("points") && jsonObject.has("predicts")){
                    String name = jsonObject.get("name").getAsString();
                    int numberOfPoints = jsonObject.get("points").getAsInt();
                    JsonArray predictsArray = jsonObject.get("predicts").getAsJsonArray();
                    ShapeData shape = new ShapeData(name, numberOfPoints);
                    shape.setPredictsFromJSON(predictsArray);
                    shapes.put(shape.getName(), shape);
                }
            }
        }

        pProfiler.pop();
    }
}
