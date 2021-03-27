package com.kauruck.exterra.Tabs;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.Init.ModItems;
import com.kauruck.exterra.Item.GemType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ToolsTab extends ItemGroup {
    public ToolsTab() {
        super(ExTerra.MOD_ID + ".Tools");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModItems.GEM_PICKAXES[GemType.indexOf(GemType.Ruby)]);
    }
}
