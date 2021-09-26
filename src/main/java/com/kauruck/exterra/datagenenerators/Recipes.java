package com.kauruck.exterra.datagenenerators;

import com.kauruck.exterra.modules.ExTerraCore;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(ExTerraCore.COMPOUND.get(), 4)
                .pattern("cg ")
                .pattern("s  ")
                .pattern("   ")
                .define('c', Items.CLAY)
                .define('g', Tags.Items.GRAVEL)
                .define('s', Tags.Items.SAND)
                .group("exterra")
                .unlockedBy("clay", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CLAY))
                .save(consumer);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ExTerraCore.COMPOUND.get()), ExTerraCore.COMPOUND_BRICK.get(), 0.3f, 200)
                        .group("exterra")
                        .unlockedBy("compound", InventoryChangeTrigger.TriggerInstance.hasItems(ExTerraCore.COMPOUND.get()))
                        .save(consumer);

        ShapedRecipeBuilder.shaped(ExTerraCore.COMPOUND_BRICKS.get())
                .pattern("bb ")
                .pattern("bb ")
                .pattern("   ")
                .define('b', ExTerraCore.COMPOUND_BRICK.get())
                .group("exterra")
                .unlockedBy("compound", InventoryChangeTrigger.TriggerInstance.hasItems(ExTerraCore.COMPOUND_BRICK.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ExTerraCore.COMPOUND_FRAMED_GLASS.get())
                .requires(ExTerraCore.COMPOUND.get())
                .requires(Tags.Items.GLASS)
                .group("exterra")
                .unlockedBy("compound", InventoryChangeTrigger.TriggerInstance.hasItems(ExTerraCore.COMPOUND.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ExTerraCore.COMPOUND_BRICKS_STAIR.get(), 4)
                .pattern("x  ")
                .pattern("xx ")
                .pattern("xxx")
                .define('x', ExTerraCore.COMPOUND_BRICKS.get())
                .group("exterra")
                .unlockedBy("compound_bricks", InventoryChangeTrigger.TriggerInstance.hasItems(ExTerraCore.COMPOUND_BRICKS.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ExTerraCore.COMPOUND_BRICKS_SLAB.get(), 6)
                .pattern("   ")
                .pattern("   ")
                .pattern("xxx")
                .define('x', ExTerraCore.COMPOUND_BRICKS.get())
                .group("exterra")
                .unlockedBy("compound_bricks", InventoryChangeTrigger.TriggerInstance.hasItems(ExTerraCore.COMPOUND_BRICKS.get()))
                .save(consumer);
    }
}
