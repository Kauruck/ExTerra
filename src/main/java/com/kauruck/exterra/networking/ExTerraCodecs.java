package com.kauruck.exterra.networking;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.server.ServerLifecycleHooks;


public class ExTerraCodecs {

    public static final StreamCodec<RegistryFriendlyByteBuf, Character> STREAM_CHARACTER = ByteBufCodecs.STRING_UTF8
            .map(s -> s.charAt(0), Object::toString)
            .mapStream(ByteBuf::asByteBuf);

    public static final Codec<Character> CODEC_CHARACTER = Codec.STRING.xmap(s -> s.charAt(0), Object::toString);

    public static final Codec<Block> BLOCK_CODEC = ResourceLocation.CODEC.comapFlatMap(
            loc -> BuiltInRegistries.BLOCK.containsKey(loc) ?
                    DataResult.success(BuiltInRegistries.BLOCK.get(loc)):
                    DataResult.error(() -> "Block not found: " + loc),
            BuiltInRegistries.BLOCK::getKey
    );


    public static final Codec<Level> LEVEL_CODEC = ResourceKey.codec(Registries.DIMENSION).xmap(
            k -> ServerLifecycleHooks.getCurrentServer().getLevel(k),
            Level::dimension
    );
}
