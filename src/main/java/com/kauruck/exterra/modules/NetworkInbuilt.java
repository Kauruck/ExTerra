package com.kauruck.exterra.modules;

import com.kauruck.exterra.networking.BlockEntityCodecHolder;
import com.kauruck.exterra.networking.ExTerraCodecs;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.kauruck.exterra.modules.RegistryManger.BLOCK_ENTITY_PROPERTY_CODEC_REGISTRY;

public class NetworkInbuilt {

    public static DeferredHolder<BlockEntityCodecHolder<?>, BlockEntityCodecHolder<Float>> PROPERTY_FLOAT = BLOCK_ENTITY_PROPERTY_CODEC_REGISTRY.register("float", () -> new BlockEntityCodecHolder<>(Codec.FLOAT));
    public static DeferredHolder<BlockEntityCodecHolder<?>, BlockEntityCodecHolder<Boolean>> PROPERTY_BOOLEAN = BLOCK_ENTITY_PROPERTY_CODEC_REGISTRY.register("bool", () -> new BlockEntityCodecHolder<>(Codec.BOOL));
    public static DeferredHolder<BlockEntityCodecHolder<?>, BlockEntityCodecHolder<String>> PROPERTY_STRING = BLOCK_ENTITY_PROPERTY_CODEC_REGISTRY.register("string", () -> new BlockEntityCodecHolder<>(Codec.STRING));
    public static DeferredHolder<BlockEntityCodecHolder<?>, BlockEntityCodecHolder<Integer>> PROPERTY_INTEGER = BLOCK_ENTITY_PROPERTY_CODEC_REGISTRY.register("int", () -> new BlockEntityCodecHolder<>(Codec.INT));
    public static DeferredHolder<BlockEntityCodecHolder<?>, BlockEntityCodecHolder<Double>> PROPERTY_DOUBLE = BLOCK_ENTITY_PROPERTY_CODEC_REGISTRY.register("double", () -> new BlockEntityCodecHolder<>(Codec.DOUBLE));
    public static DeferredHolder<BlockEntityCodecHolder<?>, BlockEntityCodecHolder<BlockPos>> PROPERTY_BLOCK_POS = BLOCK_ENTITY_PROPERTY_CODEC_REGISTRY.register("block_pos", () -> new BlockEntityCodecHolder<>(BlockPos.CODEC));
    public static DeferredHolder<BlockEntityCodecHolder<?>, BlockEntityCodecHolder<ResourceLocation>> PROPERTY_RESOURCE_LOCATION = BLOCK_ENTITY_PROPERTY_CODEC_REGISTRY.register("resource_location", () -> new BlockEntityCodecHolder<>(ResourceLocation.CODEC));
}
