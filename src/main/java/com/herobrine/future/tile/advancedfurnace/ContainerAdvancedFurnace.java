package com.herobrine.future.tile.advancedfurnace;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class    ContainerAdvancedFurnace extends Container {
    public final TileAdvancedFurnace te;
    public final InventoryPlayer playerInventory;
    protected int fuelLeft, progress;
    private ItemStack clientFuel = ItemStack.EMPTY;

    public ContainerAdvancedFurnace(InventoryPlayer playerInv, TileAdvancedFurnace te) {
        this.te = te;
        this.playerInventory = playerInv;
        addOwnSlots();
        addPlayerSlots(playerInv);
    }

    private void addOwnSlots() {
        IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        addSlotToContainer(new SlotItemHandler(handler, 0, 56, 17));
        addSlotToContainer(new SlotItemHandler(handler, 1, 56, 53));
        addSlotToContainer(new SlotItemHandler(handler, 2, 116, 35));
    }

    private void addPlayerSlots(IInventory playerInv) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) { // Inventory
                int x = 9 + col * 18 - 1;
                int y = row * 18 + 70 + 14;
                this.addSlotToContainer(new Slot(playerInv, col + row * 9 + 9, x, y));
            }
        }

        for (int row = 0; row < 9; ++row) { // Hotbar
            int x = 9 + row * 18 - 1;
            int y = 58 + 70 + 14;
            this.addSlotToContainer(new Slot(playerInv, row, x, y));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return te.canInteractWith(playerIn);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener listener : listeners) {
            listener.sendWindowProperty(this, 0, te.fuelLeft);
            listener.sendWindowProperty(this, 2, te.progress);
        }

        this.fuelLeft = te.fuelLeft;
        this.progress = te.progress;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        switch (id) {
            case 0: {
                te.fuelLeft = data;
            }
            case 2: {
                te.progress = data;
            }
        }
    }

    protected int getScaledFire() {
        int progress = te.fuelLeft;
        updateClientFuel();

        int maxProgress = TileEntityFurnace.getItemBurnTime(clientFuel);
        if(maxProgress == 0) {
            maxProgress = progress;
        }
        int i = maxProgress != progress && progress != 0 ? progress * 13 / maxProgress : 0;
        if(i > 13) {
            return 13;
        }
        return i;
    }

    protected int getScaledArrow() {
        int progress = te.progress;
        int maxProgress = 100;
        int i = maxProgress != progress && progress != 0 ? progress * 24 / maxProgress : 0;
        if(i > 24) {
            return 24;
        }
        return i;
    }

    protected boolean isBurning() {
        return te.fuelLeft > 0;
    }

    protected void updateClientFuel() {
        if(te.fuelStack() != clientFuel) {
            if(te.fuelStack() != ItemStack.EMPTY && te.fuelStack() != null) {
                clientFuel = te.fuelStack();
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) { // Re-uses the transferStackInSlot from ContainerFurnace
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemstack = itemStack1.copy();

            if (index == 2) {
                if (!this.mergeItemStack(itemStack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemStack1, itemstack);
            }
            else if (index != 1 && index != 0) {
                if (!FurnaceRecipes.instance().getSmeltingResult(itemStack1).isEmpty()) {
                    if (!this.mergeItemStack(itemStack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (TileEntityFurnace.isItemFuel(itemStack1)) {
                    if (!this.mergeItemStack(itemStack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index < 30) {
                    if (!this.mergeItemStack(itemStack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index < 39 && !this.mergeItemStack(itemStack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemStack1, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }
            else {
                slot.onSlotChanged();
            }

            if (itemStack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemStack1);
        }
        return itemstack;
    }
}