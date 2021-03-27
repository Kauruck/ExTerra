package com.kauruck.exterra.Item.Tools;

import com.kauruck.exterra.Init.ModTabs;
import com.kauruck.exterra.Item.GemType;
import com.kauruck.exterra.Util.RegistryHandler;
import net.minecraft.item.*;

public class GemHoe extends HoeItem {

    public GemHoe(GemType tier, float attackSpeed, int idk) {
        super(tier,idk, attackSpeed - 4, new Item.Properties().group( ModTabs.TOOLS));
        RegistryHandler.ITEMS.add(this);
        this.setRegistryName(tier.getRegistryName() + "_hoe");
    }
}
