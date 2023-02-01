package com.kauruck.exterra.client.model;

import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConnectedTextureGeometry implements IUnbakedGeometry<ConnectedTextureGeometry> {

    private final ChunkRenderTypeSet renderTypes;

    public ConnectedTextureGeometry(ChunkRenderTypeSet renderTypes){
        this.renderTypes = renderTypes;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
        return new ConnectedTextureModel(Arrays.stream(Direction.values())
                .map(resourceLocation -> new AbstractMap.SimpleEntry<>(resourceLocation
                        , spriteGetter.apply(context.hasMaterial(resourceLocation.toString()) ? context.getMaterial(resourceLocation.toString()) : context.getMaterial("all"))))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                )), renderTypes);
    }

    @Override
    public Collection<Material> getMaterials(IGeometryBakingContext context, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        Set<Material> texs = Sets.newHashSet();

        if(context.hasMaterial("north"))
            texs.add(context.getMaterial("north"));

        if(context.hasMaterial("south"))
            texs.add(context.getMaterial("south"));

        if(context.hasMaterial("west"))
            texs.add(context.getMaterial("west"));

        if(context.hasMaterial("east"))
            texs.add(context.getMaterial("east"));

        if(context.hasMaterial("up"))
            texs.add(context.getMaterial("up"));

        if(context.hasMaterial("down"))
            texs.add(context.getMaterial("down"));

        return texs;
    }

    public static class ConnectedTextureLoader implements IGeometryLoader<ConnectedTextureGeometry> {

        public static final ConnectedTextureLoader INSTANCE = new ConnectedTextureLoader();

        @Override
        public ConnectedTextureGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
            ChunkRenderTypeSet renderTypes = ChunkRenderTypeSet.none();
            if(jsonObject.has("render_type")){
                if(jsonObject.get("render_type").getAsString().equals("minecraft:translucent"))
                    renderTypes = ChunkRenderTypeSet.of(RenderType.translucent());
            }
            return new ConnectedTextureGeometry(renderTypes);
        }
    }

}
