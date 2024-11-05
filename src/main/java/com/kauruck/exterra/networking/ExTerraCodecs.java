package com.kauruck.exterra.networking;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.server.ServerLifecycleHooks;


public class ExTerraCodecs {

    public static final StreamCodec<RegistryFriendlyByteBuf, Character> STREAM_CHARACTER = ByteBufCodecs.STRING_UTF8
            .map(s -> s.charAt(0), Object::toString)
            .mapStream(ByteBuf::asByteBuf);

    public static final Codec<Character> CODEC_CHARACTER = Codec.STRING.xmap(s -> s.charAt(0), Object::toString);


    public static final Codec<Level> LEVEL_CODEC = ResourceKey.codec(Registries.DIMENSION).xmap(
            k -> ServerLifecycleHooks.getCurrentServer().getLevel(k),
            Level::dimension
    );
}
