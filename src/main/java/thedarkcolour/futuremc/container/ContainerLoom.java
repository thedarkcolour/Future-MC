package thedarkcolour.futuremc.container;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import thedarkcolour.core.gui.Container;
import thedarkcolour.futuremc.client.gui.GuiLoom;
import thedarkcolour.futuremc.compat.oredict.OreDict;
import thedarkcolour.futuremc.init.Init;
import thedarkcolour.futuremc.item.ItemBannerPattern;

import javax.annotation.Nonnull;

public class ContainerLoom extends Container {
    private Slot[] slots = new SlotItemHandler[4];
    private InventoryPlayer playerInv;
    private World world;
    private BlockPos pos;
    private int i;
    private Runnable runnable = () -> {};

    private final ItemStackHandler handler = new ItemStackHandler(4);

    public ContainerLoom(InventoryPlayer playerInv, World worldIn, BlockPos pos) {
        this.playerInv = playerInv;
        this.world = worldIn;
        this.pos = pos;

        addOwnSlots();
        addPlayerSlots();
    }

    private void addOwnSlots() {
        slots[0] = addSlotToContainer(new SlotItemHandler(handler, 0, 13, 26) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return stack.getItem() instanceof ItemBanner;
            }
        });
        slots[1] = addSlotToContainer(new SlotItemHandler(handler, 1, 33, 26) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return OreDict.getOreName(stack).startsWith("dye");
            }
        });
        slots[2] = addSlotToContainer(new SlotItemHandler(handler, 2, 23, 45) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return stack.getItem() instanceof ItemBannerPattern;
            }
        });
        slots[3] = addSlotToContainer(new SlotItemHandler(handler, 3, 143, 57) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return false;
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

    public void setBannerSlot(ItemStack stack) {
        handler.setStackInSlot(0, stack);
    }

    public void setColorSlot(ItemStack stack) {
        handler.setStackInSlot(1, stack);
    }

    public void setPatternSlot(ItemStack stack) {
        handler.setStackInSlot(2, stack);
    }

    public void setOutput(ItemStack stack) {
        handler.setStackInSlot(3, stack);
    }

    public ItemStack getBanner() {
        return handler.getStackInSlot(0);
    }

    public ItemStack getColor() {
        return handler.getStackInSlot(1);
    }

    public ItemStack getPattern() {
        return handler.getStackInSlot(2);
    }

    public ItemStack getOutput() {
        return handler.getStackInSlot(3);
    }

    @SideOnly(Side.CLIENT)
    public Slot getLoomSlot(int index) {
        return slots[index];
    }

    @SideOnly(Side.CLIENT)
    public int func_217023_e() {
        return i;
    }

    @SideOnly(Side.CLIENT)
    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        if (this.world.getBlockState(this.pos).getBlock() != Init.LOOM) {
            return false;
        } else {
            return playerIn.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        if (!playerIn.isEntityAlive() || playerIn instanceof EntityPlayerMP && ((EntityPlayerMP) playerIn).hasDisconnected()) {
            for (int i = 0; i < 3; i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    playerIn.entityDropItem(stack, 0.5F);
                }
            }
        } else {
            for (int i = 0; i < 3; i++) {
                if (!handler.getStackInSlot(i).isEmpty()) {
                    playerInv.placeItemBackInInventory(world, handler.getStackInSlot(i));
                }
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 3) {
                if (!this.mergeItemStack(itemstack1, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != 1 && index != 0 && index != 2) {
                if (itemstack1.getItem() instanceof ItemBanner) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (OreDict.getOreName(itemstack1).startsWith("dye")) {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemstack1.getItem() instanceof ItemBannerPattern) {
                    if (!this.mergeItemStack(itemstack1, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 4 && index < 31) {
                    if (!this.mergeItemStack(itemstack1, 31, 40, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 31 && index < 40 && !this.mergeItemStack(itemstack1, 4, 31, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 4, 40, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    static EnumDyeColor getColorFromOreDict(ItemStack stack) {
        String s = OreDict.getOreName(stack);

        if(s.startsWith("dye")) {
            s = s.replaceFirst("dye", "");
            switch (s) {
                case "Black": return EnumDyeColor.BLACK;
                case "Red": return EnumDyeColor.RED;
                case "Green": return EnumDyeColor.GREEN;
                case "Purple": return EnumDyeColor.PURPLE;
                case "Cyan": return EnumDyeColor.CYAN;
                case "LightGray": return EnumDyeColor.SILVER;
                case "Gray": return EnumDyeColor.GRAY;
                case "Pink": return EnumDyeColor.PINK;
                case "Lime": return EnumDyeColor.LIME;
                case "Yellow": return EnumDyeColor.YELLOW;
                case "LightBlue": return EnumDyeColor.LIGHT_BLUE;
                case "Magenta": return EnumDyeColor.MAGENTA;
                case "Orange": return EnumDyeColor.ORANGE;
                default: return EnumDyeColor.WHITE;
            }
        } else {
            return EnumDyeColor.WHITE;
        }
    }

    public InventoryPlayer getPlayerInv() {
        return playerInv;
    }

    @SideOnly(Side.CLIENT)
    public GuiContainer getGuiContainer() {
        return new GuiLoom(new ContainerLoom(playerInv, world, pos));
    }
}