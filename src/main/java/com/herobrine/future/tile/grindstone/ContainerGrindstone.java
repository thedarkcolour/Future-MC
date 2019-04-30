package com.herobrine.future.tile.grindstone;

import com.herobrine.future.init.Init;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityXPOrb;
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

import java.util.Map;

public class ContainerGrindstone extends Container {
    protected InventoryPlayer playerInv;
    protected World world;
    protected BlockPos pos;
    protected boolean isRecipeInvalid;

    public ItemStackHandler input = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            handleCrafting();
        }
    };
    public ItemStackHandler output = new ItemStackHandler(1) {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return false;
        }

        @Override
        protected void onContentsChanged(int slot) {
            handleOutput();
        }
    };

    private ItemStack oldStack = output.getStackInSlot(0);

    public ContainerGrindstone(InventoryPlayer playerInventory, World worldIn, BlockPos posIn) {
        this.playerInv = playerInventory;
        this.world = worldIn;
        this.pos = posIn;

        addOwnSlots();
        addPlayerSlots();
    }

    private void addOwnSlots() {
        addSlotToContainer(new SlotItemHandler(input, 0, 49, 19));
        addSlotToContainer(new SlotItemHandler(input, 1, 49, 40));
        addSlotToContainer(new SlotItemHandler(output, 0, 129, 34));
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
        if (this.world.getBlockState(this.pos).getBlock() != Init.GRINDSTONE) {
            return false;
        }
        else {
            return playerIn.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    public void clearInput() {
        if(!input.getStackInSlot(0).isEmpty() && !input.getStackInSlot(1).isEmpty())
        for(int i = 0; i < input.getSlots(); i++) {
            input.setStackInSlot(i, ItemStack.EMPTY);
        }
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

    public void handleCrafting() {
        // Handles two incompatible items
        if(input.getStackInSlot(0).getItem() != input.getStackInSlot(1).getItem() && !input.getStackInSlot(0).isEmpty() && !input.getStackInSlot(1).isEmpty()) {
            isRecipeInvalid = true;
        }
        // Handles two compatible items
        else if(input.getStackInSlot(0).getMaxDamage() > 1 && input.getStackInSlot(0).getMaxStackSize() == 1 && input.getStackInSlot(0).getItem() == input.getStackInSlot(1).getItem()) {
            isRecipeInvalid = false;
            ItemStack stack = input.getStackInSlot(0);
            int sum = (stack.getMaxDamage() - stack.getItemDamage()) + (stack.getMaxDamage() - input.getStackInSlot(1).getItemDamage());

            sum = sum + (int)(sum * 0.2);
            if(sum > stack.getMaxDamage()) {
                sum = stack.getMaxDamage();
            }

            output.setStackInSlot(0, new ItemStack(stack.getItem(), 1, stack.getMaxDamage() - sum));
        }
        // Resets the grid when the two items are incompatible
        if(input.getStackInSlot(0).getItem() != input.getStackInSlot(1).getItem() && !output.getStackInSlot(0).isEmpty()) {
            output.setStackInSlot(0, ItemStack.EMPTY);
        }
    }

    public void handleOutput() {
        ItemStack newStack = output.getStackInSlot(0);
        if(newStack.isEmpty() && newStack != oldStack) {
            awardEXP(newStack, input.getStackInSlot(1));
            clearInput();
        }
        oldStack = output.getStackInSlot(0);
    }

    public void awardEXP(ItemStack... input) {
        int exp = 0;
        for(ItemStack stack : input) {
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
            for(Enchantment enchantment : map.keySet()) {
                exp += getEnchantmentEXP(enchantment, map.get(enchantment));
            }
        }
        if(exp > 0) {
            world.spawnEntity(new EntityXPOrb(world, pos.getX(), pos.getY(), pos.getZ(), exp));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemstack = itemStack1.copy();

            if (index < 3) {
                if (!this.mergeItemStack(itemStack1, 3, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemStack1, 0, 3, false)) {
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

    public static int getEnchantmentEXP(Enchantment enchantment, int enchantLevel) {
        float exp;
        switch (enchantment.getRarity()) {
            default: exp =+ 2F; break;
            case UNCOMMON: exp =+ 4F; break;
            case RARE: exp =+ 8F; break;
            case VERY_RARE: exp =+ 16F;
        }
        switch (enchantLevel) {
            case 2: exp *= 1.25F;
            case 3: exp *= 1.5F;
            case 4: exp *= 1.75F;
            case 5: exp *= 2F;
        }
        return (int) exp;
    }
}