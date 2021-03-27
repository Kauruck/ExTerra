package com.kauruck.exterra.TileEntities;

import com.kauruck.exterra.Init.ModItems;
import com.kauruck.exterra.Init.ModTileEntities;
import com.kauruck.exterra.Item.GemType;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GemWorkbenchTileEntity extends TileEntity implements ITickableTileEntity {

    private ItemStackHandler itemStackHandler = createHandler();

    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemStackHandler);

    public GemWorkbenchTileEntity() {
        super(ModTileEntities.GEM_WORKBENCH);
    }

    @Override
    public void remove() {
        super.remove();
        handler.invalidate();
    }

    @Override
    public void tick() {

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
            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                switch (slot){
                    case 0:
                        if(stack.getItem() == ModItems.GEMS[GemType.indexOf(GemType.Ruby)])
                        return super.insertItem(slot, stack, simulate);
                    default:
                        return stack;
                }
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                switch (slot){
                    case 0:
                        return stack.getItem() == ModItems.GEMS[GemType.indexOf(GemType.Ruby)];
                    default:
                        return false;
                }
            }

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
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
