package com.kauruck.exterra.datapacks;

import com.google.gson.*;
import com.kauruck.exterra.API.gem.Gem;
import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.ExTerraRegistries;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This one handles binding an Item (or multiple) to a Gem.
 * Locate in gems/providers
 *
 * It needs an CraftingProvider (something like an item, tag, etc.)
 * and a Gem
 *
 * @author Kauruck
 */
public class GemItemBinding extends JsonReloadListener {
    public static final String FOLDER = "gems/providers";
    public static Gson GSON = (new GsonBuilder())
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .registerTypeAdapter(ICondition.class, new ConditionSerializer())
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public GemItemBinding() {
        super(GSON, FOLDER);
    }

    private Map<Ingredient, Gem> bindings = Collections.emptyMap();

    /**
     * Weather we have a gem for that ItemStack
     * @param provider The ItemStack to test
     * @return Weather we have a binding
     */
    public boolean contains(ItemStack provider) {
        return bindings.entrySet().stream()
                .anyMatch(current -> current.getKey().test(provider));
    }

    /**
     * Returns the corresponding Gem for the ItemStack or null if it does not exists
     * @param provider The ItemStack
     * @return The GemType for that ItemStack
     */
    public Gem get(ItemStack provider) {
        return bindings.entrySet().stream()
                .filter(current -> current.getKey().test(provider))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * Weather the ItemStack is bound to the GemType
     * @param gem The GemType
     * @param provider The ItemStack which should be tested against
     * @return Weather the Stack is valid for the GemType
     */
    public boolean isValidForType(Gem gem, ItemStack provider) {
        return bindings.entrySet().stream()
                .anyMatch(current -> current.getKey().test(provider) && current.getValue() == gem);
    }

    /**
     * Get all Ingredients which can provide the GemType
     * @param gem The GemType
     * @return A Collection of Ingredients for that GemType
     */
    public Collection<Ingredient> getProviderForType(Gem gem) {
        return bindings.entrySet().stream()
                .filter(current -> current.getValue().equals(gem))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }



    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        this.bindings = objectIn.values().stream()
                .filter(JsonElement::isJsonObject)
                .map(jsonElement -> loadFromJson(jsonElement.getAsJsonObject()))
                .filter(Objects::nonNull)
                .filter(current -> current.getLeft() != null && current.getRight() != null)
                .collect(Collectors.toMap(
                        Pair::getLeft,
                        Pair::getRight
                ));

        //Just for debugging
        //this.bindings.entrySet().forEach(current -> ExTerra.LOGGER.debug("Loaded binding: " + current.getKey().getRegistryName() + " -> " + current.getValue().getRegistryName()));

    }

    private Pair<Ingredient, Gem> loadFromJson(JsonObject json){
        Gem gemType = ExTerraRegistries.GEM_REGISTRY.get().getValue(GSON.fromJson(json.get("gemtype"), ResourceLocation.class));
        Ingredient ingredient =  CraftingHelper.getIngredient(json.get("provider"));
        if(gemType == null) {
            ExTerra.LOGGER.warn("GemType corresponding to: " + json.get("gemtype") + " does not exits");
            return null;
        }
        return Pair.of(ingredient, gemType);
    }


}

