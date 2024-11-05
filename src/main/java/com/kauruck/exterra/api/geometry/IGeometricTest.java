package com.kauruck.exterra.api.geometry;

import com.google.gson.JsonObject;
import com.kauruck.exterra.geometry.BlockPosHolder;
import com.kauruck.exterra.modules.ExTerraRegistries;
import net.minecraft.resources.ResourceLocation;

public interface IGeometricTest {

    boolean test(BlockPosHolder blockPos);

    Character[] expectedBlockPosNames();

    ResourceLocation getName();

    ResourceLocation getSerializerLocation();

    default IGeometricSerializer getSerializer() {
        return ExTerraRegistries.GEOMETRIC_SERIALIZER.get(getSerializerLocation());
    }
}
