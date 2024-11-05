package com.kauruck.exterra.api.recipes;

import com.kauruck.exterra.api.exceptions.NoCodecException;
import com.kauruck.exterra.modules.ExTerraRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;

/**
 * Type of a recipe
 * @param <T> The type of all recipes
 * @since 0.1
 * @author Kauruck
 */
public class ExTerraRecipeType<T extends  ExTerraRecipe<?, ?>> {

    private final ResourceLocation loc;
    private final ResourceLocation serializerLocation;

    private MapCodec<ExTerraRecipe<T, ?>> cachedCodec;

    public ExTerraRecipeType(ResourceLocation id, ResourceLocation serializerLocation) {
        this.loc = id;
        this.serializerLocation = serializerLocation;
    }
    /**
     * The id for the type. Used in translation:
     * recipe.(id.namespace).(id.path)
     * @return The Id
     */
    public ResourceLocation typeId(){
        return loc;
    }

    @Override
    public String toString() {
        return loc.toString();
    }

    public ResourceLocation getSerializer() {
        return serializerLocation;
    }

    public MapCodec<ExTerraRecipe<T, ?>> getCodec() {
        if (cachedCodec == null) {
            cachedCodec = (MapCodec<ExTerraRecipe<T, ?>>) ExTerraRegistries.RECIPE_SERIALIZER.get(this.serializerLocation);
            if (cachedCodec == null) {
                throw new NoCodecException(this.serializerLocation);
            }
        }
        return cachedCodec;
    }
}
