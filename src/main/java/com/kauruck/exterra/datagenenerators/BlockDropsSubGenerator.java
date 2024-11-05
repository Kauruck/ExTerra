package com.kauruck.exterra.datagenenerators;

import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.modules.RegistryManger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class BlockDropsSubGenerator extends BlockLootSubProvider {


    protected BlockDropsSubGenerator(HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return RegistryManger.BLOCK_REGISTRY.getEntries()
                .stream()
                .map(block -> (Block) block.value())
                .toList();
    }

    @Override
    protected void generate() {
        dropSelf(ExTerraCore.COMPOUND_BRICKS.get());
        dropSelf(ExTerraCore.COMPOUND_BRICKS_SLAB.get());
        dropSelf(ExTerraCore.COMPOUND_BRICKS_STAIR.get());
        dropSelf(ExTerraCore.COMPOUND_FRAMED_GLASS.get());
        dropSelf(ExTerraCore.CALCITE_DUST.get());
        dropSelf(ExTerraCore.RITUAL_STONE.get());
        dropSelf(ExTerraCore.EMITTER_BLOCK.get());
        dropSelf(ExTerraCore.RECEIVER_BLOCK.get());
    }
}
