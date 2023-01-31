package com.kauruck.exterra.commands;

import com.kauruck.exterra.networking.BaseBlockEntity;
import com.kauruck.exterra.networking.BlockEntityProperty;
import com.kauruck.exterra.networking.BlockEntityPropertySide;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CommandClientGetProperty {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("property_client")
                .then(Commands.argument("block", BlockPosArgument.blockPos())
                        .executes(CommandClientGetProperty::sendServerBlockPos)
                );

        dispatcher.register(command);
    }

    private static int sendServerBlockPos(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        BlockPos pos = BlockPosArgument.getLoadedBlockPos(commandContext, "block");
        Level level = commandContext.getSource().getUnsidedLevel();
        BlockEntity blockEntity = level.getBlockEntity(pos);
        Block block = level.getBlockState(pos).getBlock();
        MutableComponent root = block.getName().append("\n");
        if(blockEntity instanceof BaseBlockEntity entity){
            for(BlockEntityProperty<?> currentProperty : entity.getProperties()){
                if(currentProperty.getSide() == BlockEntityPropertySide.Synced || currentProperty.getSide() == BlockEntityPropertySide.Requestable ) {
                    MutableComponent propertyComponent = Component.literal(ChatFormatting.YELLOW + currentProperty.getName())
                            .append(ChatFormatting.BLACK + ": ")
                            .append(ChatFormatting.BLACK + currentProperty.get().toString())
                            .append(ChatFormatting.BLUE + " (")
                            .append(ChatFormatting.BLUE + currentProperty.getSide().toString())
                            .append(ChatFormatting.BLUE + ")")
                            .append("\n");
                    if(currentProperty.getSide() == BlockEntityPropertySide.Requestable){
                        propertyComponent.append(" (")
                                .append(currentProperty.isOutOfSync() ? ChatFormatting.RED + "Out Of Sync" : ChatFormatting.GREEN + "Synced")
                                .append(")");
                    }
                    root.append(propertyComponent);
                }
            }
        }
        else{
            root.append("Has no BlockEntity that inherits from BaseBlockEntity");
        }
        commandContext.getSource().sendSystemMessage(root);
        return 1;
    }
}
