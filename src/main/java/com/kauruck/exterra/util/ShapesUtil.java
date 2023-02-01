package com.kauruck.exterra.util;

import com.kauruck.exterra.geometry.Shape;
import net.minecraft.core.BlockPos;

import java.util.Collection;
import java.util.List;

public class ShapesUtil {
    public static boolean containsBlockPos(List<Shape> shapes, BlockPos pos, BlockPos center){
        return shapes.stream()
                .map(shape -> shape.getActualPositions(center))
                .flatMap(Collection::stream)
                .anyMatch(position -> pos == position);
    }
}
