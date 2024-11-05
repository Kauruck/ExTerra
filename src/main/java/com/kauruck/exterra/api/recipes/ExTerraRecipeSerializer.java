package com.kauruck.exterra.api.recipes;

import com.mojang.serialization.Codec;
public interface ExTerraRecipeSerializer<T extends ExTerraRecipe<?, ?>> {

    Codec<T> getCodec();
}
