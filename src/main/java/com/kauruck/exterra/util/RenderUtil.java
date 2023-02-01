package com.kauruck.exterra.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.util.List;
@OnlyIn(Dist.CLIENT)
public class RenderUtil {


    public static final Font FONT = Minecraft.getInstance().font;

    private static boolean depthTest = true;

    public static Vec3 getCameraPosition(){
       return Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();
    }

    public static Quaternion getCameraRotation(){
        return Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation();
    }

    public static void disableDepthTest(){
        depthTest = false;
        RenderSystem.disableDepthTest();
    }

    public static void enableDepthTest(){
        depthTest = true;
        RenderSystem.enableDepthTest();
    }


    // Copy from EntityRender.renderNameTag
    public static void renderFloatingTextOverBlock(BlockEntityRendererProvider.Context context, String pText, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        float f2 = (float)(-FONT.width(pText) / 2);
        float f = 0.6F;
        pMatrixStack.pushPose();
        pMatrixStack.translate(0.5D, (double)f, 0.5D);
        pMatrixStack.mulPose(context.getEntityRenderer().cameraOrientation());
        pMatrixStack.scale(-0.025F, -0.025F, 0.025F);
        Matrix4f matrix4f = pMatrixStack.last().pose();
        float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.9F);
        int j = (int)(f1 * 255.0F) << 24;
        FONT.drawInBatch(pText, f2, 0f, 553648127, false, matrix4f, pBuffer, false, j, pPackedLight);
        pMatrixStack.popPose();
    }

    public static void renderFloatingTextOverBlock(BlockPos offset, BlockEntityRendererProvider.Context context, String pText, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        float f2 = (float)(-FONT.width(pText) / 2);
        float f = 0.6F;
        pMatrixStack.pushPose();
        pMatrixStack.translate(0.5D, (double)f, 0.5D);
        pMatrixStack.translate(offset.getX(), offset.getY(), offset.getZ());
        pMatrixStack.mulPose(context.getEntityRenderer().cameraOrientation());
        pMatrixStack.scale(-0.025F, -0.025F, 0.025F);
        Matrix4f matrix4f = pMatrixStack.last().pose();
        float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.9F);
        int j = (int)(f1 * 255.0F) << 24;
        FONT.drawInBatch(pText, f2, 0f, 553648127, false, matrix4f, pBuffer, false, j, pPackedLight);
        pMatrixStack.popPose();
    }

    public static MultiBufferSource.BufferSource getMainBufferSource(){
        return Minecraft.getInstance().renderBuffers().bufferSource();
    }

    public static void renderOutline(PoseStack stack, VoxelShape shape, float red, float green, float blue, float alpha){
        VertexConsumer builder = getMainBufferSource().getBuffer(RenderType.lines());
        Matrix4f matrix4f = stack.last().pose();
        shape.forAllEdges((x1, y1, z1, x2, y2, z2) -> {
            builder.vertex(matrix4f, (float)x1, (float)y1, (float)z1).color(red, green, blue, alpha).normal(0,0,0).endVertex();
            builder.vertex(matrix4f, (float)x2, (float)y2, (float)z2).color(red, green, blue, alpha).normal(0,0,0).endVertex();
        });
        getMainBufferSource().endBatch(RenderType.lines());
    }

    //This is a copy from minecraft's Code, just with the color fixed
    public static void renderVoxelShape(PoseStack pPoseStack, VertexConsumer pConsumer, VoxelShape pShape, double pX, double pY, double pZ, float pRed, float pGreen, float pBlue, float pAlpha) {
        List<AABB> list = pShape.toAabbs();
        int i = Mth.ceil((double)list.size() / 3.0D);

        for(int j = 0; j < list.size(); ++j) {
            AABB aabb = list.get(j);
            renderShape(pPoseStack, pConsumer, Shapes.create(aabb.move(0.0D, 0.0D, 0.0D)), pX, pY, pZ, pRed, pGreen, pBlue, pAlpha);
        }

    }

    private static void renderShape(PoseStack pPoseStack, VertexConsumer pConsumer, VoxelShape pShape, double pX, double pY, double pZ, float pRed, float pGreen, float pBlue, float pAlpha) {
        PoseStack.Pose posestack$pose = pPoseStack.last();
        pShape.forAllEdges((p_234280_, p_234281_, p_234282_, p_234283_, p_234284_, p_234285_) -> {
            float f = (float)(p_234283_ - p_234280_);
            float f1 = (float)(p_234284_ - p_234281_);
            float f2 = (float)(p_234285_ - p_234282_);
            float f3 = Mth.sqrt(f * f + f1 * f1 + f2 * f2);
            f /= f3;
            f1 /= f3;
            f2 /= f3;
            pConsumer.vertex(posestack$pose.pose(), (float)(p_234280_ + pX), (float)(p_234281_ + pY), (float)(p_234282_ + pZ)).color(pRed, pGreen, pBlue, pAlpha).normal(posestack$pose.normal(), f, f1, f2).endVertex();
            pConsumer.vertex(posestack$pose.pose(), (float)(p_234283_ + pX), (float)(p_234284_ + pY), (float)(p_234285_ + pZ)).color(pRed, pGreen, pBlue, pAlpha).normal(posestack$pose.normal(), f, f1, f2).endVertex();
        });
    }

}
