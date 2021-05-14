package com.kauruck.exterra.API.tooltype;

import com.kauruck.exterra.API.item.tool.BaseGemTool;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Supplier;

/**
 * A handler for upgrading a tool
 * Register this to the ExTerra Registry
 * @see com.kauruck.exterra.ExTerraRegistries
 *
 * @author Kauruck
 */
public class Tool extends ForgeRegistryEntry<Tool> implements Comparable<Tool>{

    private final Supplier<BaseGemTool> toolProvider;
    private final Class<? extends Item> parent;
    private final int priority;


    /**
     * Create a new tool handler
     * @param toolProvider The item for the tool
     * @param parent The class of the parent item
     * @param priority The priority of the handler. Higher priority means it will be used in favour of a lower on.
     */
    public Tool(Supplier<BaseGemTool> toolProvider, Class<? extends Item> parent, int priority){
        super();
        this.toolProvider = toolProvider;
        this.parent = parent;
        this.priority = priority;
    }

    /**
     * Create a new tool handler. The priority will be 0.
     * @param toolProvider The item for the tool
     * @param parent The class of the parent item
     */
    public Tool(Supplier<BaseGemTool> toolProvider, Class<? extends Item> parent){
        this(toolProvider, parent, 0);
    }


    /**
     * Get the class of the parent item
     * @return The class of the parent item
     */
    public Class<? extends Item> getParent(){
        return parent;
    }

    /**
     * Get the item for the tool
     * @return The item
     */
    public BaseGemTool getTool(){
        return toolProvider.get();
    }

    /**
     * Get the priority
     * @return The priority
     */
    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Tool o) {
        return Integer.compare(priority, o.getPriority());
    }
}
