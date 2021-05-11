package com.kauruck.exterra.datapacks;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.modules.ExTerraTools;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handle things with DataPacks
 * This has to be extracted from the
 * @see ExTerraTools
 * because we need a different Eventbus
 *
 *  @author Kauruck
 */
@Mod.EventBusSubscriber(modid = ExTerra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DataPackEvents {

    //Add Listeners for reloading the DataPacks sources
    @SubscribeEvent
    public static void addDataPackListeners(AddReloadListenerEvent event){
        event.addListener(ExTerraTools.GEM_ITEM_BINDING);
    }
}
