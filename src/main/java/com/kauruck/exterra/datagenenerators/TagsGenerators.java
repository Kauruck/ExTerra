package com.kauruck.exterra.datagenenerators;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.modules.ExTerraTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TagsGenerators extends BlockTagsProvider {

    public TagsGenerators(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ExTerra.MOD_ID ,existingFileHelper);
    }


    private void tagDiamondPickaxe(Block block){
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(block);
        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(block);
    }

    private void tagIronPickaxe(Block block){
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(block);
        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(block);
    }

    private void tagStonePickaxe(Block block){
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(block);
        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(block);
    }

    private void tagWoodPickaxe(Block block){
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(block);
        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(block);
    }


    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tagWoodPickaxe(ExTerraCore.COMPOUND_FRAMED_GLASS.get());
        this.tagWoodPickaxe(ExTerraCore.COMPOUND_BRICKS_SLAB.get());
        this.tagWoodPickaxe(ExTerraCore.COMPOUND_BRICKS_STAIR.get());
        this.tagWoodPickaxe(ExTerraCore.COMPOUND_BRICKS.get());
        this.tag(ExTerraTags.RITUAL_TIER_I).add(Blocks.CANDLE);
        this.tag(ExTerraTags.MATTER_WIRE).add(ExTerraCore.CALCITE_DUST.get());
    }
}
