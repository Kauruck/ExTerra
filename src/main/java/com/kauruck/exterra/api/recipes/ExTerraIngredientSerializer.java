package com.kauruck.exterra.api.recipes;

import com.mojang.serialization.Codec;

public interface ExTerraIngredientSerializer<R extends ExTerraIngredient<?>> {

    Codec<R> getCodec();
    
}
