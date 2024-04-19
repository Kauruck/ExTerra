package com.kauruck.exterra.recipes;

import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.geometry.Shape;
import com.kauruck.exterra.modules.ExTerraCore;
import net.minecraft.util.Tuple;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConversionHelper {

    /**
     * Convert all possible matters with the conversion recipes.
     * The returned list contains all converted matters ond all maters who can not be converted.
     *
     * @param inputMatters all matters that could be converted
     * @param shapes shapes present for converting
     * @return combined list of converted matters and non-used matters
     */
    public static Tuple<Matter, Matter[]>[] convertWithConversion(Matter[] inputMatters, List<Shape> shapes) {
        List<ConversionRecipe> allRecipes = ExTerraCore.CONVERSION_RECIPE_MANGER.get().getAllRecipesFor(ExTerraCore.CONVERSION_RECIPE_TYPE.get());
        Set<Tuple<Matter, Matter[]>> fromConversion = new HashSet<>();
        Set<Matter> usedForConversion = new HashSet<>();
        for(ConversionRecipe currentRecipe : allRecipes) {
            if (currentRecipe.canBeCraftedWith(List.of(inputMatters), shapes)) {
                List<Matter> used = currentRecipe.getUsedMatters();
                fromConversion.add(new Tuple<>(currentRecipe.getResult().getMatter(), used.toArray(Matter[]::new)));
                usedForConversion.addAll(used);
            }
        }
        List<Matter> remainingMatter = new ArrayList<>(List.of(inputMatters));
        remainingMatter.removeAll(usedForConversion);
        fromConversion.addAll(remainingMatter.stream()
                .map(m -> new Tuple<>(m, new Matter[]{m}))
                .toList());

        return fromConversion.toArray(new Tuple[0]);
    }
}
