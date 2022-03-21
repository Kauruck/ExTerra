package com.kauruck.exterra;

import com.kauruck.exterra.blockentityrenderer.DEBUGRitualStone;
import com.kauruck.exterra.client.ConnectedTextureGeometry;
import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.modules.ExTerraPower;
import com.kauruck.exterra.screens.GeneratorScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fmlclient.registry.ClientRegistry;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ExTerraClient {

    public static void clientSetupEvent(final FMLClientSetupEvent e){
        e.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ExTerraCore.COMPOUND_FRAMED_GLASS.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ExTerraCore.CALCITE_DUST.get(), RenderType.cutout());
        });
        MenuScreens.register(ExTerraPower.GENERATOR_CONTAINER.get(), GeneratorScreen::new);
        BlockEntityRenderers.register(ExTerraCore.RITUAL_STONE_ENTITY.get(), DEBUGRitualStone::new);
    }

    @SubscribeEvent
    public static void registerModelLoader(ModelRegistryEvent e){
        ModelLoaderRegistry.registerLoader(ExTerra.getResource("connected_textures"), ConnectedTextureGeometry.ConnectedTextureLoader.INSTANCE);
    }

}
