package com.kauruck.exterra;

import com.kauruck.exterra.blockentityrenderer.MatterReceiverEntityRenderer;
import com.kauruck.exterra.blockentityrenderer.RitualStoneBlockEntityRender;
import com.kauruck.exterra.client.model.ConnectedTextureGeometry;
import com.kauruck.exterra.modules.ExTerraCore;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ExTerraClient {

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
        e.register("connected_textures", (IGeometryLoader<?>) ConnectedTextureGeometry.ConnectedTextureLoader.INSTANCE);
    }

}
