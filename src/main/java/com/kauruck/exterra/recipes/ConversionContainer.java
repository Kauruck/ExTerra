package com.kauruck.exterra.recipes;

import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.api.recipes.ExTerraRecipeContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ConversionContainer implements ExTerraRecipeContainer<MatterStack> {

    private final Set<MatterStack> matters;
    public ConversionContainer(Set<MatterStack> matters){
        this.matters = matters;
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
}
