package com.kauruck.exterra.datagenenerators;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.modules.ExTerraPower;
import net.minecraft.data.CachedOutput;
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
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class LootTables extends LootTableProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private final DataGenerator generator;
    Map<ResourceLocation, LootTable> tables = new HashMap<>();

    public LootTables(DataGenerator pGenerator) {
        super(pGenerator);
        this.generator = pGenerator;
    }

    @Override
    public void run(CachedOutput cache) {
        tables = new HashMap<>();

        registerDefaultForBlock(ExTerraCore.COMPOUND_BRICKS.get());
        registerDefaultForBlock(ExTerraCore.COMPOUND_BRICKS_SLAB.get());
        registerDefaultForBlock(ExTerraCore.COMPOUND_BRICKS_STAIR.get());
        registerDefaultForBlock(ExTerraCore.COMPOUND_FRAMED_GLASS.get());
        registerDefaultForBlock(ExTerraCore.CALCITE_DUST.get());


        writeTables(cache, tables);
    }

    private void registerDefaultForBlock(Block block){
        tables.put(block.getLootTable(), createDefaultTable(ForgeRegistries.BLOCKS.getKey(block).getPath(), block).setParamSet(LootContextParamSets.BLOCK).build());
    }

    protected LootTable.Builder createDefaultTable(String name, Block block){
        LootPool.Builder builder = LootPool.lootPool()
                .name(name)
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(block));
                /*.apply(SetContainerContents.setContents()
                        .withEntry(DynamicLoot.dynamicEntry(new ResourceLocation("minecraft", "contents")))
                );*/

        return LootTable.lootTable().withPool(builder);
    }

    private void writeTables(CachedOutput cache, Map<ResourceLocation, LootTable> tables){
        Path outputFolder = this.generator.getOutputFolder();
        tables.forEach((key, lootTable) -> {
            Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
            try{
                DataProvider.saveStable(cache, net.minecraft.world.level.storage.loot.LootTables.serialize(lootTable), path);
            } catch (IOException e){
                ExTerra.LOGGER.error("Couldn't write loot table {}", path, e);
            }
        });
    }
}
