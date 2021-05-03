package com.kauruck.exterra.util;

import com.kauruck.exterra.API.gem.Gem;
import com.kauruck.exterra.API.tooltype.Tool;
import com.kauruck.exterra.ExTerraRegistries;
import com.kauruck.exterra.datapacks.DataPackProviders;
import net.minecraft.item.Item;
import net.minecraft.util.RegistryKey;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Map;

public class GemHelper {

    public static Tool getToolUpgradeType(Item item){
        if(item == null)
            return null;
        IForgeRegistry<Tool> tools = ExTerraRegistries.TOOL_REGISTRY.get();
        return tools.getValues().stream()
                .filter(current -> current.getParent().equals(item.getClass()))
                .findFirst()
                .orElse(null);
    }

    public static Gem getGemFromItem(Item item){
        return DataPackProviders.GEM_ITEM_BINDING.get(item.getDefaultInstance());
    }

}
