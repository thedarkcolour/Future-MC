package com.herobrine.future.tile.advancedfurnace;

import com.herobrine.future.blocks.FurnaceAdvanced;
import com.herobrine.future.blocks.FurnaceAdvanced.FurnaceType;
import com.herobrine.future.utils.proxy.Init;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nullable;

public abstract class TileEntityAdvancedFurnace extends TileEntity implements ITickable {
    protected ItemStackHandler inputCraft = new ItemStackHandler(), outputCraft = new ItemStackHandler(), inputFuel = new ItemStackHandler();
    protected boolean isBurning;
    protected Block SMOKER = Init.advancedFurnace.get(0), BLAST_FURNACE = Init.advancedFurnace.get(1);

    public int fuelLeft = 0, progress = 0; // Time before current fuel burns out
     // The time this machine has been cooking

    private CombinedInvWrapper combinedInventoryHandler = new CombinedInvWrapper(inputCraft, inputFuel);
    protected FurnaceType type; // The type of block this is

    public TileEntityAdvancedFurnace(FurnaceType type) {
        this.type = type;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("inputCraft")) {
            inputCraft.deserializeNBT((NBTTagCompound) compound.getTag("inputCraft"));
        }
        if (compound.hasKey("inputFuel")) {
            inputFuel.deserializeNBT((NBTTagCompound) compound.getTag("inputFuel"));
        }
        if (compound.hasKey("outputCraft")) {
            outputCraft.deserializeNBT((NBTTagCompound) compound.getTag("outputCraft"));
        }
        if (compound.hasKey("fuelLeft")) {
            compound.getInteger("fuelLeft");
        }
        if (compound.hasKey("progress")) {
            compound.getInteger("progress");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("inputCraft", inputCraft.serializeNBT());
        compound.setTag("inputFuel", inputFuel.serializeNBT());
        compound.setTag("outputCraft", outputCraft.serializeNBT());
        compound.setInteger("fuelLeft", fuelLeft);
        compound.setInteger("progress", progress);
        return compound;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == null) {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(combinedInventoryHandler);
            }
            else if (facing == EnumFacing.UP) {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inputCraft);
            }
            else if (facing == EnumFacing.DOWN) {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(outputCraft);
            }
            else {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inputFuel);
            }
        }
        return super.getCapability(capability, facing);
    }

    boolean canInteractWith(EntityPlayer playerIn) {
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    public abstract FurnaceType getType();

    protected void fuelFunction(FurnaceType type) {
        boolean flag = false;
        if(!world.isRemote) {
            if(this.fuelLeft < 1) {
                int burnTime = TileEntityFurnace.getItemBurnTime(inputFuel.getStackInSlot(0));

                if(TileEntityFurnace.isItemFuel(inputFuel.getStackInSlot(0)) && type.canCraft(inputCraft.getStackInSlot(0))) {
                    inputFuel.getStackInSlot(0).shrink(1);
                    this.fuelLeft = burnTime;
                }
            }
            else {
                fuelLeft--;
            }

            if(this.fuelLeft != 0) {
                this.isBurning = true;
            }
            else {
                this.isBurning = false;
            }
        }
        //this.markDirty();
    }

    protected void updateState() {
        FurnaceAdvanced.setState(isBurning, world, pos);
    }
}