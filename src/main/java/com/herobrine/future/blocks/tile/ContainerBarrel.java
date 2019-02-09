package com.herobrine.future.blocks.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerBarrel extends Container {
    public TileEntityBarrel te;
    public static IInventory playerInv;

    public ContainerBarrel(IInventory playerInventory, TileEntityBarrel te) {
        this.te = te;
        this.playerInv = playerInventory;
        addOwnSlots();
        addPlayerSlots(playerInventory);
    }

    private void addPlayerSlots(IInventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 9 + col * 18 - 1;
                int y = row * 18 + 70 + 14;
                this.func_75146_a(new Slot(playerInventory, col + row * 9 + 9, x, y));
            }
        }

        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = 9 + row * 18 - 1;
            int y = 58 + 70 + 14;
            this.func_75146_a(new Slot(playerInventory, row, x, y));
        }
    }

    private void addOwnSlots() {
        IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        int slotIndex = 0;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 8 + col * 18;
                int y = 18 + row * 18;
                func_75146_a(new SlotItemHandler(itemHandler, slotIndex, x, y));
                slotIndex++;
            }
        }
    }

    @Override
    public ItemStack func_82846_b(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.field_190927_a;
        Slot slot = this.field_75151_b.get(index);

        if (slot != null && slot.func_75216_d()) {
            ItemStack itemstack1 = slot.func_75211_c();
            itemstack = itemstack1.func_77946_l();

            if (index < TileEntityBarrel.SIZE) {
                if (!this.func_75135_a(itemstack1, TileEntityBarrel.SIZE, this.field_75151_b.size(), true)) {
                    return ItemStack.field_190927_a;
                }
            } else if (!this.func_75135_a(itemstack1, 0, TileEntityBarrel.SIZE, false)) {
                return ItemStack.field_190927_a;
            }

            if (itemstack1.func_190926_b()) {
                slot.func_75215_d(ItemStack.field_190927_a);
            } else {
                slot.func_75218_e();
            }
        }
        return itemstack;
    }

    @Override
    public boolean func_75145_c(EntityPlayer playerIn) {
        return te.canInteractWith(playerIn);
    }
}
