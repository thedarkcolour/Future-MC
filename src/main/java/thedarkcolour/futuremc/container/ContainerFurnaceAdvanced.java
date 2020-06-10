package thedarkcolour.futuremc.container;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thedarkcolour.core.gui.FContainer;
import thedarkcolour.futuremc.block.BlockFurnaceAdvanced;
import thedarkcolour.futuremc.client.gui.GuiFurnaceAdvanced;
import thedarkcolour.futuremc.registry.FBlocks;
import thedarkcolour.futuremc.tile.TileFurnaceAdvanced;

public class ContainerFurnaceAdvanced extends FContainer {
    public final TileFurnaceAdvanced te;
    protected int fuelLeft, progress, currentItemBurnTime;

    public ContainerFurnaceAdvanced(InventoryPlayer playerInv, TileEntity te) {
        super(playerInv);
        this.te = (TileFurnaceAdvanced) te;

        addOwnSlots();
        addPlayerSlots(playerInv);
    }

    private void addOwnSlots() {
        addSlotToContainer(new Slot(te, 0, 56, 17));
        addSlotToContainer(new SlotFurnaceFuel(te, 1, 56, 53));
        addSlotToContainer(new SlotFurnaceOutput(getPlayerInv().player, te, 2, 116, 35));
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener listener : listeners) {
            listener.sendWindowProperty(this, 0, te.getField(0));
            listener.sendWindowProperty(this, 1, te.getField(1));
            listener.sendWindowProperty(this, 2, te.getField(2));
        }

        fuelLeft = te.getField(0);
        currentItemBurnTime = te.getField(1);
        progress = te.getField(2);
    }

    @Override
    public void updateProgressBar(int id, int data) {
        te.setField(id, data);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) { // Re-uses the transferStackInSlot from ContainerFurnace
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemstack = itemStack1.copy();

            if (index == 2) {
                if (!this.mergeItemStack(itemStack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                try {
                    slot.onSlotChange(itemStack1, itemstack);
                } catch (ClassCastException e) {
                    // silences the special case error for mr. BLaKe 🙂 🙂 🙂 🙂 🙂
                }
            } else if (index != 1 && index != 0) {
                if (!FurnaceRecipes.instance().getSmeltingResult(itemStack1).isEmpty()) {
                    if (!this.mergeItemStack(itemStack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (TileEntityFurnace.isItemFuel(itemStack1)) {
                    if (!this.mergeItemStack(itemStack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 30) {
                    if (!this.mergeItemStack(itemStack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 39 && !this.mergeItemStack(itemStack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemStack1, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemStack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemStack1);
        }
        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return isTileInRange(te, playerIn) && (
                isBlockInRange(FBlocks.SMOKER, te.getWorld(), te.getPos(), playerIn) ||
                isBlockInRange(FBlocks.BLAST_FURNACE, te.getWorld(), te.getPos(), playerIn)
        );
    }

    @SideOnly(Side.CLIENT)
    public GuiContainer getGuiContainer() {
        return te.getType() == BlockFurnaceAdvanced.FurnaceType.BLAST_FURNACE ?
                new GuiFurnaceAdvanced.BlastFurnace(new ContainerFurnaceAdvanced(getPlayerInv(), te)) :
                new GuiFurnaceAdvanced.Smoker(new ContainerFurnaceAdvanced(getPlayerInv(), te));
    }
}