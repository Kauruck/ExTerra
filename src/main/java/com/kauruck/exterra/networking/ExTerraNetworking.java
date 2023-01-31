package com.kauruck.exterra.networking;

import com.kauruck.exterra.ExTerra;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ExTerraNetworking {

    public static final Logger LOGGER  = LogManager.getLogger();

    public static final String PROTOCOL_VERSION = "1";

    private static int id = 0;

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ExTerra.getResource("main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );


    public static void init(){
        registerMessage(BlockEntityClientBoundUpdatePacket.class, BlockEntityClientBoundUpdatePacket::encoder, BlockEntityClientBoundUpdatePacket::decoder, BlockEntityClientBoundUpdatePacket::messageConsumer);
        registerMessage(RequestUpdatePacket.class, RequestUpdatePacket::encoder, RequestUpdatePacket::decoder, RequestUpdatePacket::messageConsumer);
    }

    private static <MSG> void registerMessage(Class<MSG> messageType, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer){
        INSTANCE.registerMessage(id, messageType, encoder, decoder, messageConsumer);
        id++;
    }
}
