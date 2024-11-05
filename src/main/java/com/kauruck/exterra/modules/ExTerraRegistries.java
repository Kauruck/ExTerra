package com.kauruck.exterra.modules;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.geometry.IGeometricSerializer;
import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.api.recipes.*;
import com.kauruck.exterra.networking.BlockEntityCodecHolder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;


public class ExTerraRegistries {

    public static final ResourceKey<Registry<Matter>> MATTER_KEY = ResourceKey.createRegistryKey(ExTerra.getResource("matter"));
    public static final Registry<Matter> MATTER = new RegistryBuilder<>(MATTER_KEY)
            .create();

    public static final ResourceKey<Registry<ExTerraRecipeType<?>>> RECIPE_TYPE_KEY = ResourceKey.createRegistryKey(ExTerra.getResource("recipe_type"));
    public static final Registry<ExTerraRecipeType<?>> RECIPE_TYPE = new RegistryBuilder<>(RECIPE_TYPE_KEY)
            .sync(true)
            .create();

    public static final ResourceKey<Registry<MapCodec<? extends ExTerraRecipe<?, ?>>>> RECIPE_SERIALIZER_KEY = ResourceKey.createRegistryKey(ExTerra.getResource("recipe_serializer"));
    public static final Registry<MapCodec<? extends ExTerraRecipe<?, ?>>> RECIPE_SERIALIZER = new RegistryBuilder<>(RECIPE_SERIALIZER_KEY)
            .sync(true)
            .create();

    public static final ResourceKey<Registry<MapCodec<? extends ExTerraIngredient<?>>>> INGREDIENT_SERIALIZER_KEY = ResourceKey.createRegistryKey(ExTerra.getResource("ingredient_serializer"));
    public static final Registry<MapCodec<? extends ExTerraIngredient<?>>> INGREDIENT_SERIALIZER = new RegistryBuilder<>(INGREDIENT_SERIALIZER_KEY)
            .sync(true)
            .create();

    public static final ResourceKey<Registry<IGeometricSerializer>> GEOMETRIC_SERIALIZER_KEY = ResourceKey.createRegistryKey(ExTerra.getResource("geometric_serializer"));
    public static final Registry<IGeometricSerializer> GEOMETRIC_SERIALIZER = new RegistryBuilder<>(GEOMETRIC_SERIALIZER_KEY)
            .sync(true)
            .create();

    public static final ResourceKey<Registry<BlockEntityCodecHolder<?>>> BLOCK_ENTITY_PROPERTY_CODEC_KEY = ResourceKey.createRegistryKey(ExTerra.getResource("block_entity_property_codec"));
    public static final Registry<BlockEntityCodecHolder<?>> BLOCK_ENTITY_PROPERTY_CODEC = new RegistryBuilder<>(BLOCK_ENTITY_PROPERTY_CODEC_KEY)
            .sync(true)
            .create();

    @SubscribeEvent
    public static void makeRegistries(NewRegistryEvent event) {
        event.register(MATTER);
        event.register(RECIPE_TYPE);
        event.register(RECIPE_SERIALIZER);
        event.register(INGREDIENT_SERIALIZER);
        event.register(GEOMETRIC_SERIALIZER);
        event.register(BLOCK_ENTITY_PROPERTY_CODEC);
    }
}
