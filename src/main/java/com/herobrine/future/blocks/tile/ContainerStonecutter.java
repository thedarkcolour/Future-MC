package com.herobrine.future.blocks.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerStonecutter extends Container {
    public static IInventory playerInv;
    public static TileEntityStonecutter te;
    public static final IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

    /**@SideOnly(Side.CLIENT)
    public ContainerStonecutter(IInventory playerInv, World worldIn) {
        this(playerInv, worldIn, BlockPos.ORIGIN);
    }*/

    public ContainerStonecutter(IInventory playerInv, World worldIn, BlockPos pos) {
        this.playerInv = playerInv;
        //this.te = te;
        addPlayerSlots(playerInv);

        this.func_75146_a(new SlotItemHandler(handler, 0, 20, 33));
        this.func_75146_a(new SlotStonecutterOutput(handler, 1, 143, 33));
    }

    private void addPlayerSlots(IInventory playerInv) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {//Inventory
                int x = 9 + col * 18 - 1;
                int y = row * 18 + 70 + 14;
                this.func_75146_a(new Slot(playerInv, col + row * 9 + 9, x, y));
            }
        }

        for (int row = 0; row < 9; ++row) {//Hotbar
            int x = 9 + row * 18 - 1;
            int y = 58 + 70 + 14;
            this.func_75146_a(new Slot(playerInv, row, x, y));
        }
    }

    @Override
    public ItemStack func_82846_b(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.field_190927_a;
        Slot slot = this.field_75151_b.get(index);

        if (slot != null && slot.func_75216_d()) {
            ItemStack itemstack1 = slot.func_75211_c();
            itemstack = itemstack1.func_77946_l();

            if (index == 0) {
                if (!this.func_75135_a(itemstack1, 2, 38, true)) {
                    return ItemStack.field_190927_a;
                }
            } else if (index == 1) {
                if (!this.func_75135_a(itemstack1, 2, 38, true)) {
                    return ItemStack.field_190927_a;
                }
            }

            if (index == 0 || index == 1) {
                if(index == 1) {
                    if (!this.func_75135_a(itemstack1, 2, 38, true)) {
                        return ItemStack.field_190927_a;
                    }
                }

                if(index == 0) {
                    if(!this.func_75135_a(itemstack1, 2, 38, true)) {
                        return ItemStack.field_190927_a;
                    }
                }
            } else if (!this.func_75135_a(itemstack1, 0, 2, false)) {
                return ItemStack.field_190927_a;
            }

            if (itemstack1.func_190926_b()) {
                slot.func_75215_d(ItemStack.field_190927_a);
            } else {
                slot.func_75218_e();
            }

            if (itemstack1.func_190916_E() == itemstack.func_190916_E()) {
                return ItemStack.field_190927_a;
            }

            slot.func_190901_a(playerIn, itemstack1);
        }
        return itemstack;
    }

    @Override
    public boolean func_75145_c(EntityPlayer playerIn) {
        return this.te.canInteractWith(playerIn);
    }
}
