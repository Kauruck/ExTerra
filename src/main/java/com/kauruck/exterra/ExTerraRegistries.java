package com.kauruck.exterra;

import com.kauruck.exterra.API.gem.Gem;
import com.kauruck.exterra.API.tooltype.Tool;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

/**
 * Registries provided by ExTerra
 * @author Kauruck
 */
public class ExTerraRegistries {
    /**
     * Register all the gem types here
     */
    public static Supplier<IForgeRegistry<Gem>> GEM_REGISTRY;
    /**
     * Register all the tool types here
     */
    public static Supplier<IForgeRegistry<Tool>> TOOL_REGISTRY;

}
