package com.kauruck.exterra.commands;

import com.kauruck.exterra.ExTerra;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.client.ClientCommandSourceStack;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = ExTerra.MOD_ID)
public class ExTerraCommands {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event){
        CommandDispatcher<CommandSourceStack> commandDispatcher = event.getDispatcher();
        CommandGetProperty.register(commandDispatcher);

    }

    @SubscribeEvent
    public static void registerCommandsClient(RegisterClientCommandsEvent event){
        CommandDispatcher<CommandSourceStack> commandDispatcher = event.getDispatcher();
        CommandClientGetProperty.register(commandDispatcher);
    }
}
