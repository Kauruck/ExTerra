package com.kauruck.exterra.data.provider;

import com.google.common.hash.HashCode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.data.ShapeData;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public abstract class GenericDataProvider implements DataProvider {

    private static final Gson GSON = (new GsonBuilder())
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .registerTypeAdapter(ShapeData.class, new ShapeData.ShapeDataSerializer())
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    private final DataGenerator dataGenerator;
    private final PackType resourceType;
    private final String folder;

    private final String mod_id;

    public GenericDataProvider(DataGenerator generator, PackType type, String folder, String mod_id){
        this.dataGenerator = generator;
        this.resourceType = type;
        this.folder = folder;
        this.mod_id = mod_id;
    }

    //Copied and Modified for 1.19 from Tinkers Construct: https://github.com/SlimeKnights/TinkersConstruct/blob/1.16/src/main/java/slimeknights/tconstruct/library/data/GenericDataProvider.java
    protected void saveThing(CachedOutput cache, String path, Object objectJson){
        try {
            String json = GenericDataProvider.GSON.toJson(objectJson);
            Path cPath = this.dataGenerator.getOutputFolder().resolve(Paths.get(resourceType.getDirectory(), mod_id, folder, path + ".json"));
            if(json.length()%2 != 0)
                json += " ";
            HashCode hash = HashCode.fromBytes(json.getBytes(StandardCharsets.UTF_8));
            cache.writeIfNeeded(cPath, json.getBytes(StandardCharsets.UTF_8), hash);
        } catch (IOException e) {
            ExTerra.LOGGER.error("Couldn't create data for mod {} {}", mod_id, path, e);
        }
    }
}
