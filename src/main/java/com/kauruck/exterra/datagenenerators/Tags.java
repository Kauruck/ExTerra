package com.kauruck.exterra.datagenenerators;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.modules.ExTerraCore;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class Tags extends BlockTagsProvider {

    public Tags(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, ExTerra.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ExTerraCore.COMPOUND_BRICKS.get());
        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ExTerraCore.COMPOUND_BRICKS.get());
    }
}
