package com.kauruck.exterra.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;

public class BlockBase extends Block {

    public BlockBase(float hardness, Material material, ItemGroup itemGroup, ToolType harvestTool, int harvestLevel) {
        super(Block.Properties.create(material).harvestTool(harvestTool).harvestLevel(harvestLevel).hardnessAndResistance(hardness));
    }




}
