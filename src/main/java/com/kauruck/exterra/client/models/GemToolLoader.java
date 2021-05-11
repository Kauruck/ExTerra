package com.kauruck.exterra.client.models;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.kauruck.exterra.API.gem.Gem;
import com.kauruck.exterra.API.item.tool.BaseGemTool;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.VanillaResourceType;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The loader for the gem tool.
 * Use this for the item model with
 * "loader" : "exterra:gem_tool"
 *
 * This requires the particle texture and a "gem_cutout" as a layout for the GemTexture
 *
 * @author Kauruck
 */
@OnlyIn(Dist.CLIENT)
public class GemToolLoader implements IModelGeometry<GemToolLoader> {

    private static final float NORTH_Z_COVER = 7.496f / 16f;
    private static final float SOUTH_Z_COVER = 8.504f / 16f;
    private static final float NORTH_Z_FLUID = 7.498f / 16f;
    private static final float SOUTH_Z_FLUID = 8.502f / 16f;

    private Item parent = null;
    private final ResourceLocation gemTexture;

    public GemToolLoader(Item parent, ResourceLocation gemTexture){
        this.parent = parent;
        this.gemTexture = gemTexture;

    }

    public GemToolLoader(){
        parent = Items.BUCKET;
        gemTexture = new ResourceLocation("minecraft", "blocks/stone");
    }

    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation) {
        RenderMaterial particleLocation = owner.isTexturePresent("particle") ? owner.resolveTexture("particle") : null;
        RenderMaterial gemCutoutLocation = owner.isTexturePresent("gem_cutout") ? owner.resolveTexture("gem_cutout") : null;
        RenderMaterial toolOverlayLocation = owner.isTexturePresent("tool_texture") ? owner.resolveTexture("tool_texture") : null;
        if(gemCutoutLocation == null)
            return new GemToolBakedModel(null, parent.getDefaultInstance(), modelTransform, this);

        RenderMaterial gemLocation = new RenderMaterial(gemCutoutLocation.getAtlasLocation(), gemTexture);


        IModelTransform transformsFromModel = owner.getCombinedTransform();
        ImmutableMap<ItemCameraTransforms.TransformType, TransformationMatrix> transformMap =
                PerspectiveMapWrapper.getTransforms(new ModelTransformComposition(transformsFromModel, modelTransform));


        TextureAtlasSprite particleSprite = spriteGetter.apply(particleLocation);

        TransformationMatrix transform = modelTransform.getRotation();


        ItemMultiLayerBakedModel.Builder builder = ItemMultiLayerBakedModel.builder(owner, particleSprite, new GemToolRendererOverriderHandler(overrides, bakery, owner), transformMap);

        TextureAtlasSprite gemTexture = spriteGetter.apply(gemLocation);
        TextureAtlasSprite gemCutoutTexture = spriteGetter.apply(gemCutoutLocation);
        if(toolOverlayLocation != null) {
            TextureAtlasSprite toolOverlayTexture = spriteGetter.apply(toolOverlayLocation);
            builder.addQuads(ItemLayerModel.getLayerRenderType(false), ItemTextureQuadConverter.genQuad(transform, 0,0,16,16, 0, toolOverlayTexture, Direction.NORTH, 1, 1));
            builder.addQuads(ItemLayerModel.getLayerRenderType(false), ItemTextureQuadConverter.genQuad(transform, 0,0,16,16, 0, toolOverlayTexture, Direction.SOUTH, 1, 1));
        }

        builder.addQuads(ItemLayerModel.getLayerRenderType(false), ItemTextureQuadConverter.convertTexture(transform,gemCutoutTexture, gemTexture , NORTH_Z_FLUID, Direction.NORTH,0xFFFFFFFF, 1));
        builder.addQuads(ItemLayerModel.getLayerRenderType(false),  ItemTextureQuadConverter.convertTexture(transform, gemCutoutTexture, gemTexture, SOUTH_Z_COVER, Direction.SOUTH, 0xFFFFFFFF,1));

        builder.setParticle(particleSprite);

            return new GemToolBakedModel(builder.build(), parent.getDefaultInstance(), modelTransform, this);
    }

    @Override
    public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        Set<RenderMaterial> texs = Sets.newHashSet();
        RenderMaterial gemLocation = owner.isTexturePresent("gem_cutout") ? owner.resolveTexture("gem_cutout") : null;
        texs.add(gemLocation);

        return texs;
    }

    public GemToolLoader forItem(Item parent, Gem gem){
        if(gem == null)
            return new GemToolLoader();
        return new GemToolLoader(parent, gem.getTexture());
    }

    public enum Loader implements IModelLoader<GemToolLoader>
    {
        INSTANCE;

        @Override
        public IResourceType getResourceType()
        {
            return VanillaResourceType.MODELS;
        }

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager)
        {
            // no need to clear cache since we create a new model instance
        }

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate)
        {
            // no need to clear cache since we create a new model instance
        }

        @Override
        public GemToolLoader read(JsonDeserializationContext deserializationContext, JsonObject modelContents)
        {
            return new GemToolLoader();
        }
    }




    private static class GemToolRendererOverriderHandler extends ItemOverrideList{

        private final Map<String, IBakedModel> cache = Maps.newHashMap(); // contains all the baked models since they'll never change
        private final ItemOverrideList nested;
        private final ModelBakery bakery;
        private final IModelConfiguration owner;

        private GemToolRendererOverriderHandler(ItemOverrideList nested, ModelBakery bakery, IModelConfiguration owner)
        {
            this.nested = nested;
            this.bakery = bakery;
            this.owner = owner;
        }

        @Nullable
        @Override
        public IBakedModel getOverrideModel(IBakedModel originalModel, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity) {
            IBakedModel overriden = nested.getOverrideModel(originalModel, stack, world, entity);
            if (overriden != originalModel) return overriden;
            String name = "";
            if(stack.hasTag()) {
                CompoundNBT tag = stack.getTag();
                if (tag.contains(BaseGemTool.TAG_ORIGINAL_ITEM)){
                    name = tag.getString(BaseGemTool.TAG_ORIGINAL_ITEM);
                }
            }
            if(name == ""){
                name = stack.getItem().getDefaultInstance().getTag().getString(BaseGemTool.TAG_ORIGINAL_ITEM);
            }
            if (!cache.containsKey(name)) {
                GemToolLoader unbaked = ((GemToolBakedModel)originalModel).getParent().forItem(ForgeRegistries.ITEMS.getValue(new ResourceLocation(name)), stack.getItem() instanceof BaseGemTool ? ((BaseGemTool)stack.getItem()).getGemType(stack) : null);
                IBakedModel bakedModel = unbaked.bake(owner, bakery, ModelLoader.defaultTextureGetter(), ModelRotation.X0_Y0, this, new ResourceLocation("exterra:gem_tool_override"));
                cache.put(name, bakedModel);
                return bakedModel;
            }
            return cache.get(name);
        }

    }
}
