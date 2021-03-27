package com.kauruck.exterra.Item;

import com.kauruck.exterra.Init.ModTabs;

public class GemItem extends ItemBase{

    public GemItem(GemType gemType) {
        super(gemType.getRegistryName(), ModTabs.RESOURCES);
    }
}
