package com.kauruck.exterra.item.tools;

import com.kauruck.exterra.API.item.tool.BaseGemTool;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ToolType;

/**
 * To mine stone and other
 *
 *  @author Kauruck
 */
public class GemPickaxe extends BaseGemTool {

    @Override
    public boolean canHarvestBlock(BlockState state) {
        return state.getHarvestTool() == ToolType.PICKAXE;
    }

    @Override
    public float getAttackSpeed(ItemStack stack) {
        return this.getPatentsAttributeValue(stack, Attributes.ATTACK_SPEED).orElse(0D).floatValue();
    }
}
