package com.kauruck.exterra.datapacks;

import com.kauruck.exterra.ExTerra;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExTerra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DataPackEvents {

    @SubscribeEvent
    public static void addDataPackListeners(AddReloadListenerEvent event){
        event.addListener(DataPackProviders.GEM_ITEM_BINDING);
    }
}
