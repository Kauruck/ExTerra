package com.kauruck.exterra.geometry;

import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class ShapeCollection {

    private List<Shape> shapes = new ArrayList<>();

    public ShapeCollection() {

    }

    public ShapeCollection(List<Shape> shapes) {
        this.shapes = shapes;
    }

    public ShapeCollection(CompoundTag tag){
        this.formNBT(tag);
    }

    public void formNBT(CompoundTag tag){
        int size = tag.getInt("size");
        this.shapes.clear();
        for(int i = 0; i < size; i++){
            Shape shape = new Shape(tag.getCompound(Integer.toString(i)));
            this.shapes.add(shape);
        }
    }

    public CompoundTag toNBT(){
        CompoundTag tag = new CompoundTag();
        tag.putInt("size", shapes.size());
        for(int i = 0; i < shapes.size(); i++) {
            tag.put(Integer.toString(i), shapes.get(i).toNBT());
        }
        return tag;
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes;
    }
}
