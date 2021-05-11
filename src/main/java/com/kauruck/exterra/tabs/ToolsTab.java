package com.kauruck.exterra.tabs;

import com.kauruck.exterra.API.item.tool.BaseGemTool;
import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.modules.ExTerraGems;
import com.kauruck.exterra.modules.ExTerraTools;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * The class providing the ToolTab
 *
 * @author Kauruck
 */
public class ToolsTab extends ItemGroup {
    public ToolsTab() {
        super(ExTerra.MOD_ID + ".Tools");
    }

    @Override
    public ItemStack createIcon() {
        return ((BaseGemTool)ExTerraTools.TOOL_PICKAXE_ITEM.get()).forTool(Items.IRON_PICKAXE.getDefaultInstance(), ExTerraGems.RUBY.get());
    }
}
