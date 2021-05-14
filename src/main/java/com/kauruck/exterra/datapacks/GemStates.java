package com.kauruck.exterra.datapacks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.kauruck.exterra.API.gem.Gem;
import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.ExTerraRegistries;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.util.Map;

public class GemStates extends JsonReloadListener {
    public static final String FOLDER = "gems/stats";
    public static Gson GSON = (new GsonBuilder())
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .registerTypeAdapter(Gem.Properties.class, new Gem.PropertiesSerializer())
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public GemStates() {
        super(GSON, FOLDER);
    }




    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        objectIn.forEach(this::loadFromJson);
    }

    private void loadFromJson(ResourceLocation resourceLocation, JsonElement json){
        Gem gem = ExTerraRegistries.GEM_REGISTRY.get().getValue(resourceLocation);
        if(gem == null) {
            ExTerra.LOGGER.warn("GemType corresponding to: " + resourceLocation + " does not exits");
            return;
        }
        gem.fromProperties(GSON.fromJson(json, Gem.Properties.class));
    }
}
