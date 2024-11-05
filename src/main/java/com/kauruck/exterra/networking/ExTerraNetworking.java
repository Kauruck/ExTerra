package com.kauruck.exterra.networking;


import com.kauruck.exterra.ExTerra;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@EventBusSubscriber(modid = ExTerra.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ExTerraNetworking {

    public static final Logger LOGGER  = LogManager.getLogger();

    public static final String PROTOCOL_VERSION = "1";

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playToClient(
                BlockEntityClientBoundUpdatePacket.TYPE,
                BlockEntityClientBoundUpdatePacket.STREAM_CODEC,
                BlockEntityClientBoundUpdatePacket::handle
        );
        registrar.playToServer(
                RequestUpdatePacket.TYPE,
                RequestUpdatePacket.STREAM_CODEC,
                RequestUpdatePacket::handle
        );
    }
}
