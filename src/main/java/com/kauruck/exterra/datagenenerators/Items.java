package com.kauruck.exterra.datagenenerators;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.modules.ExTerraCore;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class Items extends ItemModelProvider {

    public Items(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, ExTerra.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.singleTexture(getPathOf(ExTerraCore.TEST_ITEM.get()),
                new ResourceLocation("item/generated"),
                "layer0",
                ExTerra.getResource("item/test"));

        this.singleTexture(getPathOf(ExTerraCore.COMPOUND.get()),
                new ResourceLocation("item/generated"),
                "layer0",
                ExTerra.getResource("item/compound"));

        this.singleTexture(getPathOf(ExTerraCore.COMPOUND_BRICK.get()),
                new ResourceLocation("item/generated"),
                "layer0",
                ExTerra.getResource("item/compound_brick"));


        this.withExistingParent(getPathOf(ExTerraCore.COMPOUND_BRICKS.get()),
                ExTerra.getResource("block/compound_bricks"));

        this.withExistingParent(getPathOf(ExTerraCore.COMPOUND_BRICKS_SLAB.get()),
                ExTerra.getResource("block/compound_bricks_slab"));

        this.withExistingParent(getPathOf(ExTerraCore.COMPOUND_BRICKS_STAIR.get()),
                ExTerra.getResource("block/compound_bricks_stair"));

        this.withExistingParent(getPathOf(ExTerraCore.COMPOUND_FRAMED_GLASS.get()),
                ExTerra.getResource("block/compound_framed_glass"));

        this.singleTexture(getPathOf(ExTerraCore.CALCITE_DUST_ITEM.get()),
                new ResourceLocation("item/generated"),
                "layer0",
                ExTerra.getResource("item/calcite_dust"));

        this.singleTexture(getPathOf(ExTerraCore.RITUAL_MAP.get()),
                new ResourceLocation("item/generated"),
                "layer0",
                ExTerra.getResource("item/ritual_map"));

        this.singleTexture(getPathOf(ExTerraCore.RITUAL_STONE_ITEM.get()),
                new ResourceLocation("item/generated"),
                "layer0",
                ExTerra.getResource("item/ritual_stone_item"));

        this.singleTexture(getPathOf(ExTerraCore.RECEIVER_BLOCK_ITEM.get()),
                new ResourceLocation("item/generated"),
                "layer0",
                ExTerra.getResource("item/receiver_stone_item"));

        this.singleTexture(getPathOf(ExTerraCore.EMITTER_BLOCK_ITEM.get()),
                new ResourceLocation("item/generated"),
                "layer0",
                ExTerra.getResource("item/emitter_stone_item"));

        this.singleTexture(getPathOf(ExTerraCore.RITUAL_LENS.get()),
                new ResourceLocation("item/generated"),
                "layer0",
                ExTerra.getResource("item/debug_lens"));

    }

    private String getPathOf(Item item){
        return ForgeRegistries.ITEMS.getKey(item).getPath();
    }

    private String getPathOf(Block block){
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }
}
