package com.kauruck.exterra.crafting;

import com.kauruck.exterra.API.gem.Gem;
import com.kauruck.exterra.API.item.tool.BaseGemTool;
import com.kauruck.exterra.API.tooltype.Tool;
import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.modules.ExTerraTools;
import com.kauruck.exterra.util.ExTerraToolHelper;
import net.minecraft.item.*;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

/**
 * Helper for upgrading an Item to another
 *
 * @author Kauruck
 */
public class ToolUpgrading {

    /**
     * The current instance
     */
    public static final ToolUpgrading INSTANCE = new ToolUpgrading();

    //upgrade_blacklist
    private final ITag<Item> blacklist = loadBlacklist();

    private ITag<Item> loadBlacklist(){
        ResourceLocation blacklistLocation = ExTerra.getResource("crafting/upgrade_blacklist");
        return ItemTags.getCollection().get(blacklistLocation);
    }


    /**
     * Checks weather the Item can be upgraded.
     * This checks the blacklist tag "exterra:crafting/upgrade_blacklist" and
     * also the registry for
     * @see Tool
     * @param base The ItemStack to test
     * @return Weather it can be upgraded
     */
    public boolean isUpgradable(ItemStack base){
        if(blacklist != null && blacklist.contains(base.getItem()))
            return false;

        return ExTerraToolHelper.getToolUpgradeType(base.getItem()) != null;
    }

    /**
     * This upgrade, if possible, the base with the gem. If not possible this will return the base.
     * @param base The base Item (Tool) to upgrade
     * @param gem The GemType to apply to the base
     * @return The upgraded ItemStack or the base on
     */
    public ItemStack getUpgrade(ItemStack base, ItemStack gem){
        if(!isUpgradable(base))
            return base;

        Tool tools = ExTerraToolHelper.getToolUpgradeType(base.getItem());
        Gem gemType = ExTerraTools.GEM_ITEM_BINDING.get(gem);
        BaseGemTool tool = tools.getTool();
        return tool.forTool(base,gemType);
    }
}
