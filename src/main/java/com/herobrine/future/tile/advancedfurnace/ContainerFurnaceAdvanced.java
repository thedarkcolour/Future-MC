package com.herobrine.future.tile.advancedfurnace;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerFurnaceAdvanced extends Container {
    public final TileFurnaceAdvanced te;
    public final InventoryPlayer playerInventory;
    protected int fuelLeft, progress, currentItemBurnTime;

    public ContainerFurnaceAdvanced(InventoryPlayer playerInv, TileFurnaceAdvanced te) {
        this.te = te;
        this.playerInventory = playerInv;
        addOwnSlots();
        addPlayerSlots(playerInv);
    }

    private void addOwnSlots() {
        this.addSlotToContainer(new Slot(te, 0, 56, 17));
        this.addSlotToContainer(new SlotFurnaceFuel(te, 1, 56, 53));
        this.addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, te, 2, 116, 35));
    }

    private void addPlayerSlots(IInventory playerInv) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) { // Inventory
                int x = 9 + col * 18 - 1;
                int y = row * 18 + 70 + 14;
                this.addSlotToContainer(new Slot(playerInv, col + row * 9 + 9, x, y));
            }
        }

        for (int col = 0; col < 9; ++col) { // Hotbar
            int x = 9 + col * 18 - 1;
            int y = 58 + 70 + 14;
            this.addSlotToContainer(new Slot(playerInv, col, x, y));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return playerIn.getDistanceSq(te.getPos().add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener listener : listeners) {
            listener.sendWindowProperty(this, 0, te.getField(0));
            listener.sendWindowProperty(this, 1, te.getField(1));
            listener.sendWindowProperty(this, 2, te.getField(2));
        }

        this.fuelLeft = te.getField(0);
        this.currentItemBurnTime = te.getField(1);
        this.progress = te.getField(2);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        te.setField(id, data);
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