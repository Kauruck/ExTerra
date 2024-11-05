package com.kauruck.exterra.recipes;

import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.api.recipes.ExTerraIngredient;
import com.kauruck.exterra.api.recipes.ExTerraRecipe;
import com.kauruck.exterra.data.ShapeData;
import com.kauruck.exterra.geometry.Shape;
import com.kauruck.exterra.ingredients.MatterIngredient;
import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.modules.ExTerraRegistries;
import com.kauruck.exterra.modules.ExTerraReloadableResources;
import com.kauruck.exterra.serializers.ExTerraIngredientsSerializer;
import com.kauruck.exterra.util.StreamHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConversionRecipe extends ExTerraRecipe<MatterStack, ConversionContainer> {

    public static final MapCodec<ConversionRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    NonNullList.codecOf(ResourceLocation.CODEC).fieldOf("shapes").forGetter(ConversionRecipe::getShapeNames),
                    MatterStack.CODEC.fieldOf("output").forGetter(ConversionRecipe::getResult),
                    NonNullList.codecOf(ExTerraIngredientsSerializer.CODEC).fieldOf("input").forGetter(c -> c.getIngredients().stream()
                            .map(i -> (ExTerraIngredient<?>) i).collect(StreamHelper.toNonNullList()))
            ).apply(instance, ConversionRecipe::new));

    private final MatterStack output;
    private final NonNullList<ExTerraIngredient<MatterStack>> ingredients;

    private final NonNullList<ShapeData> shapes;

    private final NonNullList<ResourceLocation> shapeResourceLocation;

    private boolean loadedShapes;

    public ConversionRecipe(NonNullList<ExTerraIngredient<MatterStack>> ingredients, NonNullList<ShapeData> shapes, MatterStack output) {
        super(ExTerraCore.CONVERSION_RECIPE_TYPE.get());
        this.ingredients = ingredients;
        this.output = output;
        this.shapes = shapes;
        this.shapeResourceLocation = shapes.stream()
                .map(ShapeData::getName)
                .collect(StreamHelper.toNonNullList());
        this.loadedShapes = true;
    }

    public ConversionRecipe(NonNullList<ExTerraIngredient<MatterStack>> ingredients, List<ResourceLocation> shapesAsResourceLocation, MatterStack output) {
        super(ExTerraCore.CONVERSION_RECIPE_TYPE.get());
        this.ingredients = ingredients;
        this.output = output;
        this.shapes = NonNullList.create();
        this.shapeResourceLocation = shapesAsResourceLocation.stream().collect(StreamHelper.toNonNullList());
        loadedShapes = false;
    }

    private ConversionRecipe(NonNullList<ResourceLocation> shapesAsResourceLocation, MatterStack output, NonNullList<ExTerraIngredient<?>> ingredients) {
        super(ExTerraCore.CONVERSION_RECIPE_TYPE.get());
        this.ingredients = ingredients.stream().map(i -> (ExTerraIngredient<MatterStack>) i).collect(StreamHelper.toNonNullList());
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
        for(ShapeData currentShape : this.shapes){
            if(!container.isShapePresent(currentShape))
                return false;
        }
        return testIngredient(container);
    }

    private boolean testIngredient(ConversionContainer container) {
        for(ExTerraIngredient<MatterStack> currentIngredient : ingredients){
            if(!container.testIngredient(currentIngredient)){
                return false;
            }
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

    public boolean canBeCraftedWith(List<Matter> matters, List<Shape> shapes) {
        List<ShapeData> presentShapes = shapes.stream()
                        .map(Shape::getShapeData)
                        .toList();
        loadShapes();
        for(ShapeData currentShape : this.shapes){
            if(!presentShapes.contains(currentShape))
                return false;
        }
        for(ExTerraIngredient<MatterStack> currentIngredient : ingredients) {
            if(currentIngredient instanceof MatterIngredient matterIngredient) {
                if(!matterIngredient.testMatters(matters)){
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public MatterStack assemble(ConversionContainer container) {
        return output.copy();
    }

    public MatterStack assembleAsMuchAsPossible(ConversionContainer container) {
        MatterStack outputCumulated = new MatterStack(output.getMatter(), 0);
        while (testIngredient(container)) {
            outputCumulated.addMatter(output.getAmount());
            for (ExTerraIngredient<MatterStack> ingredient : ingredients) {
                if(ingredient instanceof MatterIngredient matterIngredient) {
                    container.reduceByIngredient(matterIngredient);
                }
            }
        }
        return outputCumulated;
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

    public NonNullList<ResourceLocation> getShapeNames() {
        return this.shapeResourceLocation;
    }

    public NonNullList<Matter> getUsedMatters() {
        return ingredients.stream()
                .filter(MatterIngredient.class::isInstance)
                .map(MatterIngredient.class::cast)
                .map(MatterIngredient::getStack)
                .map(MatterStack::getMatter)
                .collect(StreamHelper.toNonNullList());
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
