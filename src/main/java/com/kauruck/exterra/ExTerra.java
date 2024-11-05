package com.kauruck.exterra;

import com.kauruck.exterra.commands.ExTerraCommands;
import com.kauruck.exterra.data.DataEventHandler;
import com.kauruck.exterra.datagenenerators.DataGenerators;
import com.kauruck.exterra.modules.*;
import com.kauruck.exterra.networking.ExTerraNetworking;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod("exterra")
public class ExTerra {
    //Logger
    public static final Logger LOGGER = LogManager.getLogger();

    //Constants
    public static final String MOD_ID = "exterra";

    public ExTerra(IEventBus bus) {
        //Make the registries
        bus.register(ExTerraRegistries.class);
        // Data Gen
        bus.register(DataGenerators.class);
        //Modules
        new ExTerraCore();
        new ExTerraPower();

        // Network Inbuilts
        new NetworkInbuilt();

        // Client
        bus.register(ExTerraClient.class);

        RegistryManger.doRegistry(bus);
    }

    public static ResourceLocation getResource(String path){
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }


}
