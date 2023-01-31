package com.kauruck.exterra.commands;

import com.kauruck.exterra.networking.BaseBlockEntity;
import com.kauruck.exterra.networking.BlockEntityProperty;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CommandGetProperty {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("property")
                    .then(Commands.argument("block", BlockPosArgument.blockPos())
                            .executes(CommandGetProperty::sendServerBlockPos)
                    );

        dispatcher.register(command);
    }

    private static int sendServerBlockPos(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException{
        BlockPos pos = BlockPosArgument.getLoadedBlockPos(commandContext, "block");
        Level level = commandContext.getSource().getLevel();
        BlockEntity blockEntity = level.getBlockEntity(pos);
        Block block = level.getBlockState(pos).getBlock();
        MutableComponent root = block.getName().append("\n");
        if(blockEntity instanceof BaseBlockEntity entity){
            for(BlockEntityProperty<?> currentProperty : entity.getProperties()){
                MutableComponent propertyComponent = Component.literal( ChatFormatting.YELLOW + currentProperty.getName())
                        .append(ChatFormatting.BLACK + ": ")
                        .append(ChatFormatting.BLACK + currentProperty.get().toString())
                        .append(ChatFormatting.BLUE + " (")
                        .append(ChatFormatting.BLUE + currentProperty.getSide().toString())
                        .append(ChatFormatting.BLUE + ")")
                        .append("\n");
                root.append(propertyComponent);
            }
        }
        else{
            root.append("Has no BlockEntity that inherits from BaseBlockEntity");
        }
        commandContext.getSource().getPlayer().sendSystemMessage(root);
        return 1;
    }
}
