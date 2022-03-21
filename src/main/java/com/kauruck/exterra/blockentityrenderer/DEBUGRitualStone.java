package com.kauruck.exterra.blockentityrenderer;

import com.google.common.collect.ImmutableMap;
import com.kauruck.exterra.blockentities.RitualStoneEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;

public class DEBUGRitualStone implements BlockEntityRenderer<RitualStoneEntity> {

    private final Font font;

    private final int xOffsetPerBlock = 30;

    public DEBUGRitualStone(BlockEntityRendererProvider.Context context) {
        this.font = context.getFont();
    }

    @Override
    public void render(RitualStoneEntity pBlockEntity, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay) {
        pMatrixStack.pushPose();
        pMatrixStack.scale(0.02f,-0.02f,-0.02f);
        pMatrixStack.translate(-30,-60,-30);
        this.font.draw(pMatrixStack, "Tier 1", 0, 0, 1);
        pMatrixStack.popPose();
    }

    private void drawTextAtBlock(String text, BlockPos pos){

    }

    @Override
    public boolean shouldRenderOffScreen(RitualStoneEntity pTe) {
        return true;
    }
}
