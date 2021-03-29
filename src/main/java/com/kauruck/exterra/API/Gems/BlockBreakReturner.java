package com.kauruck.exterra.API.Gems;

import net.minecraft.item.ItemStack;

public class BlockBreakReturner {

    public ItemStack dropOverride;
    public boolean addToStatistics;

    public BlockBreakReturner(boolean addToStatistics) {
        this.addToStatistics = addToStatistics;
    }
}
