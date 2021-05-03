package com.kauruck.exterra.item.tools;


import com.kauruck.exterra.API.item.tool.BaseGemTool;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.*;

public class GemSword extends BaseGemTool {

    @Override
    public float getAttackSpeed(ItemStack stack) {
        return  this.getPatentsAttributeValue(stack, Attributes.ATTACK_SPEED).orElse(0D).floatValue();
    }
}
