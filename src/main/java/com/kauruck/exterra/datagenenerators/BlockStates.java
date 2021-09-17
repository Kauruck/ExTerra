package com.kauruck.exterra.datagenenerators;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.modules.ExTerraCore;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStates extends BlockStateProvider {

    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, ExTerra.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlock(ExTerraCore.COMPOUND_BRICKS.get());
    }
}
