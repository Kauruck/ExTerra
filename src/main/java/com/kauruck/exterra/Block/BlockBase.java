package com.kauruck.exterra.Block;

import com.kauruck.exterra.Util.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;

public class BlockBase extends Block {

    public BlockBase(String name, float hardness, Material material, ItemGroup itemGroup, ToolType harvestTool, int harvestLevel) {
        super(Block.Properties.create(material).hardnessAndResistance(hardness).harvestTool(harvestTool).harvestLevel(harvestLevel));
        RegistryHandler.BLOCKS.add(this);
        this.setRegistryName(name);
        RegistryHandler.ITEMS.add(new BlockItem(this, new Item.Properties().group(itemGroup)).setRegistryName(name));

    }


}
