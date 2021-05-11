package com.kauruck.exterra.tabs;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.modules.ExTerraGems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;


/**
 * The class providing the ResourceTab
 *
 * @author Kauruck
 */
public class ResourcesTab extends ItemGroup {


    public ResourcesTab() {
        super(ExTerra.MOD_ID+ ".Resources" );
    }

    @Override
    public ItemStack createIcon() {
        return ExTerraGems.RUBY_GEM.get().getDefaultInstance();
    }
}
