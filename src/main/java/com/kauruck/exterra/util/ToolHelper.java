package com.kauruck.exterra.util;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ToolType;

/**
 * Helper for things Vanilla Tools
 *
 * @author Kauruck
 */
public class ToolHelper {
    /**
     * Checks weather the tool is valid for the block
     * @param tool The tool
     * @param state The block
     * @return Weather it is valid
     */
    public static boolean isValidTool(ItemStack tool, BlockState state){
        for(ToolType current : tool.getToolTypes()){
            if(state.isToolEffective(current))
                return true;
        }
        return false;
    }
}
