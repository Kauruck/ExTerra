package com.kauruck.exterra.serializers;

import com.kauruck.exterra.api.recipes.ExTerraIngredient;
import com.kauruck.exterra.modules.ExTerraRegistries;
import com.mojang.serialization.Codec;

import java.util.function.Function;

public class ExTerraIngredientsSerializer {

    public static final Codec<ExTerraIngredient<?>> CODEC = ExTerraRegistries.INGREDIENT_SERIALIZER.byNameCodec()
            .dispatch(
                    ExTerraIngredient::type,
                    Function.identity()
            );

}
