package com.kauruck.exterra.util;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.ExTerraRegistries;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExTerra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryHandler {


    //Register gem textures
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerTextures(TextureStitchEvent.Pre event){
        ExTerraRegistries.GEM_REGISTRY.get().forEach(current -> event.addSprite(current.getTexture()));
    }





}
