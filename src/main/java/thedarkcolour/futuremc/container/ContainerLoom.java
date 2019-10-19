package thedarkcolour.futuremc.container;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.BannerPattern;
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
    private InventoryPlayer playerInv;
    private World world;
    private BlockPos pos;
    private int selectedIndex;
    private Runnable inventoryUpdateListener = () -> {};
    private ItemStackHandler handler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            if (slot != 3) {
                handleCrafting();
            }
            inventoryUpdateListener.run();
        }
    };

    public ContainerLoom(InventoryPlayer playerInv, World worldIn, BlockPos pos) {
        this.playerInv = playerInv;
        this.world = worldIn;
        this.pos = pos;

        addOwnSlots();
        addPlayerSlots();
    }

    private void addOwnSlots() {
        addSlotToContainer(new SlotItemHandler(handler, 0, 13, 26) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return stack.getItem() instanceof ItemBanner;
            }
        });
        addSlotToContainer(new SlotItemHandler(handler, 1, 33, 26) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return OreDict.getOreName(stack).startsWith("dye");
            }
        });
        addSlotToContainer(new SlotItemHandler(handler, 2, 23, 45) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return stack.getItem() instanceof ItemBannerPattern;
            }
        });
        addSlotToContainer(new SlotItemHandler(handler, 3, 143, 57) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return false;
            }

            @Override
            public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
                getColor().shrink(1);
                getBanner().shrink(1);
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

        for (int row = 0; row < 9; ++row) {
            int x = 9 + row * 18 - 1;
            int y = 58 + 70 + 14;
            this.addSlotToContainer(new Slot(this.playerInv, row, x, y));
        }
    }

    public void setColor(ItemStack stack) {
        handler.setStackInSlot(1, stack);
    }

    public void setPattern(ItemStack stack) {
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

    public Slot getLoomSlot(int index) {
        return getSlot(index);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        if (world.getBlockState(pos).getBlock() != Init.LOOM) {
            return false;
        } else {
            return playerIn.getDistanceSq((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public boolean enchantItem(EntityPlayer playerIn, int id) {
        if (id > 0 && id <= BannerPattern.values().length) {
            selectedIndex = id;
            updateRecipeResultSlot();
            return true;
        }

        return false;
    }

    private void handleCrafting() {
        ItemStack banner = getBanner();
        ItemStack pattern = getPattern();
        if (getOutput().isEmpty() || !banner.isEmpty() && !getColor().isEmpty() && selectedIndex > 0 && (selectedIndex < BannerPattern.values().length - 5 || !pattern.isEmpty())) {
            if (!pattern.isEmpty() && pattern.getItem() instanceof ItemBannerPattern) {
                NBTTagCompound tag = banner.getOrCreateSubCompound("BlockEntityTag");
                boolean flag = tag.hasKey("Patterns", 9) && !banner.isEmpty() && tag.getTagList("Patterns", 10).tagCount() >= 6;
                if (flag) {
                    selectedIndex = 0;
                } else {
                    selectedIndex = ItemBannerPattern.getBannerPattern(pattern).ordinal();
                }
            }
        } else {
            setOutput(ItemStack.EMPTY);
            selectedIndex = 0;
        }

        updateRecipeResultSlot();
        detectAndSendChanges();
    }

    private void updateRecipeResultSlot() {
        if (selectedIndex > 0) {
            ItemStack banner = getBanner();
            ItemStack color = getColor();
            ItemStack stack = ItemStack.EMPTY;
            if (!banner.isEmpty() && !color.isEmpty()) {
                stack = banner.copy();
                stack.setCount(1);
                NBTTagCompound nbt = stack.getOrCreateSubCompound("BlockEntityTag");
                NBTTagList list;
                if (nbt.hasKey("Patterns", 9)) {
                    list = nbt.getTagList("Pattern", 10);
                } else {
                    list = new NBTTagList();
                    nbt.setTag("Patterns", list);
                }
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("Pattern", BannerPattern.values()[selectedIndex].getHashname());
                tag.setInteger("Color", getColorForStack(color).getDyeDamage());
                list.appendTag(tag);
            }

            if (!ItemStack.areItemStacksEqual(stack, getOutput())) {
                setOutput(stack);
            }
        } else {
            setOutput(ItemStack.EMPTY);
        }
    }

    public void setInventoryUpdateListener(Runnable listenerIn) {
        inventoryUpdateListener = listenerIn;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 3) {
                if (!mergeItemStack(itemstack1, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != 1 && index != 0 && index != 2) {
                if (itemstack1.getItem() instanceof ItemBanner) {
                    if (!mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (OreDict.getOreName(itemstack1).startsWith("dye")) {
                    if (!mergeItemStack(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemstack1.getItem() instanceof ItemBannerPattern) {
                    if (!mergeItemStack(itemstack1, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 4 && index < 31) {
                    if (!mergeItemStack(itemstack1, 31, 40, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 31 && index < 40 && !mergeItemStack(itemstack1, 4, 31, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(itemstack1, 4, 40, false)) {
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

    public InventoryPlayer getPlayerInv() {
        return playerInv;
    }

    public static EnumDyeColor getColorForStack(ItemStack stack) {
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

    @SideOnly(Side.CLIENT)
    public GuiContainer getGuiContainer() {
        return new GuiLoom(new ContainerLoom(playerInv, world, pos));
    }
}