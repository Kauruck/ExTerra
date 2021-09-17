package com.kauruck.exterra.datagenenerators;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.modules.ExTerraPower;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class LootTables extends LootTableProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private final DataGenerator generator;

    public LootTables(DataGenerator pGenerator) {
        super(pGenerator);
        this.generator = pGenerator;
    }

    @Override
    public void run(HashCache cache) {
        Map<ResourceLocation, LootTable> tables = new HashMap<>();

        tables.put(ExTerraPower.GENERATOR.get().getLootTable(), createGeneratorTable("generator", ExTerraPower.GENERATOR.get()).setParamSet(LootContextParamSets.BLOCK).build());
        tables.put(ExTerraCore.COMPOUND_BRICKS.get().getLootTable(), createDefaultTable("mud_bricks", ExTerraCore.COMPOUND_BRICKS.get()).setParamSet(LootContextParamSets.BLOCK).build());


        writeTables(cache, tables);
    }

    protected LootTable.Builder createDefaultTable(String name, Block block){
        LootPool.Builder builder = LootPool.lootPool()
                .name(name)
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(block))
                .apply(SetContainerContents.setContents()
                        .withEntry(DynamicLoot.dynamicEntry(new ResourceLocation("minecraft", "contents")))
                );

        return LootTable.lootTable().withPool(builder);
    }

    protected LootTable.Builder createGeneratorTable(String name, Block block){
        LootPool.Builder builder = LootPool.lootPool()
                .name(name)
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(block)
                        .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                        .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                .copy("inv", "BlockEntityTab.inv", CopyNbtFunction.MergeStrategy.REPLACE)
                                .copy("energy", "BlockEntityTag.energy", CopyNbtFunction.MergeStrategy.REPLACE))
                        .apply(SetContainerContents.setContents()
                                .withEntry(DynamicLoot.dynamicEntry(new ResourceLocation("minecraft", "contents"))))
                );

        return LootTable.lootTable().withPool(builder);
    }

    private void writeTables(HashCache cache, Map<ResourceLocation, LootTable> tables){
        Path outputFolder = this.generator.getOutputFolder();
        tables.forEach((key, lootTable) -> {
            Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
            try{
                DataProvider.save(GSON, cache, net.minecraft.world.level.storage.loot.LootTables.serialize(lootTable), path);
            } catch (IOException e){
                ExTerra.LOGGER.error("Couldn't write loot table {}", path, e);
            }
        });
    }
}
