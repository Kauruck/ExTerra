package com.kauruck.exterra.api.recipes;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.*;
import com.kauruck.exterra.modules.ExTerraRegistries;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExTerraRecipeManager<T> extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogUtils.getLogger();

    private Map<ExTerraRecipeType<? extends  ExTerraRecipe<T, ?>>, Map<ResourceLocation, ExTerraRecipe<T, ?>>> recipes = ImmutableMap.of();
    private Map<ResourceLocation, ExTerraRecipe<T, ?>> byName = ImmutableMap.of();
    private final ICondition.IContext context;

    private final ResourceLocation id;

    public ExTerraRecipeManager(ICondition.IContext context, String directory, ResourceLocation id){
        super(GSON, directory);
        this.context = context;
        this.id = id;
    }

    public ResourceLocation getId() {
        return id;
    }

    public void apply(Map<ResourceLocation, JsonElement> elements, ResourceManager resourceManager, ProfilerFiller profiler){
        Map<ExTerraRecipeType<? extends ExTerraRecipe<T, ?>>, ImmutableMap.Builder<ResourceLocation, ExTerraRecipe<T, ?>>> loadedRecipes = Maps.newHashMap();
        ImmutableMap.Builder<ResourceLocation, ExTerraRecipe<T, ?>> builder = ImmutableMap.builder();
        for(Map.Entry<ResourceLocation, JsonElement> entry : elements.entrySet()){
            ResourceLocation resourcelocation = entry.getKey();
            if (resourcelocation.getPath().startsWith("_")) continue; // Skip anything that starts with _. That is just metadata
            try {
                if (entry.getValue().isJsonObject() && !CraftingHelper.processConditions(entry.getValue().getAsJsonObject(), "conditions", this.context)) {
                    LOGGER.debug("Skipping loading recipe {} as it's conditions were not met", resourcelocation);
                    continue;
                }
                ExTerraRecipe<T, ?> recipe = fromJson(resourcelocation, GsonHelper.convertToJsonObject(entry.getValue(), "top element"), this.context);
                if (recipe == null) {
                    LOGGER.info("Skipping loading recipe {} as it's serializer returned null", resourcelocation);
                    continue;
                }
                loadedRecipes.computeIfAbsent(recipe.getType(), (ignored) -> ImmutableMap.builder())
                        .put(resourcelocation, recipe);

                builder.put(resourcelocation, recipe);
            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                LOGGER.error("Parsing error loading recipe {}", resourcelocation, jsonparseexception);
            }
        }

        this.recipes = loadedRecipes.entrySet().stream()
                .collect(ImmutableMap.toImmutableMap(
                        Map.Entry::getKey,
                        (value) -> value.getValue().build()));
        this.byName = builder.build();
        LOGGER.info("Loaded {} recipes", (int)loadedRecipes.size());
    }

    /**
     * Use this to check if the recipe is valid after it is loaded
     * @param recipe The recipe to check
     * @return Weather it is valid or not
     */
    protected boolean isValidRecipe(ExTerraRecipe<T, ?> recipe){
        return true;
    }

    public <C extends ExTerraRecipeContainer<T>, R extends ExTerraRecipe<T, C>> Optional<R> getRecipeFor(ExTerraRecipeType<R> pRecipeType, C container, Level pLevel) {
        return this.byType(pRecipeType).values().stream()
                .filter((current) -> current.matches(container, pLevel))
                .findFirst();
    }

    public <C extends ExTerraRecipeContainer<T>, R extends ExTerraRecipe<T, C>> Optional<Pair<ResourceLocation, R>> getRecipeFor(ExTerraRecipeType<R> type, C container, Level level, @Nullable ResourceLocation resourceLocation) {
        Map<ResourceLocation, R> map = this.byType(type);
        if (resourceLocation != null) {
            R r = map.get(resourceLocation);
            if (r != null && r.matches(container, level)) {
                return Optional.of(Pair.of(resourceLocation, r));
            }
        }

        return map.entrySet().stream()
                .filter((current) -> current.getValue().matches(container, level)).findFirst()
                .map((current) -> Pair.of(current.getKey(), current.getValue()));
    }

    public <C extends ExTerraRecipeContainer<T>, R extends ExTerraRecipe<T, C>> List<R> getAllRecipesFor(ExTerraRecipeType<R> pRecipeType) {
        return List.copyOf(this.byType(pRecipeType).values());
    }

    public <C extends ExTerraRecipeContainer<T>, R extends ExTerraRecipe<T, C>> List<R> getRecipesFor(ExTerraRecipeType<R> pRecipeType, C pInventory, Level pLevel) {
        // TODO: Maybe sort the List by something, so that it is deterministic
        return this.byType(pRecipeType).values().stream()
                .filter((current) -> current.matches(pInventory, pLevel))
                .collect(Collectors.toList());
    }

    private <R extends ExTerraRecipe<T, ?>> Map<ResourceLocation, R> byType(ExTerraRecipeType<R> pRecipeType) {
        return (Map<ResourceLocation, R>) this.recipes.getOrDefault(pRecipeType, Collections.emptyMap());
    }

    public <C extends ExTerraRecipeContainer<T>, R extends ExTerraRecipe<T, C>> NonNullList<T> getRemainderFor(ExTerraRecipeType<R> pRecipeType, C container, Level pLevel) {
        Optional<R> optional = this.getRecipeFor(pRecipeType, container, pLevel);
        if (optional.isPresent()) {
            return optional.get().getRemainder(container);
        } else {
            NonNullList<T> nonnulllist = NonNullList.withSize(container.getSize(), container.getEmpty());
            int i = 0;
            for(T current : container) {
                nonnulllist.set(i, current);
            }

            return nonnulllist;
        }
    }

    public Optional<? extends ExTerraRecipe<T, ?>> byKey(ResourceLocation pRecipeId) {
        return Optional.ofNullable(this.byName.get(pRecipeId));
    }

    public Collection<ExTerraRecipe<T, ?>> getRecipes() {
        return this.recipes.values().stream()
                .flatMap((current) -> current.values().stream())
                .collect(Collectors.toSet());
    }

    public Stream<ResourceLocation> getRecipeIds() {
        return this.recipes.values().stream()
                .flatMap((current) -> current.keySet().stream());
    }

    public <C extends ExTerraRecipeContainer<T>> ExTerraRecipe<T, C> fromJson(ResourceLocation pRecipeId, JsonObject pJson, net.minecraftforge.common.crafting.conditions.ICondition.IContext context) {
        String s = GsonHelper.getAsString(pJson, "type");
        ExTerraRecipeSerializer<? extends ExTerraRecipe<T, C>> serializer = (ExTerraRecipeSerializer<? extends ExTerraRecipe<T, C>>) ExTerraRegistries.RECIPE_SERIALIZER.get().getValue(new ResourceLocation(s));
        if(serializer == null){
            LOGGER.error("No serializer for type: " + s);
            return null;
        }
        return serializer.fromJson(pRecipeId, pJson, context);
    }
}
