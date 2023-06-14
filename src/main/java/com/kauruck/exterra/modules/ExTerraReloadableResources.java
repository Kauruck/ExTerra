package com.kauruck.exterra.modules;

import com.kauruck.exterra.api.recipes.ExTerraRecipeManager;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ExTerraReloadableResources implements PreparableReloadListener{
    public static final Logger LOGGER = LogUtils.getLogger();


    public static ExTerraReloadableResources INSTANCE = new ExTerraReloadableResources();

    private final Map<ResourceLocation, Function<ICondition.IContext, ExTerraRecipeManager<?>>> allRecipeManager = new HashMap<>();

    private Map<ResourceLocation ,? extends ExTerraRecipeManager<?>> currentRecipeManager = new HashMap<>();

    private ICondition.IContext currentContext;

    public void setCurrentContext(ICondition.IContext newCurrentContext){
        this.currentContext = newCurrentContext;
    }

    public static <T> Supplier<ExTerraRecipeManager<T>> registerRecipeManger(ResourceLocation resourceLocation, Function<ICondition.IContext, ExTerraRecipeManager<T>> builder){
        INSTANCE.allRecipeManager.put(resourceLocation, builder::apply);
        return () -> INSTANCE.getRecipeManger(resourceLocation);
    }

    public <T> ExTerraRecipeManager<T> getRecipeManger(ResourceLocation location){
        return (ExTerraRecipeManager<T>) currentRecipeManager.get(location);
    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier pPreparationBarrier, ResourceManager pResourceManager, ProfilerFiller pPreparationsProfiler, ProfilerFiller pReloadProfiler, Executor pBackgroundExecutor, Executor pGameExecutor) {
        currentRecipeManager = allRecipeManager.keySet().stream()
                .map(current -> allRecipeManager.get(current).apply(currentContext))
                .collect(Collectors.toMap(
                        ExTerraRecipeManager::getId,
                        Function.identity()
                ));
        return CompletableFuture.allOf(
                currentRecipeManager.values().stream()
                        .map(current -> current.reload(pPreparationBarrier, pResourceManager, pPreparationsProfiler, pReloadProfiler, pBackgroundExecutor, pGameExecutor))
                        .toArray(CompletableFuture[]::new));
    }
}
