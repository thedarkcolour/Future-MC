package thedarkcolour.futuremc.container;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.SlotItemHandler;
import thedarkcolour.core.gui.ContainerBase;
import thedarkcolour.core.inventory.DarkInventory;
import thedarkcolour.futuremc.client.gui.GuiGrindstone;
import thedarkcolour.futuremc.enchantment.EnchantHelper;
import thedarkcolour.futuremc.registry.FBlocks;
import thedarkcolour.futuremc.registry.FSounds;

import java.util.Map;

public class ContainerGrindstone extends ContainerBase {
    private InventoryPlayer playerInv;
    private World world;
    private BlockPos pos;

    public DarkInventory input = new DarkInventory(2) {
        @Override
        public void onContentsChanged(int slot) {
            handleCrafting();
            detectAndSendChanges();
        }
    };
    public DarkInventory output = new DarkInventory(1) {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return false;
        }
    };

    public ContainerGrindstone(InventoryPlayer playerInv, World worldIn, BlockPos posIn) {
        this.playerInv = playerInv;
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
                int x = col * 18 + 8;
                int y = row * 18 + 84;
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

    private void clearInput() {
        for (int i = 0; i < input.getSlots(); i++) {
            input.getStackInSlot(i).shrink(1);
        }
    }

    private void handleCrafting() {
        // Handles two incompatible items
        if (!input.getStackInSlot(0).isItemEqualIgnoreDurability(input.getStackInSlot(1)) && !(input.anyMatch(ItemStack::isEmpty))) {
            output.setStackInSlot(0, ItemStack.EMPTY);
        }

        // Handles two compatible items
        else if (input.getStackInSlot(0).isItemEqualIgnoreDurability(input.getStackInSlot(1)) && input.getStackInSlot(0).isItemEnchantable()) {
            ItemStack stack = input.getStackInSlot(0);
            int sum = (stack.getMaxDamage() - stack.getItemDamage()) + (stack.getMaxDamage() - input.getStackInSlot(1).getItemDamage());

            sum += Math.floor(sum * 0.2);
            if (sum > stack.getMaxDamage()) {
                sum = stack.getMaxDamage();
            }

            ItemStack outItem = stack.copy();
            outItem.setItemDamage(stack.getMaxDamage() - sum);
            outItem.setTagInfo("Enchantments", new NBTTagList());

            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
            for (Enchantment e : map.keySet()) {
                if (e.isCurse()) {
                    outItem.addEnchantment(e, 1);
                }
            }
            map = EnchantmentHelper.getEnchantments(input.getStackInSlot(1));
            for (Enchantment e : map.keySet()) {
                try {
                    if (e.isCurse()) {
                        outItem.addEnchantment(e, 1);
                    }
                } catch (NullPointerException ignored) {
                    //  😈 😈 😈
                }
            }

            output.setStackInSlot(0, outItem);
        }

        // Disenchants an item
        else if (input.anyMatch(ItemStack::isItemEnchanted)) {
            int slot = input.getStackInSlot(0).isEmpty() ? 1 : 0;
            ItemStack stack = input.getStackInSlot(slot);

            ItemStack outItem = stack.copy();
            outItem.setTagInfo("ench", new NBTTagList());
            if (stack.isItemEqual(input.getStackInSlot(input.getStackInSlot(0).isEmpty() ? 0 : 1))) {
                int sum = (stack.getMaxDamage() - stack.getItemDamage()) + (stack.getMaxDamage() - input.getStackInSlot(1).getItemDamage());

                sum += Math.floor(sum * 0.2);
                if (sum > stack.getMaxDamage()) {
                    sum = stack.getMaxDamage();
                }
                outItem.setItemDamage(stack.getMaxDamage() - sum);
            }

            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
            for (Enchantment e : map.keySet()) {
                if (e.isCurse()) {
                    outItem.addEnchantment(e, 1);
                }
            }

            output.setStackInSlot(0, outItem);
        }

        // Converts enchanted books to EXP
        else if (((input.anyMatch(stack -> stack.getItem() == Items.ENCHANTED_BOOK))) && !input.getStackInSlot(0).isItemEqual(input.getStackInSlot(1))) {
            int slot = input.getStackInSlot(0).isEmpty() ? 1 : 0;
            ItemStack book = input.getStackInSlot(slot);
            boolean isCursed = input.anyMatch(EnchantHelper.INSTANCE::isCursed);

            ItemStack outBook;
            if (isCursed) {
                outBook = book.copy();
                outBook.setTagInfo("StoredEnchantments", new NBTTagList());
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(book);

                for (Enchantment e : enchantments.keySet()) {
                    if (e.isCurse()) {
                        ItemEnchantedBook.addEnchantment(outBook, new EnchantmentData(e, 1));
                    }
                }
                enchantments = EnchantmentHelper.getEnchantments(input.getStackInSlot(slot == 1 ? 0 : 1));
                for (Enchantment e : enchantments.keySet()) {
                    if (e.isCurse()) {
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
            output.setStackInSlot(0, ItemStack.EMPTY);
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        if (!world.isRemote) { // Mostly copied from ContainerBase#clearContainer
            if (!playerIn.isEntityAlive() || playerIn instanceof EntityPlayerMP && ((EntityPlayerMP) playerIn).hasDisconnected()) {
                for (int i = 0; i < input.getSlots(); i++) {
                    ItemStack stack = input.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        playerIn.entityDropItem(stack, 0.5F);
                    }
                }
            } else {
                for (int i = 0; i < input.getSlots(); i++) {
                    if (!input.getStackInSlot(i).isEmpty()) {
                        playerInv.placeItemBackInInventory(world, input.getStackInSlot(i));
                    }
                }
            }
        }
    }

    private void handleOutput() {
        awardEXP(input);
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), FSounds.INSTANCE.getGRINDSTONE_USE(), SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        clearInput(); // Clear it last, otherwise XP doesn't work
    }

    private void awardEXP(DarkInventory inventory) {
        int exp = 0;
        for (ItemStack stack : inventory) {
            if (stack.isEmpty()) continue;

            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);

            for (Enchantment enchantment : enchantments.keySet()) {
                if (!enchantment.isCurse()) {
                    exp += getEnchantmentEXP(enchantment, enchantments.get(enchantment));
                }
            }
        }
        if (exp > 0) {
            EntityXPOrb orb = new EntityXPOrb(world, pos.getX(), pos.getY(), pos.getZ(), 0);
            orb.delayBeforeCanPickup = 5;
            orb.xpValue = exp;
            if (!world.isRemote) {
                world.spawnEntity(orb);
            }
        }
    }

    private int getEnchantmentEXP(Enchantment enchantment, int enchantLevel) {
        return enchantment.getMinEnchantability(enchantLevel);
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

    public InventoryPlayer getPlayerInv() {
        return playerInv;
    }

    public boolean isRecipeInvalid() {
        return !(input.getStackInSlot(0).isEmpty() && input.getStackInSlot(1).isEmpty()) && output.getStackInSlot(0).isEmpty();
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return isBlockInRange(FBlocks.INSTANCE.getGRINDSTONE(), world, pos, playerIn);
    }

    @SideOnly(Side.CLIENT)
    public GuiContainer getGuiContainer() {
        return new GuiGrindstone(new ContainerGrindstone(playerInv, world, pos));
    }
}