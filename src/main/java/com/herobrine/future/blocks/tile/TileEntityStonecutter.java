package com.herobrine.future.blocks.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityStonecutter extends TileEntity implements IInteractionObject {
    private String customName;
    private int SIZE = 2;
    private NonNullList<ItemStack> stacks = NonNullList.func_191197_a(SIZE, ItemStack.field_190927_a);
    private ItemStackHandler handler = new ItemStackHandler(SIZE);

    @Override
    public void func_145839_a(NBTTagCompound compound) {
        super.func_145839_a(compound);
        this.stacks = NonNullList.func_191197_a(SIZE, ItemStack.field_190927_a);
        if (compound.func_74764_b("inventory")) {
            handler.deserializeNBT((NBTTagCompound) compound.func_74781_a("inventory"));
        }
        if (compound.func_150297_b("CustomName", 8)) {
            this.customName = compound.func_74779_i("CustomName");
        }
    }

    @Override
    public NBTTagCompound func_189515_b(NBTTagCompound compound) {
        super.func_189515_b(compound);
        compound.func_74782_a("inventory", this.getInventory().serializeNBT());
        if (this.func_145818_k_()) {
            compound.func_74778_a("CustomName", this.customName);
        }
        return compound;
    }

    boolean canInteractWith(EntityPlayer playerIn) {
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
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handler);
        }
        return super.getCapability(capability, facing);
    }

    public ItemStackHandler getInventory() {
        return this.handler;
    }

    @Override
    public Container func_174876_a(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerStonecutter(playerInventory,this.field_145850_b, this.field_174879_c);
    }

    @Override
    public String func_174875_k() {
        return "minecraftfuture:stonecutter";
    }

    @Override
    public String func_70005_c_() {
        return this.func_145818_k_() ? this.customName : "container.stonecutter";
    }

    @Override
    public boolean func_145818_k_() {
        return false;
    }
}
