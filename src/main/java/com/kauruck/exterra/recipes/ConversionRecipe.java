package com.kauruck.exterra.recipes;

import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.api.recipes.ExTerraIngredient;
import com.kauruck.exterra.api.recipes.ExTerraRecipe;
import com.kauruck.exterra.data.ShapeData;
import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.modules.ExTerraReloadableResources;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConversionRecipe extends ExTerraRecipe<MatterStack, ConversionContainer> {

    private final MatterStack output;
    private final NonNullList<ExTerraIngredient<MatterStack>> ingredients;

    private final NonNullList<ShapeData> shapes;

    private final List<ResourceLocation> shapeResourceLocation;

    private boolean loadedShapes;

    public ConversionRecipe(ResourceLocation id, NonNullList<ExTerraIngredient<MatterStack>> ingredients, NonNullList<ShapeData> shapes, MatterStack output) {
        super(id, ExTerraCore.CONVERSION_RECIPE_TYPE.get());
        this.ingredients = ingredients;
        this.output = output;
        this.shapes = shapes;
        this.shapeResourceLocation = shapes.stream()
                .map(ShapeData::getName)
                .collect(Collectors.toList());
        this.loadedShapes = true;
    }

    public ConversionRecipe(ResourceLocation id, NonNullList<ExTerraIngredient<MatterStack>> ingredients, List<ResourceLocation> shapesAsResourceLocation, MatterStack output) {
        super(id, ExTerraCore.CONVERSION_RECIPE_TYPE.get());
        this.ingredients = ingredients;
        this.output = output;
        this.shapes = NonNullList.create();
        this.shapeResourceLocation = shapesAsResourceLocation;
        loadedShapes = false;
    }

    /*
     * The shapes might not be present at loading.
     * Hence, we load all them here
     */
    private void loadShapes(){
        if(loadedShapes)
            return;
        for(ResourceLocation currentShape : shapeResourceLocation){
            ShapeData shape = ExTerraReloadableResources.INSTANCE.getShape(currentShape);
            if(shape == null){
                ExTerraReloadableResources.LOGGER.warn("Shape {} for conversion recipe {} does not exist!", currentShape, this.getId());
                continue;
            }
            shapes.add(shape);
        }
        loadedShapes = true;

    }

    @Override
    public NonNullList<MatterStack> getRemainder(ConversionContainer container) {
        return NonNullList.create();
    }

    @Override
    public boolean matches(ConversionContainer container, Level pLevel) {
        loadShapes();
        for(ExTerraIngredient<MatterStack> currentIngredient : ingredients){
            if(!container.testIngredient(currentIngredient)){
                return false;
            }
        }
        for(ShapeData currentShape : this.shapes){
            if(!container.isShapePresent(currentShape))
                return false;
        }
        return true;
    }

    @Override
    public boolean canBeCraftedInContainer(ConversionContainer container) {
        loadShapes();
        for(ShapeData currentShape : this.shapes){
            if(!container.isShapePresent(currentShape))
                return false;
        }
        return true;
    }

    @Override
    public MatterStack assemble(ConversionContainer container) {
        return output.copy();
    }

    @Override
    public MatterStack getResult() {
        return output;
    }

    @Override
    public ItemStack getToastSymbol() {
        return ExTerraCore.RITUAL_STONE_ITEM.get().getDefaultInstance();
    }

    @Override
    public NonNullList<ExTerraIngredient<MatterStack>> getIngredients() {
        return ingredients;
    }

    @Override
    public String getGroup() {
        return "matter";
    }

    /**
     * Do not call this, before the game has loaded all data
     * @return
     */
    public NonNullList<ShapeData> getShapes(){
        loadShapes();
        return this.shapes;
    }
}
