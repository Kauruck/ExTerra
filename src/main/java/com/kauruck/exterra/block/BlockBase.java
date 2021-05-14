package com.kauruck.exterra.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;

/**
 * Base block.
 * Just there so I do not need to work with the properties
 *
 * @author Kauruck
 */
public class BlockBase extends Block {

    /**
     * Create a basic block
     * @param hardness Hardness of the block
     * @param material The material of the block
     * @param harvestTool The tool needed to harvest the block
     * @param harvestLevel The level needed to harvest the block
     */
    public BlockBase(float hardness, Material material, ToolType harvestTool, int harvestLevel) {
        super(Block.Properties.create(material).harvestTool(harvestTool).harvestLevel(harvestLevel).hardnessAndResistance(hardness));
    }




}
