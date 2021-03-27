package com.kauruck.exterra.Init;

import com.kauruck.exterra.Tabs.ResourcesTab;
import com.kauruck.exterra.Tabs.ToolsTab;
import net.minecraft.item.ItemGroup;

public class ModTabs {

    public static ItemGroup RESOURCES = null;

    public static ItemGroup TOOLS;

    public ModTabs()
    {
        RESOURCES = new ResourcesTab();
        TOOLS = new ToolsTab();
    }

}
