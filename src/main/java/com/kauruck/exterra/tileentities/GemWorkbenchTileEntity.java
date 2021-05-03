package com.kauruck.exterra.tileentities;

import com.kauruck.exterra.datapacks.DataPackProviders;
import com.kauruck.exterra.modregistreys.ExTerraRegistry;
import com.kauruck.exterra.modules.ExTerraTools;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GemWorkbenchTileEntity extends TileEntity {

    private ItemStackHandler itemStackHandler = createHandler();

    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemStackHandler);

    public GemWorkbenchTileEntity() {
        super(ExTerraTools.GEM_WORKBENCH_TILE_ENTITY.get());
    }


    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        itemStackHandler.deserializeNBT(nbt.getCompound("inv"));
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("inv", itemStackHandler.serializeNBT());
        return super.write(compound);
    }

    private ItemStackHandler createHandler(){
        return new ItemStackHandler(5){
            boolean craftable = false;


            //TODO Fix crash when shift clicking
            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                switch (slot){
                    case 0:
                        if(DataPackProviders.GEM_ITEM_BINDING.contains(stack))
                            return super.insertItem(slot, stack, simulate);
                    case 1:
                        if(ExTerraRegistry.CRAFTING.TOOL_UPGRADING.isUpgradable(stack))
                            return super.insertItem(slot, stack, simulate);
                    case 2:
                        return stack;
                    default:
                        return stack;
                }
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                switch (slot){
                    case 0:
                        return DataPackProviders.GEM_ITEM_BINDING.contains(stack);
                    case 1:
                        return ExTerraRegistry.CRAFTING.TOOL_UPGRADING.isUpgradable(stack);
                    case 2:
                        return false;
                    default:
                        return false;
                }


            }

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
                if(slot == 0 || slot == 1) {
                    ItemStack gem = this.getStackInSlot(0);
                    ItemStack tool = this.getStackInSlot(1);
                    if (!gem.isEmpty() && !tool.isEmpty()) {
                        this.setStackInSlot(2, ExTerraRegistry.CRAFTING.TOOL_UPGRADING.getUpgrade(tool, gem));
                        craftable = true;
                    }
                    else{
                        this.setStackInSlot(2, ItemStack.EMPTY);
                    }
                }
                if(slot == 2){
                    ItemStack product = this.getStackInSlot(2);
                    ItemStack gems = this.getStackInSlot(0);
                    ItemStack tool = this.getStackInSlot(1);
                    if(product.isEmpty() && craftable && !gems.isEmpty() && !tool.isEmpty()) {
                        gems.shrink(1);

                        this.setStackInSlot(0, gems);
                        this.setStackInSlot(1, ItemStack.EMPTY);
                        craftable = false;
                    }
                }
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)){
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }
}
