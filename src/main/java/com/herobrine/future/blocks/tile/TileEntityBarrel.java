package com.herobrine.future.blocks.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityBarrel extends TileEntity {
    public static final int SIZE = 27;

    public ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE);
    private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>func_191197_a(27, ItemStack.field_190927_a);

    public void func_145839_a(NBTTagCompound compound) {
        super.func_145839_a(compound);
        this.stacks = NonNullList.func_191197_a(SIZE, ItemStack.field_190927_a);
        if (compound.func_74764_b("items")) {
            itemStackHandler.deserializeNBT((NBTTagCompound) compound.func_74781_a("items"));
        }
    }

    public NBTTagCompound func_189515_b(NBTTagCompound compound) {
        super.func_189515_b(compound);
        compound.func_74782_a("items", this.getInventory().serializeNBT());
        return compound;
    }

    /*********************/
    public boolean canInteractWith(EntityPlayer playerIn) {
        return !func_145837_r() && playerIn.func_174818_b(field_174879_c.func_177963_a(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
        }
        return super.getCapability(capability, facing);
    }

    public ItemStackHandler getInventory() {
        return this.itemStackHandler;
    }
}
