package com.kauruck.exterra.Tabs;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.Init.ModItems;
import com.kauruck.exterra.Item.GemType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;



public class ResourcesTab extends ItemGroup {


    public ResourcesTab() {
        super(ExTerra.MOD_ID+ ".Resources" );
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModItems.GEMS[GemType.indexOf(GemType.Ruby)]);
    }
}
