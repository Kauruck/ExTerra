package com.kauruck.exterra.client;

import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.VanillaResourceType;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConnectedTextureGeometry implements IModelGeometry<ConnectedTextureGeometry> {



    @Override
    public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
        return new ConnectedTextureModel(Arrays.stream(Direction.values())
                .map(resourceLocation -> new AbstractMap.SimpleEntry<>(resourceLocation
                        , spriteGetter.apply(owner.isTexturePresent(resourceLocation.toString()) ? owner.resolveTexture(resourceLocation.toString()) : owner.resolveTexture("all"))))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                )));
    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        Set<Material> texs = Sets.newHashSet();

        if(owner.isTexturePresent("north"))
            texs.add(owner.resolveTexture("north"));

        if(owner.isTexturePresent("south"))
            texs.add(owner.resolveTexture("south"));

        if(owner.isTexturePresent("west"))
            texs.add(owner.resolveTexture("west"));

        if(owner.isTexturePresent("east"))
            texs.add(owner.resolveTexture("east"));

        if(owner.isTexturePresent("up"))
            texs.add(owner.resolveTexture("up"));

        if(owner.isTexturePresent("down"))
            texs.add(owner.resolveTexture("down"));

        return texs;
    }

    public static class ConnectedTextureLoader implements IModelLoader<ConnectedTextureGeometry>{

        public static final ConnectedTextureLoader INSTANCE = new ConnectedTextureLoader();

        @Override
        public ConnectedTextureGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {



           /* ResourceLocation north = null;
            ResourceLocation down = null;
            ResourceLocation south = null;
            ResourceLocation up = null;
            ResourceLocation east = null;
            ResourceLocation west = null;
            ResourceLocation all = null;
            if(modelContents.has("north"))
                north = new ResourceLocation(modelContents.get("north").getAsString());
            if(modelContents.has("south"))
                south = new ResourceLocation(modelContents.get("south").getAsString());
            if(modelContents.has("west"))
                west = new ResourceLocation(modelContents.get("west").getAsString());
            if(modelContents.has("east"))
                east = new ResourceLocation(modelContents.get("east").getAsString());
            if(modelContents.has("up"))
                up = new ResourceLocation(modelContents.get("up").getAsString());
            if(modelContents.has("down"))
                down = new ResourceLocation(modelContents.get("down").getAsString());

            if(!modelContents.has("all")){
                if(north == null || south == null || west == null || east == null || up == null || down == null)
                    throw new RuntimeException("A Connected Texture model requires either an 'all' texture given or textures given for all");
            }
            else {
                all = new ResourceLocation(modelContents.get("all").getAsString());
            }*/
            return new ConnectedTextureGeometry();
        }

        @Override
        public IResourceType getResourceType()
        {
            return VanillaResourceType.MODELS;
        }

        @Override
        public void onResourceManagerReload(ResourceManager resourceManager)
        {
            // no need to clear cache since we create a new model instance
        }
    }

}
