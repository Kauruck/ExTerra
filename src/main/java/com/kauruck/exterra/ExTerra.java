package com.kauruck.exterra;

import com.kauruck.exterra.modules.ExTerraGems;
import com.kauruck.exterra.modules.ExTerraModules;
import com.kauruck.exterra.modules.ExTerraTools;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * The main class for ExTerra.
 *
 * @author Kauruck
 */
@Mod("exterra")
public class ExTerra
{
    /**
     * The mod id
     */
    public static final String MOD_ID = "exterra";

    /**
     * The logger for ExTerra
     */
    public static final Logger LOGGER = LogManager.getLogger();

    /**
     * Creates the mod (only Forge should calle this)
     */
    public ExTerra() {

        ExTerraModules.createRegistries();

        //Here are all the modules
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.register(new ExTerraGems());
        bus.register(new ExTerraTools());

        ExTerraModules.register();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);



    }

    /**
     * Get a resource for ExTerra
     * @param name The name of the resource
     * @return The ResourceLocation with the ModId ExTerra and the path with the name
     */
    public static ResourceLocation getResource(String name) {
        return new ResourceLocation(MOD_ID, name);
    }
}
