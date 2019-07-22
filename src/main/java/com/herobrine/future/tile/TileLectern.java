package com.herobrine.future.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

public class TileLectern extends TileEntity {
    public ItemStackHandler heldBook = new ItemStackHandler();

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("heldBook")) {
            heldBook.deserializeNBT((NBTTagCompound) compound.getTag("heldBook"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("heldBook", heldBook.serializeNBT());
        return compound;
    }

    public ItemStack getBook() {
        return heldBook.getStackInSlot(0);
    }
}