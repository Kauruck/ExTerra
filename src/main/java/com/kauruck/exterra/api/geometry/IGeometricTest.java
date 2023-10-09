package com.kauruck.exterra.api.geometry;

import com.google.gson.JsonObject;
import com.kauruck.exterra.geometry.BlockPosHolder;
import net.minecraft.resources.ResourceLocation;

public interface IGeometricTest {

    boolean test(BlockPosHolder blockPos);

    void fromJSON(JsonObject json);

    Character[] expectedBlockPosNames();

    ResourceLocation getName();

    JsonObject toJSON();
}
