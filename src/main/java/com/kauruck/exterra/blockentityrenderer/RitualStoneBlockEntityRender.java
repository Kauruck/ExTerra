package com.kauruck.exterra.blockentityrenderer;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.blockentities.RitualStoneEntity;
import com.kauruck.exterra.geometry.Shape;
import com.kauruck.exterra.modules.ExTerraCore;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;

import java.awt.*;

import static com.kauruck.exterra.fx.MathHelper.clamp;

//TODO Do I need this
public class RitualStoneBlockEntityRender implements BlockEntityRenderer<RitualStoneEntity> {

    //From Industrial Forgoing
    private static RenderType AREA_TYPE = createRenderType();
    public static RenderType createRenderType() {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorShader))
                .setTransparencyState(new RenderStateShard.TransparencyStateShard("translucent_transparency", () -> {
                    RenderSystem.enableBlend();
                    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                }, () -> {
                    RenderSystem.disableBlend();
                    RenderSystem.defaultBlendFunc();
                })).createCompositeState(true);
        return RenderType.create("working_area_render", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, true, state);
    }
    private final BlockEntityRendererProvider.Context context;
    public RitualStoneBlockEntityRender(BlockEntityRendererProvider.Context context){
        this.context = context;
    }

    @Override
    public void render(RitualStoneEntity blockEntity, float pPartialTick, PoseStack stack, MultiBufferSource bufferSource, int pPackedLight, int pPackedOverlay) {
        // Currently not renderting anything
        /*Player player = Minecraft.getInstance().player;
        if(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == ExTerraCore.DEBUG_LENS.get() || player.getItemInHand(InteractionHand.OFF_HAND).getItem() == ExTerraCore.DEBUG_LENS.get()){
            renderDebug(blockEntity, stack, bufferSource, pPackedLight, pPackedOverlay);
        }*/
    }


    private void renderDebug(RitualStoneEntity entity, PoseStack stack, MultiBufferSource bufferSource, int pPackedLight, int pPackedOverlay){
        BlockPos blockPos = entity.getBlockPos();
        //Color color = new Color(Math.abs(blockPos.getX() % 255), Math.abs(blockPos.getY() % 255), Math.abs(blockPos.getZ() % 255));

        int i = 0;
        int r = 100;
        int g = 100;
        int b = 100;
        int step = (int) (155 * (1f/entity.getShapes().size()));
        for(Shape current : entity.getShapes()){
            Color color = new Color(r,g,b);
            for(BlockPos currentPos : current.getActualPositions(blockPos)){
                drawCubeAtBlock(stack, entity.getBlockPos(), currentPos, color, bufferSource);
            }
            switch (i%3){
                case 0: r += step;
                case 1: g += step;
                case 2: b += step;
            }
            r = clamp(r, 0, 255);
            g = clamp(g, 0, 255);
            b = clamp(b, 0, 255);
            i++;
        }
    }



    //From Tinkers Construct
    private void drawCubeAtBlock(PoseStack stack,BlockPos tilePos, BlockPos blockPos, Color color, MultiBufferSource renderTypeBuffer){
        float dx = blockPos.getX() - tilePos.getX();
        float dy = blockPos.getY() - tilePos.getY();
        float dz = blockPos.getZ() - tilePos.getZ();
        VertexConsumer consumer = renderTypeBuffer.getBuffer(RenderType.LINES);
        RenderHelper.renderVoxelShape(stack, consumer, Shapes.block(), dx,dy, dz, (float) color.getRed() / 255f, (float) color.getGreen() / 255f, (float) color.getBlue() / 255f, 1);
        //renderFaces(stack, renderTypeBuffer, shape.bounds(), (double) -blockPos.getX(), (double) -blockPos.getY(), (double) -blockPos.getZ(), (float) color.getRed() / 255f, (float) color.getGreen() / 255f, (float) color.getBlue() / 255f, 0.3F);
    }

    private void renderFaces(PoseStack stack, MultiBufferSource renderTypeBuffer, AABB pos, double x, double y, double z, float red, float green, float blue, float alpha) {

        float x1 = (float) (pos.minX + x);
        float x2 = (float) (pos.maxX + x);
        float y1 = (float) (pos.minY + y);
        float y2 = (float) (pos.maxY + y);
        float z1 = (float) (pos.minZ + z);
        float z2 = (float) (pos.maxZ + z);

        Matrix4f matrix = stack.last().pose();
        VertexConsumer buffer;

        buffer = renderTypeBuffer.getBuffer(AREA_TYPE);

        buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha).endVertex();

        buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha).endVertex();


        buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha).endVertex();

        buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha).endVertex();


        buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha).endVertex();

        buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha).endVertex();

    }

}
