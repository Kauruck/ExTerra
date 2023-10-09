package com.kauruck.exterra.recipes;

import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.api.recipes.ExTerraRecipeContainer;
import com.kauruck.exterra.data.ShapeData;
import com.kauruck.exterra.geometry.Shape;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ConversionContainer implements ExTerraRecipeContainer<MatterStack> {

    private final Set<MatterStack> matters;
    private final Set<ShapeData> presentShapes = new HashSet<>();
    public ConversionContainer(Set<MatterStack> matters, Set<Shape> presentShapes){
        this.matters = matters;
        for(Shape currentShape : presentShapes){
            this.presentShapes.add(currentShape.getShapeData());
        }
    }
    @Override
    public int getSize() {
        return matters.size();
    }

    @Override
    public MatterStack getEmpty() {
        return MatterStack.EMPTY;
    }

    @Override
    public boolean isOrdered() {
        return false;
    }

    @Override
    public MatterStack getAt(int index) {
        throw new UnsupportedOperationException("This container is unordered");
    }

    @Override
    public Collection<MatterStack> getAll() {
        return matters;
    }

    @NotNull
    @Override
    public Iterator<MatterStack> iterator() {
        return matters.iterator();
    }

    // Additional information
    public boolean isShapePresent(ShapeData shape){
        return presentShapes.contains(shape);
    }
}
