package com.kauruck.exterra.data.provider;

import com.kauruck.exterra.api.geometry.ElementalOperation;
import com.kauruck.exterra.api.geometry.IGeometricTest;
import com.kauruck.exterra.data.ShapeData;
import com.kauruck.exterra.geometry.builtin.IntersectAngle;
import com.kauruck.exterra.geometry.builtin.ParallelLine;
import com.kauruck.exterra.geometry.elmental.AND;

import java.util.ArrayList;
import java.util.List;

public class ShapeDataBuilder {

    private final String name;
    private final int numberOfPoints;
    private final List<IGeometricTest> parts = new ArrayList<>();
    private final ShapeDataBuilder superBuilder;
    private final IGeometricTest targetTest;

    public ShapeDataBuilder(String name, int numberOfPoints) {
        this.name = name;
        this.numberOfPoints = numberOfPoints;
        this.superBuilder = null;
        this.targetTest = null;
    }

    public ShapeDataBuilder(ShapeDataBuilder superBuilder, IGeometricTest targetTest) {
        name = "";
        numberOfPoints = 0;
        this.superBuilder = superBuilder;
        this.targetTest = targetTest;
    }

    public ShapeData build(){
        ShapeData data = new ShapeData(this.name, this.numberOfPoints);
        data.setParts(this.parts);
        return data;
    }

    public ShapeDataBuilder addPart(IGeometricTest part){
        parts.add(part);
        return this;
    }

    public ShapeDataBuilder parallel(Character pointA, Character pointB, Character pointC, Character pointD){
        ParallelLine parallel = new ParallelLine(pointA, pointB, pointC, pointD);
        this.addPart(parallel);
        return this;
    }

    public ShapeDataBuilder parallel(Character pointA, Character pointB, Character pointC, Character pointD, float epsilon){
        ParallelLine parallel = new ParallelLine(pointA, pointB, pointC, pointD);
        parallel.setEpsilon(epsilon);
        this.addPart(parallel);
        return this;
    }

    public ShapeDataBuilder intersectAngle(Character pointA, Character pointB, Character pointC, float angle){
        IntersectAngle intersectAngle = new IntersectAngle(pointA, pointB, pointC, angle);
        this.addPart(intersectAngle);
        return this;
    }

    public ShapeDataBuilder intersectAngle(Character pointA, Character pointB, Character pointC, float angle, float epsilon){
        IntersectAngle intersectAngle = new IntersectAngle(pointA, pointB, pointC, angle);
        intersectAngle.setEpsilon(epsilon);
        this.addPart(intersectAngle);
        return this;
    }

    //--------[Elemental Operations]--------
    public ShapeDataBuilder and(){
        IGeometricTest test = new AND();
        return new ShapeDataBuilder(this, test);
    }

    public ShapeDataBuilder end(){
        if(superBuilder == null || targetTest == null)
            throw new IllegalStateException("There is no Elemental Operation to end");
        if(targetTest instanceof ElementalOperation eo){
            eo.setParts(this.parts);
        }
        return superBuilder;
    }
}
