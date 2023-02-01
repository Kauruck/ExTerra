package com.kauruck.exterra.blockentityrenderer;

import com.kauruck.exterra.blockentities.RitualStoneEntity;
import com.kauruck.exterra.geometry.Shape;
import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.util.RenderUtil;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.entity.player.Player;

public class RitualStoneBlockEntityRender implements BlockEntityRenderer<RitualStoneEntity> {

    private final BlockEntityRendererProvider.Context context;
    public RitualStoneBlockEntityRender(BlockEntityRendererProvider.Context context){
        this.context = context;
    }

    @Override
    public void render(RitualStoneEntity blockEntity, float pPartialTick, PoseStack stack, MultiBufferSource bufferSource, int pPackedLight, int pPackedOverlay) {
        if(blockEntity.isBroken()) {
            RenderUtil.renderFloatingTextOverBlock(context, "Broken", stack, bufferSource, pPackedLight);
        }
        else {
            Player player = Minecraft.getInstance().player;
            if(player.getMainHandItem().getItem() == ExTerraCore.RITUAL_LENS.get()){
                for(Shape currentShape : blockEntity.getShapes()){
                    RenderUtil.renderFloatingTextOverBlock(currentShape.get(0).offset(0,1,0),context, currentShape.toString(), stack, bufferSource, pPackedLight);
                }
            }
        }
    }

}
