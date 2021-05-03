package com.kauruck.exterra.crafting;

import com.kauruck.exterra.API.gem.Gem;
import com.kauruck.exterra.API.item.tool.BaseGemTool;
import com.kauruck.exterra.API.tooltype.Tool;
import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.datapacks.DataPackProviders;
import com.kauruck.exterra.util.GemHelper;
import net.minecraft.item.*;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class ToolUpgrading {

    //upgrade_blacklist
    private final ITag<Item> blacklist = loadBlacklist();

    private ITag<Item> loadBlacklist(){
        ResourceLocation blacklistLocation = ExTerra.getResource("crafting/upgrade_blacklist");
        return ItemTags.getCollection().get(blacklistLocation);
    }


    public boolean isUpgradable(ItemStack base){
        if(blacklist != null && blacklist.contains(base.getItem()))
            return false;

        return GemHelper.getToolUpgradeType(base.getItem()) != null;
    }

    public ItemStack getUpgrade(ItemStack base, ItemStack gem){
        if(!isUpgradable(base))
            return base;

        Tool tools = GemHelper.getToolUpgradeType(base.getItem());
        Gem gemType = DataPackProviders.GEM_ITEM_BINDING.get(gem);
        BaseGemTool tool = tools.getTool();
        return tool.forTool(base,gemType);
    }
}
