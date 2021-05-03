package com.kauruck.exterra.util;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ToolType;

public class ToolHelper {
    public static boolean isValidTool(ItemStack tool, BlockState state){
        for(ToolType current : tool.getToolTypes()){
            if(state.isToolEffective(current))
                return true;
        }
        return false;
    }
}
