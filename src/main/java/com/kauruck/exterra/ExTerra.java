package com.kauruck.exterra;

import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.modules.ExTerraPower;
import com.kauruck.exterra.modules.RegistryManger;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod("exterra")
public class ExTerra
{
    //Logger
    public static final Logger LOGGER = LogManager.getLogger();

    //Constants
    public static final String MOD_ID = "exterra";

    public ExTerra() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.register(new ExTerraCore());
        bus.register(new ExTerraPower());

        RegistryManger.doRegistry();
    }

    public static ResourceLocation getResource(String path){
        return new ResourceLocation(MOD_ID, path);
    }


}
