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

/**
 * This one handles the stats for a gem
 * Locate in gems/stats
 *
 * Its name and folder to the corresponds the gem.
 * The following things can be defined:
 *   priority: Higher priority can override lower one [-1]
 *   miningSpeedModifier: How much faster the upgraded tool can mine [0]
 *   attackSpeedModifier: How much faster the upgraded tool can attack [0]
 *   attackDamageModifier: How much damage the upgraded tool deals more [0]
 *   maxDamageModifier: How much longer the upgraded tool lasts [0]
 *   enchantebilityModifier: How likelier the upgraded tool is to receive better enchantments
 *
 * @author Kauruck
 */
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

    /**
     * Try to load the given json as stats for the gem
     * @param resourceLocation The resource location of the gem
     * @param json The json (needs to be an jsonObject)
     */
    private void loadFromJson(ResourceLocation resourceLocation, JsonElement json){
        Gem gem = ExTerraRegistries.GEM_REGISTRY.get().getValue(resourceLocation);
        if(gem == null) {
            ExTerra.LOGGER.warn("GemType corresponding to: " + resourceLocation + " does not exits");
            return;
        }
        gem.fromProperties(GSON.fromJson(json, Gem.Properties.class));
    }
}
