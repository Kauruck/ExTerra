package com.kauruck.exterra.data;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.modules.ExTerraReloadableResources;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExTerra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DataEventHandler {

    @SubscribeEvent
    public static void DataPackReloadEvent(AddReloadListenerEvent event){
        event.addListener(ExTerraReloadableResources.INSTANCE);
    }
}
