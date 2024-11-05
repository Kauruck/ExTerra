package com.kauruck.exterra.api.recipes;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.function.Predicate;

public abstract class ExTerraIngredient<T> implements Predicate<T> {

    public abstract MapCodec<? extends ExTerraIngredient<T>> type();
}
