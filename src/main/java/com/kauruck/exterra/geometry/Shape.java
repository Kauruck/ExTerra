package com.kauruck.exterra.geometry;

import com.kauruck.exterra.data.ShapeData;
import com.kauruck.exterra.modules.ExTerraReloadableResources;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Shape implements Iterable<BlockPos>{

    public static final Codec<Shape> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                Codec.list(BlockPos.CODEC).fieldOf("positions").forGetter(Shape::getPositions),
                ShapeData.CODEC.optionalFieldOf("shapeData", null).forGetter(Shape::getShapeData)
        ).apply(instance, Shape::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Shape> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()),
            Shape::getPositions,
            ShapeData.STREAM_CODEC.apply(ByteBufCodecs::optional),
            Shape::getShapeDataOptional,
            Shape::new
    );

    List<BlockPos> positions = new ArrayList<>();

    private ShapeData shapeData;

    public Shape(){

    }

    private List<BlockPos> getPositions() {
        return positions;
    }

    public Shape(List<BlockPos> positions, ShapeData shapeData) {
        this.positions = positions;
        this.shapeData = shapeData;
    }

    private Shape(List<BlockPos> positions, Optional<ShapeData> shapeData) {
        this.positions = positions;
        this.shapeData = shapeData.orElse(null);
    }

    public boolean end(){
        for(ResourceLocation currentKey : ExTerraReloadableResources.INSTANCE.getShapes().keySet()){
            ShapeData currentShape = ExTerraReloadableResources.INSTANCE.getShape(currentKey);
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
            return shapeData.getTranslationKey();
    }

    public TranslatableContents getTranslation(){
        if(shapeData == null)
            return new TranslatableContents("shape.exterra.unfinshed.size", "Shape with %s positions",
                    new Object[]{this.positions.size()});
        else
            return new TranslatableContents(shapeData.getTranslationKey(), shapeData.getTranslationKey(), TranslatableContents.NO_ARGS);
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

    public ShapeData getShapeData() {
        return this.shapeData;
    }

    private Optional<ShapeData> getShapeDataOptional() {
        return Optional.ofNullable(this.shapeData);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shape blockPos)) return false;

        return Objects.equals(positions, blockPos.positions) && Objects.equals(shapeData, blockPos.shapeData);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(positions);
        result = 31 * result + Objects.hashCode(shapeData);
        return result;
    }
}
