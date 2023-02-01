package com.kauruck.exterra.blockentityrenderer;

import com.kauruck.exterra.blockentities.MatterReceiverEntity;
import com.kauruck.exterra.util.RenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class MatterReceiverEntityRenderer implements BlockEntityRenderer<MatterReceiverEntity> {

    private final BlockEntityRendererProvider.Context context;
    public MatterReceiverEntityRenderer(BlockEntityRendererProvider.Context context){
        this.context = context;
    }

    @Override
    public void render(MatterReceiverEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        RenderUtil.renderFloatingTextOverBlock(context, Integer.toString(pBlockEntity.getReceivedMatter()),  pPoseStack, pBufferSource, pPackedLight);
    }
}
