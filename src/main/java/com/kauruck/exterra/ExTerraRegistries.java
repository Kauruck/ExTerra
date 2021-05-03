package com.kauruck.exterra;

import com.kauruck.exterra.API.gem.Gem;
import com.kauruck.exterra.API.tooltype.Tool;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class ExTerraRegistries {
    public static Supplier<IForgeRegistry<Gem>> GEM_REGISTRY;
    public static Supplier<IForgeRegistry<Tool>> TOOL_REGISTRY;

}
