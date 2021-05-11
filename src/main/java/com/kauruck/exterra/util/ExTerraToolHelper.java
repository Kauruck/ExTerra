package com.kauruck.exterra.util;

import com.kauruck.exterra.API.tooltype.Tool;
import com.kauruck.exterra.ExTerraRegistries;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Helper for tools provided by ExTerra
 *
 * @author Kauruck
 */
public class ExTerraToolHelper {

    /**
     * Returns the tool type with the highest priority for a given item.
     * Tool type needs to be registered beforehand.
     * @see ExTerraRegistries
     *
     * This returns null if the tool is not registerd or the item is null
     * @param item The item that should be upgrade
     * @return The tool type or null
     */
    public static Tool getToolUpgradeType(Item item){
        if(item == null)
            return null;
        IForgeRegistry<Tool> tools = ExTerraRegistries.TOOL_REGISTRY.get();
        return tools.getValues().stream()
                .filter(current -> current.getParent().equals(item.getClass()))
                .sorted()
                .findFirst()
                .orElse(null);
    }



}
