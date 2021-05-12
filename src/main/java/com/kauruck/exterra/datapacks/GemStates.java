package com.kauruck.exterra.datapacks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.kauruck.exterra.API.gem.Gem;
import com.kauruck.exterra.ExTerraRegistries;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.util.Map;

public class GemStates extends JsonReloadListener {
    public static final String FOLDER = "gems/states";
    public static Gson GSON = (new GsonBuilder())
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public GemStates() {
        super(GSON, FOLDER);
    }




    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        objectIn.entrySet().stream()
        .
    }

    private void loadFromJson(String resourceLocation, JsonElement json){
        Gem gem = ExTerraRegistries.GEM_REGISTRY.get().getValue(new ResourceLocation(resourceLocation));
        if(gem == null)
            throw new IllegalArgumentException("GemType corresponding to: " + resourceLocation + " does not exits");
        gem.fromProperties();
    }
}
