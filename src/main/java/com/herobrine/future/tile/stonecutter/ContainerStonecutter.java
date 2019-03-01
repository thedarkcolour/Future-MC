package com.herobrine.future.tile.stonecutter;

import com.herobrine.future.utils.config.FutureConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerStonecutter extends Container {
    public static TileEntityStonecutter te;
    public static int idSlot = -1;

    public ContainerStonecutter(IInventory playerInventory, TileEntityStonecutter te) {
        ContainerStonecutter.te = te;
        addOwnSlots();
        addPlayerSlots(playerInventory);
    }

    private void addPlayerSlots(IInventory playerInv) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {//Inventory
                int x = 9 + col * 18 - 1;
                int y = row * 18 + 70 + 14;
                this.addSlotToContainer(new Slot(playerInv, col + row * 9 + 9, x, y));
            }
        }

        for (int row = 0; row < 9; ++row) {//Hotbar
            int x = 9 + row * 18 - 1;
            int y = 58 + 70 + 14;
            this.addSlotToContainer(new Slot(playerInv, row, x, y));
        }
    }

    private void addOwnSlots() {
        IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        addSlotToContainer(new SlotItemHandler(handler, 0, 20, 33)); //Input
        //noinspection unused
        addSlotToContainer(new SlotItemHandler(handler, 1, 143, 33) {//Output
            private int removeCount;
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }

            @Override
            public ItemStack decrStackSize(int amount) {
                if (this.getHasStack()) {
                    this.removeCount += Math.min(amount, this.getStack().getCount());
                }
                return super.decrStackSize(amount);
            }

            @Override
            public ItemStack onTake(EntityPlayer playerIn, ItemStack stack) {
                this.onCrafting(stack);
                super.onTake(playerIn, stack);
                return stack;
            }
        });
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (FutureConfig.c.debug) {
            System.out.println(index);
        }

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index > 1) {
                if (!this.mergeItemStack(itemstack1, 36, 38, true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 2, 36, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }
            else {
                slot.onSlotChanged();
            }
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
        }
        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return te.canInteractWith(playerIn);
    }

    public int nextID() {
        return idSlot++;
    }
}