package com.kauruck.exterra.Init;

import com.kauruck.exterra.Block.BlockBase;
import com.kauruck.exterra.Block.Machines.GemWorkbench;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class ModBlocks {


        public static final Block RUBY_BLOCK = new BlockBase("ruby_block", 5, Material.IRON, ModTabs.RESOURCES, ToolType.PICKAXE, 2);

        public static final Block RUBY_ORE = new BlockBase("ruby_ore", 3, Material.IRON, ModTabs.RESOURCES, ToolType.PICKAXE, 2);

        //Machines
        public static final Block GEM_WORKBENCH = new GemWorkbench("gem_workbench", 3, Material.IRON, ModTabs.RESOURCES, ToolType.PICKAXE, 2);

}
