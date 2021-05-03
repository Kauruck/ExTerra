package com.kauruck.exterra.API.tooltype;

import com.kauruck.exterra.API.item.tool.BaseGemTool;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Supplier;

public class Tool extends ForgeRegistryEntry<Tool> {

    private final Supplier<BaseGemTool> toolProvider;
    private final Class<? extends Item> parent;


    public Tool(Supplier<BaseGemTool> toolProvider, Class<? extends Item> parent){
        super();
        this.toolProvider = toolProvider;
        this.parent = parent;
    }


    public Class<? extends Item> getParent(){
        return parent;
    }

    public BaseGemTool getTool(){
        return toolProvider.get();
    }
}
