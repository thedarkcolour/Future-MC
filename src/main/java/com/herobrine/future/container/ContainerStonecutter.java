package com.herobrine.future.container;

import com.herobrine.future.init.Init;
import com.herobrine.future.item.crafting.stonecutter.RecipeResults;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;

public class ContainerStonecutter extends Container {
    protected InventoryPlayer playerInv;
    protected World world;
    protected BlockPos pos;
    protected int selectedID;

    public ItemStackHandler input = new ItemStackHandler() {
        @Override
        protected void onContentsChanged(int slot) {
            if (getStackInSlot(0).isEmpty()) {
                setSelectedID(0);
            }
        }
    };
    public ItemStackHandler output = new ItemStackHandler() {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return false;
        }
    };

    public CombinedInvWrapper wrapper = new CombinedInvWrapper(input, output);
    private ItemStack oldOutput = output.getStackInSlot(0);

    public ContainerStonecutter(InventoryPlayer playerInventory, World worldIn, BlockPos posIn) {
        this.playerInv = playerInventory;
        this.world = worldIn;
        this.pos = posIn;

        addOwnSlots();
        addPlayerSlots();
    }

    private void addOwnSlots() {
        addSlotToContainer(new SlotItemHandler(input, 0, 20, 33));
        addSlotToContainer(new SlotItemHandler(output, 0, 143, 33));
    }

    private void addPlayerSlots() {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 9 + col * 18 - 1;
                int y = row * 18 + 70 + 14;
                this.addSlotToContainer(new Slot(this.playerInv, col + row * 9 + 9, x, y));
            }
        }

        // Slots for the hotBar
        for (int row = 0; row < 9; ++row) {
            int x = 9 + row * 18 - 1;
            int y = 58 + 70 + 14;
            this.addSlotToContainer(new Slot(this.playerInv, row, x, y));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        if (this.world.getBlockState(this.pos).getBlock() != Init.STONECUTTER) {
            return false;
        }
        else {
            return playerIn.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    public void setSelectedID(int selectedID) {
        this.selectedID = selectedID;
        onSelectedIDChanged(selectedID);
    }

    public void onSelectedIDChanged(int selectedID) {
        if(selectedID > 0) {
            output.setStackInSlot(0, RecipeResults.getStackResult(input.getStackInSlot(0), selectedID - 1));
        }
    }

    public void handleOutput() {
        ItemStack newStack = output.getStackInSlot(0);

        if(newStack.isEmpty()) {
            input.getStackInSlot(0).shrink(1);
        }

        if(oldOutput != newStack) { // Finishes method
            oldOutput = newStack;
        }
    }

    public void handleMaxCrafting() {
    }

    public InventoryPlayer getPlayerInv() {
        return playerInv;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        if(index == 1) {
            handleMaxCrafting();
        }

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemstack = itemStack1.copy();

            if (index < 2) {
                if (!this.mergeItemStack(itemStack1, 2, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemStack1, 0, 2, false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        if(!playerInv.getItemStack().isEmpty()) {
            playerIn.entityDropItem(playerInv.getItemStack(), 0.5F);
        }
        if(!world.isRemote) { // Mostly copied from Container#clearContainer
            if(!playerIn.isEntityAlive() || playerIn instanceof EntityPlayerMP && ((EntityPlayerMP)playerIn).hasDisconnected()) {
                for(int i = 0; i < input.getSlots(); i++) {
                    ItemStack stack = input.getStackInSlot(i);
                    if(!stack.isEmpty()) {
                        playerIn.entityDropItem(stack, 0.5F);
                    }
                }
            }
            else {
                for(int i = 0; i < input.getSlots(); i++) {
                    if(!input.getStackInSlot(i).isEmpty()) {
                        playerInv.placeItemBackInInventory(world, input.getStackInSlot(i));
                    }
                }
            }
        }
    }
}