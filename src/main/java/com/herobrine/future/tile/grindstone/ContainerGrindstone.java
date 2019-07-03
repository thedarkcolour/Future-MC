package com.herobrine.future.tile.grindstone;

import com.herobrine.future.enchantment.EnchantHelper;
import com.herobrine.future.init.Init;
import com.herobrine.future.sound.Sounds;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

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
    };

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
        addSlotToContainer(new SlotItemHandler(output, 0, 129, 34) {
            @Override
            public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
                handleOutput();
                return stack;
            }
        });
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
        if(!input.getStackInSlot(0).isItemEqualIgnoreDurability(input.getStackInSlot(1)) && !(input.getStackInSlot(0).isEmpty() || input.getStackInSlot(1).isEmpty())) {
            isRecipeInvalid = true;
        }

        // Handles two compatible items
        else if(input.getStackInSlot(0).isItemEqualIgnoreDurability(input.getStackInSlot(1)) && input.getStackInSlot(0).isItemEnchantable()) {
            isRecipeInvalid = false;
            ItemStack stack = input.getStackInSlot(0);
            int sum = (stack.getMaxDamage() - stack.getItemDamage()) + (stack.getMaxDamage() - input.getStackInSlot(1).getItemDamage());

            sum += Math.floor(sum * 0.2);
            if(sum > stack.getMaxDamage()) {
                sum = stack.getMaxDamage();
            }

            ItemStack outItem = stack.copy();
            outItem.setItemDamage(stack.getMaxDamage() - sum);
            outItem.setTagInfo("Enchantments", new NBTTagList());

            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
            for(Enchantment e : map.keySet()) {
                if(e.isCurse()) {
                    outItem.addEnchantment(e, 1);
                }
            }
            map = EnchantmentHelper.getEnchantments(input.getStackInSlot(1));
            for(Enchantment e : map.keySet()) {
                if(e.isCurse()) {
                    outItem.addEnchantment(e, 1);
                }
            }

            output.setStackInSlot(0, outItem);
        }

        // Disenchants an item
        else if((input.getStackInSlot(0).isItemEnchanted()) || (input.getStackInSlot(1).isItemEnchanted())) {
            isRecipeInvalid = false;
            int slot = input.getStackInSlot(0).isEmpty() ? 1 : 0;
            ItemStack stack = input.getStackInSlot(slot);

            ItemStack outItem = stack.copy();
            outItem.setTagInfo("ench", new NBTTagList());

            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
            for(Enchantment e : map.keySet()) {
                if(e.isCurse()) {
                    outItem.addEnchantment(e, 1);
                }
            }

            output.setStackInSlot(0, outItem);
        }

        // Converts enchanted books to EXP
        else if((input.getStackInSlot(0).getItem() == Items.ENCHANTED_BOOK) || (input.getStackInSlot(1).getItem() == Items.ENCHANTED_BOOK)) {
            isRecipeInvalid = false;
            int slot = input.getStackInSlot(0).isEmpty() ? 1 : 0;
            ItemStack book = input.getStackInSlot(slot);
            boolean isCursed = EnchantHelper.isCursed(input.getStackInSlot(0)) || EnchantHelper.isCursed(input.getStackInSlot(1));

            ItemStack outBook;
            if(isCursed) {
                outBook = book.copy();
                outBook.setTagInfo("StoredEnchantments", new NBTTagList());
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(book);

                for(Enchantment e : enchantments.keySet()) {
                    if(e.isCurse()) {
                        ItemEnchantedBook.addEnchantment(outBook, new EnchantmentData(e, 1));
                    }
                }
                enchantments = EnchantmentHelper.getEnchantments(input.getStackInSlot(slot == 1 ? 0 : 1));
                for(Enchantment e : enchantments.keySet()) {
                    if(e.isCurse()) {
                        ItemEnchantedBook.addEnchantment(outBook, new EnchantmentData(e, 1));
                    }
                }
            } else {
                outBook = new ItemStack(Items.BOOK);
            }
            output.setStackInSlot(0, outBook);
        }

        // Resets grid
        else {
            isRecipeInvalid = false;
            output.setStackInSlot(0, ItemStack.EMPTY);
        }
    }

    public void handleOutput() {
        awardEXP(input.getStackInSlot(0), input.getStackInSlot(1));
        world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), Sounds.GRINDSTONE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        clearInput(); // Clear it last, otherwise XP doesn't work
    }

    public void awardEXP(ItemStack... input) {
        int exp = 0;
        for(ItemStack stack : input) {
            if(stack.isEmpty()) continue;

            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);

            for(Enchantment enchantment : enchantments.keySet()) {
                if(!enchantment.isCurse()) {
                    exp += getEnchantmentEXP(enchantment, enchantments.get(enchantment));
                }
            }
        }
        if(exp > 0) {
            EntityXPOrb orb = new EntityXPOrb(world, pos.getX(), pos.getY(), pos.getZ(), 0);
            orb.delayBeforeCanPickup = 5;
            orb.xpValue = exp;
            if(!world.isRemote) world.spawnEntity(orb);
        }
    }

    public static int getEnchantmentEXP(Enchantment enchantment, int enchantLevel) {
        float exp;
        switch (enchantment.getRarity()) {
            default: exp =+ 2F; break;
            case UNCOMMON: exp =+ 12F; break;
            case RARE: exp =+ 18F; break;
            case VERY_RARE: exp =+ 36F;
        }
        switch (enchantLevel) {
            case 2: exp *= 1.4F;
            case 3: exp *= 1.7F;
            case 4: exp *= 1.9F;
            case 5: exp *= 2.2F;
        }
        return (int) exp * 3;
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
                slot.onSlotChange(itemStack1, itemstack);
            } else if (!this.mergeItemStack(itemStack1, 0, 3, false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            slot.onTake(playerIn, itemStack1);
        }
        return itemstack;
    }
}