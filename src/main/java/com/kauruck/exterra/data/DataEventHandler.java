package com.kauruck.exterra.data;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.modules.ExTerraReloadableResources;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;


@EventBusSubscriber(modid = ExTerra.MOD_ID)
public class DataEventHandler {

    @SubscribeEvent
    public static void DataPackReloadEvent(AddReloadListenerEvent event){
        event.addListener(ExTerraReloadableResources.INSTANCE);
    }
}
