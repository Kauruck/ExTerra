package com.kauruck.exterra.modules;

import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.api.recipes.ExTerraIngredientSerializer;
import com.kauruck.exterra.api.recipes.ExTerraRecipeSerializer;
import com.kauruck.exterra.api.recipes.ExTerraRecipeType;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class ExTerraRegistries {

    public static Supplier<IForgeRegistry<Matter>> MATTER;
    public static Supplier<IForgeRegistry<ExTerraRecipeType<?>>> RECIPE_TYPE;
    public static Supplier<IForgeRegistry<ExTerraRecipeSerializer<?>>> RECIPE_SERIALIZER;
    public static Supplier<IForgeRegistry<ExTerraIngredientSerializer<?>>> INGREDIENT_SERIALIZER;

    public static void makeRegistries(){
        MATTER = RegistryManger.MATTER_REGISTRY.makeRegistry(() -> new RegistryBuilder<Matter>()
                .hasTags());

        RECIPE_TYPE = RegistryManger.RECIPE_TYPE_REGISTRY.makeRegistry(RegistryBuilder::new);
        RECIPE_SERIALIZER = RegistryManger.RECIPE_SERIALIZER_REGISTRY.makeRegistry(RegistryBuilder::new);
        INGREDIENT_SERIALIZER = RegistryManger.INGREDIENT_SERIALIZER_REGISTRY.makeRegistry(RegistryBuilder::new);


    }
}
