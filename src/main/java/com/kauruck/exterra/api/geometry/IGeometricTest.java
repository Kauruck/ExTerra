package com.kauruck.exterra.api.geometry;

import com.google.gson.JsonObject;
import com.kauruck.exterra.geometry.BlockPosHolder;

public interface IGeometricTest {

    boolean test(BlockPosHolder blockPos);

    void fromJSON(JsonObject json);

    Character[] expectedBlockPosNames();

    String getName();

    JsonObject toJSON();
}
