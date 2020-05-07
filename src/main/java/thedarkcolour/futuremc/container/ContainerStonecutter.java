package thedarkcolour.futuremc.container;

import com.google.common.base.Objects;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import thedarkcolour.core.gui.FContainer;
import thedarkcolour.futuremc.client.gui.GuiStonecutter;
import thedarkcolour.futuremc.recipe.stonecutter.StonecutterRecipe;
import thedarkcolour.futuremc.recipe.stonecutter.StonecutterRecipes;
import thedarkcolour.futuremc.registry.FBlocks;
import thedarkcolour.futuremc.registry.FSounds;

public class ContainerStonecutter extends FContainer {
    private final World world;
    private final BlockPos pos;
    private final InventoryPlayer playerInv;
    private StonecutterRecipe currentRecipe;
    private long lastOnTake = 0L;
    private int selectedIndex;
    private Runnable inventoryUpdateListener = () -> {
    };
    public ItemStackHandler handler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            if (slot == 0) {
                handleCrafting();
            }
            inventoryUpdateListener.run();
        }
    };

    public ContainerStonecutter(InventoryPlayer playerInv, World world, BlockPos posIn) {
        this.playerInv = playerInv;
        this.world = world;
        this.pos = posIn;

        addOwnSlots();
        addPlayerSlots();
    }

    private void addOwnSlots() {
        addSlotToContainer(new SlotItemHandler(handler, 0, 20, 33) {
            @Override
            public void onSlotChanged() {
                if (getStack().isEmpty()) {
                    handler.setStackInSlot(1, ItemStack.EMPTY);
                }
            }
        });
        addSlotToContainer(new SlotItemHandler(handler, 1, 143, 33) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }

            @Override
            public ItemStack onTake(EntityPlayer playerIn, ItemStack stack) {
                handler.getStackInSlot(0).shrink(1);
                if (!handler.getStackInSlot(0).isEmpty()) {
                    updateRecipeResultSlot();
                } else {
                    currentRecipe = null;
                    selectedIndex = -1;
                }

                stack.getItem().onCreated(stack, playerIn.world, playerIn);
                long l = world.getTotalWorldTime();
                if (lastOnTake != l) {
                    world.playSound(null, pos, FSounds.INSTANCE.getSTONECUTTER_CARVE(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                    lastOnTake = l;
                }
                return super.onTake(playerIn, stack);
            }
        });
    }

    private void addPlayerSlots() {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = col * 18 + 8;
                int y = row * 18 + 84;
                addSlotToContainer(new Slot(playerInv, col + row * 9 + 9, x, y));
            }
        }

        // Slots for the hotBar
        for (int row = 0; row < 9; ++row) {
            int x = 9 + row * 18 - 1;
            addSlotToContainer(new Slot(playerInv, row, x, 142));
        }
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public StonecutterRecipe getCurrentRecipe() {
        return currentRecipe;
    }

    public int getRecipeListSize() {
        return currentRecipe.getTotalOutputs();
    }

    public boolean hasRecipe() {
        return currentRecipe != null;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        if (world.getBlockState(pos).getBlock() != FBlocks.INSTANCE.getSTONECUTTER()) {
            return false;
        } else {
            return playerIn.getDistanceSq((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public boolean enchantItem(EntityPlayer playerIn, int id) {
        if (id >= 0 && id < currentRecipe.getTotalOutputs()) {
            selectedIndex = id;
            updateRecipeResultSlot();
            return true;
        }

        return false;
    }

    private void handleCrafting() {
        StonecutterRecipe recipe = StonecutterRecipes.INSTANCE.getRecipe(handler.getStackInSlot(0));
        if (recipe != null) {
            if (!Objects.equal(currentRecipe, recipe)) {
                currentRecipe = recipe;
                selectedIndex = -1;
            }
        } else {
            currentRecipe = null;
        }
    }

    private void updateRecipeResultSlot() {
        if (currentRecipe != null && selectedIndex > -1) {
            handler.setStackInSlot(1, currentRecipe.getOutput(selectedIndex));
        } else {
            handler.setStackInSlot(1, ItemStack.EMPTY);
        }

        detectAndSendChanges();
    }

    public void setInventoryUpdateListener(Runnable listenerIn) {
        inventoryUpdateListener = listenerIn;
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return false;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 1) {
                itemstack1.getItem().onCreated(itemstack1, playerIn.world, playerIn);
                if (!mergeItemStack(itemstack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index == 0) {
                if (!mergeItemStack(itemstack1, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (StonecutterRecipes.INSTANCE.getRecipe(itemstack1) != null) {
                if (!mergeItemStack(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 2 && index < 29) {
                if (!mergeItemStack(itemstack1, 29, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 29 && index < 38 && !mergeItemStack(itemstack1, 2, 29, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }

            slot.onSlotChanged();
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
            detectAndSendChanges();
        }

        return itemstack;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        ItemStack stack = handler.getStackInSlot(0);
        if (!playerIn.isEntityAlive() || playerIn instanceof EntityPlayerMP && ((EntityPlayerMP) playerIn).hasDisconnected()) {
            if (!stack.isEmpty()) {
                playerIn.entityDropItem(stack, 0.5F);
            }
        } else {
            if (!stack.isEmpty()) {
                playerInv.placeItemBackInInventory(world, stack);
            }
        }
    }

    public InventoryPlayer getPlayerInv() {
        return playerInv;
    }

    @SideOnly(Side.CLIENT)
    public GuiContainer getGuiContainer() {
        return new GuiStonecutter(new ContainerStonecutter(playerInv, world, pos));
    }

    public void setCurrentRecipe(StonecutterRecipe recipe) {
        currentRecipe = recipe;
    }
}