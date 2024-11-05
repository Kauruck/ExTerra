package com.kauruck.exterra.commands;

import com.kauruck.exterra.ExTerra;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = ExTerra.MOD_ID)
public class ExTerraCommands {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event){
        CommandDispatcher<CommandSourceStack> commandDispatcher = event.getDispatcher();
        CommandGetProperty.register(commandDispatcher);
        TestCommand.register(commandDispatcher);

    }

    @SubscribeEvent
    public static void registerCommandsClient(RegisterClientCommandsEvent event){
        CommandDispatcher<CommandSourceStack> commandDispatcher = event.getDispatcher();
        CommandClientGetProperty.register(commandDispatcher);
    }
}
