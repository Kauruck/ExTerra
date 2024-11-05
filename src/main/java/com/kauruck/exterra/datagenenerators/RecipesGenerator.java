package com.kauruck.exterra.datagenenerators;

import com.kauruck.exterra.modules.ExTerraCore;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class RecipesGenerator extends RecipeProvider {

    public RecipesGenerator(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ExTerraCore.COMPOUND.get(), 4)
                .pattern("cg ")
                .pattern("s  ")
                .pattern("   ")
                .define('c', Items.CLAY)
                .define('g', Tags.Items.GRAVELS)
                .define('s', Tags.Items.SANDS)
                .group("exterra")
                .unlockedBy("clay", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CLAY))
                .save(output);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ExTerraCore.COMPOUND.get()), RecipeCategory.MISC,  ExTerraCore.COMPOUND_BRICK.get(), 0.3f, 200)
                .group("exterra")
                .unlockedBy("compound", InventoryChangeTrigger.TriggerInstance.hasItems(ExTerraCore.COMPOUND.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ExTerraCore.COMPOUND_BRICKS.get())
                .pattern("bb ")
                .pattern("bb ")
                .pattern("   ")
                .define('b', ExTerraCore.COMPOUND_BRICK.get())
                .group("exterra")
                .unlockedBy("compound", InventoryChangeTrigger.TriggerInstance.hasItems(ExTerraCore.COMPOUND_BRICK.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ExTerraCore.COMPOUND_FRAMED_GLASS.get())
                .requires(ExTerraCore.COMPOUND.get())
                .requires(Tags.Items.GLASS_BLOCKS)
                .group("exterra")
                .unlockedBy("compound", InventoryChangeTrigger.TriggerInstance.hasItems(ExTerraCore.COMPOUND.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ExTerraCore.COMPOUND_BRICKS_STAIR.get(), 4)
                .pattern("x  ")
                .pattern("xx ")
                .pattern("xxx")
                .define('x', ExTerraCore.COMPOUND_BRICKS.get())
                .group("exterra")
                .unlockedBy("compound_bricks", InventoryChangeTrigger.TriggerInstance.hasItems(ExTerraCore.COMPOUND_BRICKS.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ExTerraCore.COMPOUND_BRICKS_SLAB.get(), 6)
                .pattern("   ")
                .pattern("   ")
                .pattern("xxx")
                .define('x', ExTerraCore.COMPOUND_BRICKS.get())
                .group("exterra")
                .unlockedBy("compound_bricks", InventoryChangeTrigger.TriggerInstance.hasItems(ExTerraCore.COMPOUND_BRICKS.get()))
                .save(output);
    }
}
