package thedarkcolour.futuremc.container;

import invtweaks.api.container.ChestContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import thedarkcolour.core.gui.FContainer;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.client.gui.GuiBarrel;
import thedarkcolour.futuremc.tile.TileBarrel;

@ChestContainer
public class ContainerBarrel extends FContainer {
    public final TileBarrel te;

    public ContainerBarrel(InventoryPlayer playerInv, TileEntity te) {
        super(playerInv);

        try {
            this.te = (TileBarrel) te;
        } catch (ClassCastException e) {
            FutureMC.LOGGER.fatal("Missing a tile entity for barrel block");
            throw e;
        }

        addOwnSlots();
        addPlayerSlots(playerInv);
    }

    protected void addOwnSlots() {
        IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 8 + col * 18;
                int y = 18 + row * 18;
                addSlotToContainer(new SlotItemHandler(itemHandler, col + row * 9, x, y));
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemstack = itemStack1.copy();

            if (index < 27) {
                if (!this.mergeItemStack(itemStack1, 27, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemStack1, 0, 27, false)) {
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
    public boolean canInteractWith(EntityPlayer playerIn) {
        return isTileInRange(te, playerIn);
    }

    @SideOnly(Side.CLIENT)
    public GuiContainer getGuiContainer() {
        return new GuiBarrel(new ContainerBarrel(getPlayerInv(), te));
    }
}