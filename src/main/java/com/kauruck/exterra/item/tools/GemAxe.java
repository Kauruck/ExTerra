package com.kauruck.exterra.item.tools;

import com.kauruck.exterra.API.item.tool.BaseGemTool;
import net.minecraft.block.BlockState;
import net.minecraftforge.common.ToolType;

/**
 * The enemy of any tree (or mob)
 *
 *  @author Kauruck
 */
public class GemAxe extends BaseGemTool {

    @Override
    public boolean canHarvestBlock(BlockState state) {
        return state.getHarvestTool() == ToolType.AXE;
    }
}
