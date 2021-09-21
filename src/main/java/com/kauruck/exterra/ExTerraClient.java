package com.kauruck.exterra;

import com.kauruck.exterra.modules.ExTerraCore;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class ExTerraClient {

    public static void clientSetupEvent(final FMLClientSetupEvent e){
        e.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ExTerraCore.COMPOUND_FRAMED_GLASS.get(), RenderType.translucent());
        });
    }




}
