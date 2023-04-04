package com.kauruck.exterra.datagenenerators;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.modules.ExTerraPower;
import com.kauruck.exterra.modules.ExTerraTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class Tags extends BlockTagsProvider {

    public Tags(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, ExTerra.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tagWoodPickaxe(ExTerraCore.COMPOUND_FRAMED_GLASS.get());
        this.tagWoodPickaxe(ExTerraCore.COMPOUND_BRICKS_SLAB.get());
        this.tagWoodPickaxe(ExTerraCore.COMPOUND_BRICKS_STAIR.get());
        this.tagWoodPickaxe(ExTerraCore.COMPOUND_BRICKS.get());
        this.tag(ExTerraTags.RITUAL_TIER_I).add(Blocks.CANDLE);
        this.tag(ExTerraTags.MATTER_WIRE).add(ExTerraCore.CALCITE_DUST.get());
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


}
