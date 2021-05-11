package com.kauruck.exterra.API.tooltype;

import com.kauruck.exterra.API.item.tool.BaseGemTool;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Supplier;

public class Tool extends ForgeRegistryEntry<Tool> implements Comparable<Tool>{

    private final Supplier<BaseGemTool> toolProvider;
    private final Class<? extends Item> parent;
    private final int priority;



    public Tool(Supplier<BaseGemTool> toolProvider, Class<? extends Item> parent, int priority){
        super();
        this.toolProvider = toolProvider;
        this.parent = parent;
        this.priority = priority;
    }

    public Tool(Supplier<BaseGemTool> toolProvider, Class<? extends Item> parent){
        this(toolProvider, parent, 0);
    }


    public Class<? extends Item> getParent(){
        return parent;
    }

    public BaseGemTool getTool(){
        return toolProvider.get();
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Tool o) {
        return Integer.compare(priority, o.getPriority());
    }
}
