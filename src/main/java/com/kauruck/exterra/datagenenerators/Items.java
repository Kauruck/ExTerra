package com.kauruck.exterra.datagenenerators;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.modules.ExTerraCore;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class Items extends ItemModelProvider {

    public Items(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, ExTerra.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.singleTexture(ExTerraCore.TEST_ITEM.get().getRegistryName().getPath(),
                new ResourceLocation("item/generated"),
                "layer0",
                ExTerra.getResource("item/test"));

        this.singleTexture(ExTerraCore.COMPOUND.get().getRegistryName().getPath(),
                new ResourceLocation("item/generated"),
                "layer0",
                ExTerra.getResource("item/compound"));

        this.singleTexture(ExTerraCore.COMPOUND_BRICK.get().getRegistryName().getPath(),
                new ResourceLocation("item/generated"),
                "layer0",
                ExTerra.getResource("item/compound_brick"));

        /*this.withExistingParent(ExTerraPower.GENERATOR.get().getRegistryName().getPath(),
                ExTerra.getResource("block/generator"));*/

        this.withExistingParent(ExTerraCore.COMPOUND_BRICKS.get().getRegistryName().getPath(),
                ExTerra.getResource("block/compound_bricks"));

        this.withExistingParent(ExTerraCore.COMPOUND_BRICKS_SLAB.get().getRegistryName().getPath(),
                ExTerra.getResource("block/compound_bricks_slab"));

        this.withExistingParent(ExTerraCore.COMPOUND_BRICKS_STAIR.get().getRegistryName().getPath(),
                ExTerra.getResource("block/compound_bricks_stair"));

        this.withExistingParent(ExTerraCore.COMPOUND_FRAMED_GLASS.get().getRegistryName().getPath(),
                ExTerra.getResource("block/compound_framed_glass"));
    }
}
