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


// The value here should match an entry in the META-INF/mods.toml file
@Mod("exterra")
public class ExTerra
{
    public static final String MOD_ID = "exterra";

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

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

    public static ResourceLocation getResource(String name) {
        return new ResourceLocation(MOD_ID, name);
    }
}
