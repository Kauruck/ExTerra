package com.kauruck.exterra.datapacks;

import com.google.gson.*;
import com.kauruck.exterra.API.gem.Gem;
import com.kauruck.exterra.ExTerraRegistries;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

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

    public boolean contains(ItemStack provider) {
        return bindings.entrySet().stream()
                .anyMatch(current -> current.getKey().test(provider));
    }

    public Gem get(ItemStack provider) {
        return bindings.entrySet().stream()
                .filter(current -> current.getKey().test(provider))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    public boolean isValidForType(Gem gem, ItemStack provider) {
        return bindings.entrySet().stream()
                .anyMatch(current -> current.getKey().test(provider) && current.getValue() == gem);
    }

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
                .filter(current -> current.getLeft() != null && current.getRight() != null)
                .collect(Collectors.toMap(
                        Pair::getLeft,
                        Pair::getRight
                ));

        //Just for debugging
        //this.bindings.entrySet().forEach(current -> ExTerra.LOGGER.debug("Loaded binding: " + current.getKey().getRegistryName() + " -> " + current.getValue().getRegistryName()));

    }

    private Pair<Ingredient, Gem> loadFromJson(JsonObject json){
        return Pair.of(CraftingHelper.getIngredient(json.get("provider")), ExTerraRegistries.GEM_REGISTRY.get().getValue(GSON.fromJson(json.get("gemtype"), ResourceLocation.class))) ;
    }




    //Copied straight form TinkersConstruct https://github.com/SlimeKnights/TinkersConstruct/blob/a4161f0d2dd9689801675f6ff5b97c3dd13e3b8c/src/main/java/slimeknights/tconstruct/library/materials/MaterialManager.java#L206
    private static class ConditionSerializer implements JsonDeserializer<ICondition>, JsonSerializer<ICondition> {
        @Override
        public ICondition deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            return CraftingHelper.getCondition(JSONUtils.getJsonObject(json, "condition"));
        }

        @Override
        public JsonElement serialize(ICondition condition, Type type, JsonSerializationContext context) {
            return CraftingHelper.serialize(condition);
        }
    }
}

