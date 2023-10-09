package com.kauruck.exterra.commands;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.api.recipes.ExTerraIngredient;
import com.kauruck.exterra.api.recipes.ExTerraRecipe;
import com.kauruck.exterra.data.ShapeData;
import com.kauruck.exterra.modules.ExTerraReloadableResources;
import com.kauruck.exterra.networking.BaseBlockEntity;
import com.kauruck.exterra.networking.BlockEntityProperty;
import com.kauruck.exterra.recipes.ConversionContainer;
import com.kauruck.exterra.recipes.ConversionRecipe;
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
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * This class holds command, I use for testing
 */
public class TestCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("conversions")
                .executes(TestCommand::sendRecipes);

        dispatcher.register(command);
    }

    private static int sendRecipes(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        MutableComponent root = MutableComponent.create(new LiteralContents("Conversion Recipes\n"));
        for(ExTerraRecipe<MatterStack, ?> recipeRaw : ExTerraReloadableResources.INSTANCE.<MatterStack>getRecipeManger(ExTerra.getResource("conversion")).getRecipes()){
            if(recipeRaw instanceof ConversionRecipe recipe){
                root.append(recipe.toString()+'\n');
                MutableComponent ingredients = MutableComponent.create(new LiteralContents("Ingredients\n"));
                for(ExTerraIngredient<MatterStack> currentIngredient : recipe.getIngredients()){
                    ingredients.append(currentIngredient.toString() + '\n');
                }
                root.append(ingredients);
                root.append("Output:\n");
                root.append(recipe.getResult().toString() + '\n');
                MutableComponent shapes = MutableComponent.create(new LiteralContents("Shapes\n"));
                for(ShapeData currentShape : recipe.getShapes()){
                    shapes.append(currentShape.toString() + '\n');
                }
                root.append(shapes);
            }
        }
        commandContext.getSource().getPlayer().sendSystemMessage(root);
        return 1;
    }
}
