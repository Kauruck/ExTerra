package com.kauruck.exterra.api.recipes;

import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.function.Predicate;

public abstract class ExTerraIngredient<T> implements Predicate<T> {
    public abstract ResourceLocation getSerializerLocation();
}
