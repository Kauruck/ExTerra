package com.kauruck.exterra.geometry;

import com.kauruck.exterra.api.geometry.IGeometricTest;
import com.kauruck.exterra.geometry.builtin.IntersectAngle;
import com.kauruck.exterra.geometry.builtin.ParallelLine;
import com.kauruck.exterra.geometry.elmental.AND;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GeometryParts {

    public static final Map<ResourceLocation, Supplier<IGeometricTest>> GEOMETRIC_TESTS = new HashMap<>();

    public static void addGeometricTest(Supplier<IGeometricTest> test){
        GeometryParts.GEOMETRIC_TESTS.put(test.get().getName(), test);
    }

    public static void initInbuilt(){
        addGeometricTest(IntersectAngle::new);
        addGeometricTest(ParallelLine::new);
        addGeometricTest(AND::new);
    }
}
