package com.kauruck.exterra.item.tools;

import com.kauruck.exterra.API.item.tool.BaseGemTool;
import net.minecraft.block.BlockState;
import net.minecraftforge.common.ToolType;

public class GemAxe extends BaseGemTool {

    @Override
    public boolean canHarvestBlock(BlockState state) {
        return state.getHarvestTool() == ToolType.AXE;
    }
}
