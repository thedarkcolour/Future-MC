package com.herobrine.future.tile.stonecutter;

import com.herobrine.future.recipe.stonecutter.RecipeResults;
import com.herobrine.future.recipe.stonecutter.StonecutterRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nullable;

public class TileEntityStonecutter extends TileEntity implements ITickable {
    private static final int IN = 1;
    private static final int OUT = 1;
    private static int selectedID = 0;

    ItemStackHandler inputHandler = new ItemStackHandler(IN) {
        @Override
        protected void onContentsChanged(int slot) {
            if (getStackInSlot(0).isEmpty()) {
                setSelectedID(0);
            }
            TileEntityStonecutter.this.markDirty();
        }
    };

    public ItemStackHandler outputHandler = new ItemStackHandler(OUT) {
        @Override
        protected void onContentsChanged(int slot) {
            if (getStackInSlot(0).isEmpty()) {
                inputHandler.getStackInSlot(0).shrink(1);
            }
            TileEntityStonecutter.this.markDirty();
        }
    };

    private CombinedInvWrapper combinedHandler = new CombinedInvWrapper(inputHandler, outputHandler);

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("itemIn")) {
            inputHandler.deserializeNBT((NBTTagCompound) compound.getTag("itemIn"));
        }
        if (compound.hasKey("itemOut")) {
            outputHandler.deserializeNBT((NBTTagCompound) compound.getTag("itemOut"));
        }
        if (compound.hasKey("selected")) {
            compound.getInteger("selected");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("itemIn", inputHandler.serializeNBT());
        compound.setTag("itemOut", outputHandler.serializeNBT());
        compound.setInteger("selected", selectedID);
        return compound;
    }


    boolean canInteractWith(EntityPlayer playerIn) {
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public boolean hasCapability(@Nullable Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(@Nullable Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY /*&& FutureConfig.c.stonecutterFunction*/) {
            if (facing == null) {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(combinedHandler);
            } else if (facing == EnumFacing.UP) {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inputHandler);
            } else {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(outputHandler);
            }
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void update() {
        tryStonecut();
    }

    private void tryStonecut() {
        if(StonecutterRecipes.instance().isStackValid(inputHandler.getStackInSlot(0)) && selectedID != 0) {
            craftItem();
        }
        else if (!outputHandler.getStackInSlot(0).isEmpty()){
            insertOutput(ItemStack.EMPTY);
        }
    }

    void craftItem() {
        ItemStack stack = RecipeResults.instance().getStackResult(inputHandler.getStackInSlot(0), (selectedID - 1));
        insertOutput(stack);
    }

    public static void setSelectedID(int selectedID) {
        TileEntityStonecutter.selectedID = selectedID;
    }

    private void insertOutput(ItemStack output) {
        outputHandler.setStackInSlot(0, output);
    }

    public static int getSelectedID() {
        return selectedID;
    }
}
