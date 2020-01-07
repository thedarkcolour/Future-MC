package thedarkcolour.futuremc.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import thedarkcolour.core.inventory.DarkInventory;

public class TileLectern extends TileEntity {
    public DarkInventory inventory = new DarkInventory(1);

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("heldBook")) {
            inventory.deserializeNBT((NBTTagCompound) compound.getTag("heldBook"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("heldBook", inventory.serializeNBT());
        return compound;
    }

    public ItemStack getBook() {
        return inventory.getStackInSlot(0);
    }
}