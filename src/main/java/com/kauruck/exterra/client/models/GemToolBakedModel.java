package com.kauruck.exterra.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.QuadTransformer;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Model for the tools
 *
 * This renders the baseItem under the baseModel
 *
 * You do not need this. Use
 * @see GemToolLoader .
 *
 * @author Kauruck
 */
@OnlyIn(Dist.CLIENT)
public class GemToolBakedModel implements IBakedModel {

    private final IBakedModel baseModel;

    private final ItemStack baseItem;

    IModelTransform modelTransform;

    enum RenderState{
        INVENTORY,
        WORLD_FIRST,
        WORLD_THIRD;
    }

    private final RenderState renderState;
    private final GemToolLoader parent;

    /**
     * Creates a model
     * @param baseModel The base model
     * @param baseItem The item to render below
     * @param modelTransform The transform
     * @param parent The parent
     */
    public GemToolBakedModel(IBakedModel baseModel, ItemStack baseItem, IModelTransform modelTransform, GemToolLoader parent) {
        this.baseModel = baseModel;
        this.baseItem = baseItem;
        this.modelTransform = modelTransform;
        renderState = RenderState.WORLD_FIRST;
        this.parent = parent;
    }

    /**
     * Get the parent loader
     * @return The parent
     */
    public GemToolLoader getParent() {
        return parent;
    }

    /**
     * Creates a model for a perspective (Inventory, First Person, Third Person)
     * @param baseModel The base model
     * @param baseItem The item to render below
     * @param modelTransform The transform
     * @param renderState The perspective
     * @param parent The parent
     */
    public GemToolBakedModel(IBakedModel baseModel, ItemStack baseItem, IModelTransform modelTransform, RenderState renderState, GemToolLoader parent) {
        this.baseModel = baseModel;
        this.baseItem = baseItem;
        this.modelTransform = modelTransform;
        this.renderState = renderState;
        this.parent = parent;
    }

    //Copied from https://forums.minecraftforge.net/topic/84021-1152-render-item-on-block/
    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        IBakedModel base = this.baseModel;
        if(baseModel == null){
            return new ArrayList<>();
        }
        if (baseItem == null) {
            return base.getQuads(state, side, rand, extraData);
        }
        if(renderState == RenderState.WORLD_THIRD) {
            TransformationMatrix orgTM = modelTransform.getRotation();
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            IBakedModel itemModel = itemRenderer.getItemModelWithOverrides(baseItem, null, null);
            Matrix4f matrix = new Matrix4f(new Quaternion(0.0f, 0.0f, 0.0f, true));
            matrix.mul(Matrix4f.makeTranslate(0,0,0.05f));
            TransformationMatrix tm = new TransformationMatrix(matrix);
            QuadTransformer transformer = new QuadTransformer(tm);
            List<BakedQuad> updatedItemQuads = transformer.processMany(itemModel.getQuads(state, side, rand, extraData));
            List<BakedQuad> quads = new ArrayList<>();
            quads.addAll(base.getQuads(state, side, rand, extraData));
            quads.addAll(updatedItemQuads);
            QuadTransformer orgTransformer = new QuadTransformer(orgTM);
            Matrix4f finalTransformation = new Matrix4f(new Quaternion(35, 90, 0, true));
            finalTransformation.mul(Matrix4f.makeTranslate(-0.4f, 0.7f, 0.05f));
            finalTransformation.mul(Matrix4f.makeScale(0.8f, 0.8f, 0.8f));
            TransformationMatrix finalTm = new TransformationMatrix(finalTransformation);
            QuadTransformer finalTransformer = new QuadTransformer(finalTm);
            return finalTransformer.processMany(orgTransformer.processMany(quads));
        }
        else if(renderState == RenderState.INVENTORY){
            TransformationMatrix orgTM = modelTransform.getRotation();
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            IBakedModel itemModel = itemRenderer.getItemModelWithOverrides(baseItem, null, null);
            Matrix4f matrix = new Matrix4f(new Quaternion(0.0f, 0.0f, 0.0f, true));
            matrix.mul(Matrix4f.makeTranslate(0,0,-0.05f));
            TransformationMatrix tm = new TransformationMatrix(matrix);
            QuadTransformer transformer = new QuadTransformer(tm);
            List<BakedQuad> updatedItemQuads = transformer.processMany(itemModel.getQuads(state, side, rand, extraData));
            List<BakedQuad> quads = new ArrayList<>();
            quads.addAll(base.getQuads(state, side, rand, extraData));
            quads.addAll(updatedItemQuads);
            QuadTransformer orgTransformer = new QuadTransformer(orgTM);
            return orgTransformer.processMany(quads);
        }
        else if(renderState == RenderState.WORLD_FIRST){
            TransformationMatrix orgTM = modelTransform.getRotation();
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            IBakedModel itemModel = itemRenderer.getItemModelWithOverrides(baseItem, null, null);
            Matrix4f matrix = new Matrix4f(new Quaternion(0.0f, 0.0f, 0.0f, true));
            matrix.mul(Matrix4f.makeTranslate(0,0,0.05f));
            TransformationMatrix tm = new TransformationMatrix(matrix);
            QuadTransformer transformer = new QuadTransformer(tm);
            List<BakedQuad> updatedItemQuads = transformer.processMany(itemModel.getQuads(state, side, rand, extraData));
            List<BakedQuad> quads = new ArrayList<>();
            quads.addAll(base.getQuads(state, side, rand, extraData));
            quads.addAll(updatedItemQuads);
            QuadTransformer orgTransformer = new QuadTransformer(orgTM);
            Matrix4f finalTransformation = new Matrix4f(new Quaternion(60, 90, 0, true));
            finalTransformation.mul(Matrix4f.makeTranslate(-0.1f, 0.6f, 0.1f));
            finalTransformation.mul(Matrix4f.makeScale(0.7f, 0.7f, 0.7f));
            TransformationMatrix finalTm = new TransformationMatrix(finalTransformation);
            QuadTransformer finalTransformer = new QuadTransformer(finalTm);
            return finalTransformer.processMany(orgTransformer.processMany(quads));
        }
        return base.getQuads(state, side, rand, extraData);
    }



    @Override
    public TextureAtlasSprite getParticleTexture(@Nonnull IModelData data) {
        return baseModel.getParticleTexture(data);
    }

    // IBakedModel Methods
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
        return this.getQuads(state,side,rand, EmptyModelData.INSTANCE);
    }

    @Override
    public boolean isAmbientOcclusion() {
        return baseModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return baseModel.isGui3d();
    }

    @Override
    public boolean isSideLit() {
        return baseModel.isSideLit();
    }


    @Override
    public boolean isBuiltInRenderer() {
        return baseModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return baseModel.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return baseModel.getOverrides();
    }

    private GemToolBakedModel forProspective(RenderState renderState){
        return new GemToolBakedModel(this.baseModel, this.baseItem, this.modelTransform, renderState, this.parent);
    }


    //Thanks to : https://forums.minecraftforge.net/topic/83298-solved-1152-different-item-model-when-rendered-in-the-inventory-vs-handheld/
    @Override
    public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat) {
        if(cameraTransformType == ItemCameraTransforms.TransformType.GUI)
            return ForgeHooksClient.handlePerspective(this.forProspective(RenderState.INVENTORY), cameraTransformType, mat);
        else if(cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND ||cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)
            return ForgeHooksClient.handlePerspective(this.forProspective(RenderState.WORLD_FIRST), cameraTransformType, mat);
        else
            return ForgeHooksClient.handlePerspective(this.forProspective(RenderState.WORLD_THIRD), cameraTransformType, mat);

    }
}
