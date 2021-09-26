package com.kauruck.exterra;

import com.kauruck.exterra.client.ConnectedTextureGeometry;
import com.kauruck.exterra.modules.ExTerraCore;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ExTerraClient {

    public static void clientSetupEvent(final FMLClientSetupEvent e){
        e.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ExTerraCore.COMPOUND_FRAMED_GLASS.get(), RenderType.translucent());
        });
    }

    @SubscribeEvent
    public static void registerModelLoader(ModelRegistryEvent e){
        ModelLoaderRegistry.registerLoader(ExTerra.getResource("connected_textures"), ConnectedTextureGeometry.ConnectedTextureLoader.INSTANCE);
    }

}
