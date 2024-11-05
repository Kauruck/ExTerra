package com.kauruck.exterra;

import com.kauruck.exterra.blockentityrenderer.MatterReceiverEntityRenderer;
import com.kauruck.exterra.blockentityrenderer.RitualStoneBlockEntityRender;
import com.kauruck.exterra.client.model.ConnectedTextureGeometry;
import com.kauruck.exterra.modules.ExTerraCore;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

@OnlyIn(Dist.CLIENT)
public class ExTerraClient {

    @SubscribeEvent
    public static void clientSetupEvent(final FMLClientSetupEvent e){
        e.enqueueWork(() -> {
            //TODO Find a way to set this in json. Maybe?
            ItemBlockRenderTypes.setRenderLayer(ExTerraCore.CALCITE_DUST.get(), RenderType.cutout());
        });
        //MenuScreens.register(ExTerraPower.GENERATOR_CONTAINER.get(), GeneratorScreen::new);
        BlockEntityRenderers.register(ExTerraCore.RITUAL_STONE_ENTITY.get(), RitualStoneBlockEntityRender::new);
        BlockEntityRenderers.register(ExTerraCore.RECEIVER_BLOCK_ENTITY.get(), MatterReceiverEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerModelLoader(ModelEvent.RegisterGeometryLoaders e){
        e.register(ExTerra.getResource("connected_textures"), (IGeometryLoader<?>) ConnectedTextureGeometry.ConnectedTextureLoader.INSTANCE);
    }

}
