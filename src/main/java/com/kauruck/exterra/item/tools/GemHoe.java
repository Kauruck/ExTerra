package com.kauruck.exterra.item.tools;

import com.kauruck.exterra.API.item.tool.BaseGemTool;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ToolType;

public class GemHoe extends BaseGemTool {

    @Override
    public boolean canHarvestBlock(BlockState state) {
        return state.getHarvestTool() == ToolType.HOE;
    }

    @Override
    public float getAttackDamage(ItemStack stack) {
        return this.getPatentsAttributeValue(stack, Attributes.ATTACK_DAMAGE).orElse(0D).floatValue();
    }
}
