package com.kauruck.exterra.geometry;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.data.ShapeData;
import com.kauruck.exterra.data.loader.ShapeReloadListener;
import com.kauruck.exterra.util.NBTUtil;
import com.kauruck.exterra.util.PositionsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Shape implements Iterable<BlockPos>{

    public static String TAG_SHAPE_DATA = "shapeData";

    List<BlockPos> positions = new ArrayList<>();

    private ShapeData shapeData;

    public Shape(){

    }

    public Shape(CompoundTag nbt){
        this.fromNBT(nbt);
    }

    public boolean end(){
        for(String currentKey : ShapeReloadListener.shapes.keySet()){
            ShapeData currentShape = ShapeReloadListener.shapes.get(currentKey);
            BlockPosHolder holder = new BlockPosHolder(this.positions.toArray(new BlockPos[0]));
            if(currentShape.test(holder)){
                shapeData = currentShape;
                return true;
            }
        }
        return false;
    }

    public void addPoint(BlockPos point){
        positions.add(point);
    }

    public boolean containsPoint(BlockPos point){
        return positions.contains(point);
    }

    @Override
    public String toString() {
        if(shapeData == null)
            return "contains: " + positions.size() + " positions";
        else
            return shapeData.getName();
    }

    public void fromNBT(CompoundTag tag){
        int size = tag.getInt("size");
        this.positions.clear();
        for(int i = 0; i < size; i++){
            BlockPos pos = NBTUtil.blockPosFromNBT(tag.getCompound(Integer.toString(i)));
            this.positions.add(pos);
        }
        if(tag.contains(TAG_SHAPE_DATA)){
            String shapeName = tag.getString(TAG_SHAPE_DATA);
            if(ShapeReloadListener.shapes.containsKey(shapeName))
                shapeData = ShapeReloadListener.shapes.get(shapeName);
        }
    }


    public CompoundTag toNBT(){
        CompoundTag tag = new CompoundTag();
        tag.putInt("size", positions.size());
        for(int i = 0; i < positions.size(); i++){
            tag.put(Integer.toString(i), NBTUtil.blockPosToNBT(positions.get(i)));
        }
        if(shapeData != null)
            tag.putString(TAG_SHAPE_DATA, shapeData.getName());
        return tag;
    }



    public List<BlockPos> getActualPositions(BlockPos center) {
        List<BlockPos> out = new ArrayList<>();
        for(BlockPos current : positions){
            out.add(current.offset(center));
        }
        return out;
    }

    public BlockPos get(int index){
        return positions.get(index);
    }

    public int length(){
        return positions.size();
    }

    @NotNull
    @Override
    public Iterator<BlockPos> iterator() {
        return this.positions.iterator();
    }

}
