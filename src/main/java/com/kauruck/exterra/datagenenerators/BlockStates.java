package com.kauruck.exterra.datagenenerators;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.modules.ExTerraCore;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStates extends BlockStateProvider {

    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, ExTerra.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlock(ExTerraCore.COMPOUND_BRICKS.get());
        this.stairsBlock((StairBlock) ExTerraCore.COMPOUND_BRICKS_STAIR.get(), ExTerra.getResource("block/compound_bricks"));
        this.slabBlock((SlabBlock) ExTerraCore.COMPOUND_BRICKS_SLAB.get(), ExTerra.getResource("block/compound_bricks"), ExTerra.getResource("block/compound_bricks"));
    }
}
