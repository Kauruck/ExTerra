package com.kauruck.exterra.Init;

import com.kauruck.exterra.GUIs.Machines.GemWorkbenchContainer;
import com.kauruck.exterra.GUIs.Machines.GemWorkbenchScreen;
import com.kauruck.exterra.Util.ModFactory;
import net.minecraft.inventory.container.ContainerType;

public class ModContainers {
    public static final ContainerType<?> GEM_WORKBENCH = ModFactory.createContainerType("gem_workbench", GemWorkbenchContainer::new, GemWorkbenchScreen::new);
}
