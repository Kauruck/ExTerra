package com.kauruck.exterra.Block.Machines;

import com.kauruck.exterra.Block.BlockBase;
import com.kauruck.exterra.GUIs.Machines.GemWorkbenchContainer;
import com.kauruck.exterra.Init.ModTileEntities;
import com.kauruck.exterra.TileEntities.GemWorkbenchTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.extensions.IForgeBlock;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class GemWorkbench extends BlockBase implements IForgeBlock {
    public GemWorkbench(String name, float hardness, Material material, ItemGroup itemGroup, ToolType harvestTool, int harvestLevel) {
        super(name, hardness, material, itemGroup, harvestTool, harvestLevel);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.GEM_WORKBENCH.create();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isRemote()){
            TileEntity te = worldIn.getTileEntity(pos);
            if(te instanceof GemWorkbenchTileEntity){
                INamedContainerProvider containerProvider = new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName() {
                        return new TranslationTextComponent("screen.exterra.gem_workbench");
                    }

                    @Nullable
                    @Override
                    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                        return new GemWorkbenchContainer(i, worldIn, pos, playerInventory, playerEntity);
                    }
                };
                NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, te.getPos());
            }
            else {
                throw new IllegalStateException("Something went wrong: Error Code GW:66");
            }
        }

        return ActionResultType.SUCCESS;
    }
}
