package com.kauruck.exterra.Init;


import com.kauruck.exterra.TileEntities.GemWorkbenchTileEntity;
import com.kauruck.exterra.Util.ModFactory;
import net.minecraft.tileentity.TileEntityType;

public class ModTileEntities {

    public static final TileEntityType<GemWorkbenchTileEntity> GEM_WORKBENCH = ModFactory.createTileEntityType("gem_workbench" , GemWorkbenchTileEntity::new, ModBlocks.GEM_WORKBENCH);


}
