package com.kauruck.exterra.client;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.ExTerraRegistries;
import com.kauruck.exterra.client.models.GemToolLoader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


/**
 * This one handles the client events
 */
@Mod.EventBusSubscriber(modid= ExTerra.MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD, value= Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class ExTerraClientEvents {

    //Register model loader to Forge
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    static void registerModels(ModelRegistryEvent event) {
        //Gem tool (exterra:gem_tool)
        ModelLoaderRegistry.registerLoader(ExTerra.getResource("gem_tool"), GemToolLoader.Loader.INSTANCE);

    }

    //Register gem textures
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerTextures(TextureStitchEvent.Pre event){
        ExTerraRegistries.GEM_REGISTRY.get().forEach(current -> event.addSprite(current.getTexture()));
    }
}
