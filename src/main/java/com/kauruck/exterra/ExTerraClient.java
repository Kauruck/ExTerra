package com.kauruck.exterra;

import com.kauruck.exterra.renderers.GemToolRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=ExTerra.MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD, value= Dist.CLIENT)
public class ExTerraClient {

    @SubscribeEvent
    static void registerModels(ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(ExTerra.getResource("gem_tool"), GemToolRenderer.Loader.INSTANCE);

    }
}
