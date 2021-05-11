package com.kauruck.exterra.guis.machines;

import com.kauruck.exterra.modules.ExTerraTools;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Container for the GemWorkbench
 *
 *  @author Kauruck
 */
public class GemWorkbenchContainer extends Container {

    private final TileEntity tileEntity;
    private final PlayerEntity playerEntity;
    private final IItemHandler playerInventory;

    private final int slotSize = 19;
    private final int margin = 3;

    /**
     * Create the Container
     * @param windowId The Id
     * @param world The world
     * @param pos The position of the TileEntity (there should also be one)
     * @param playerInventory The inventory of the player
     * @param player The player
     */
    public GemWorkbenchContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(ExTerraTools.GEM_WORKBENCH_CONTAINER.get(), windowId);
        tileEntity = world.getTileEntity(pos);
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 16, 24));
                addSlot(new SlotItemHandler(h, 1, 16 + slotSize + margin, 24));
                addSlot(new SlotItemHandler(h, 2, 16 + slotSize + margin + slotSize + margin, 24));
                addSlot(new SlotItemHandler(h, 3, 16 + slotSize + margin + slotSize + margin + slotSize + margin, 24));
            });
        }

        layoutPlayerInventorySlots(10, 70);
    }


    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private void addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }


    @Override
    @MethodsReturnNonnullByDefault
    @ParametersAreNonnullByDefault
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, ExTerraTools.GEM_WORKBENCH.get());
    }
}
