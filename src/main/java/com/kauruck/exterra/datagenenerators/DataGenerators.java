package com.kauruck.exterra.datagenenerators;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new RecipesGenerator(output, lookup));
        generator.addProvider(event.includeServer(), new LootTableProvider(output, Set.of(),
                List.of(
                        new LootTableProvider.SubProviderEntry(BlockDropsSubGenerator::new, LootContextParamSets.BLOCK)
                ),
                lookup));
        generator.addProvider(event.includeServer(), new TagsGenerators(output, lookup, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer() || event.includeClient(), new Shapes(output, lookup));
        generator.addProvider(event.includeServer(), new ConversionRecipesGenerator(output, lookup));

        generator.addProvider(event.includeClient(), new BlockStates(output, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new Items(output, event.getExistingFileHelper()));
    }

}
