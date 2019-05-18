package com.herobrine.future.tile.advancedfurnace;

import com.herobrine.future.blocks.BlockFurnaceAdvanced;
import com.herobrine.future.config.FutureConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public abstract class TileAdvancedFurnace extends TileEntity implements ITickable {
    protected ItemStackHandler inputCraft = new ItemStackHandler() {
        @Override
        protected void onContentsChanged(int slot) {
            TileAdvancedFurnace.this.markDirty();
        }
    };
    protected ItemStackHandler inputFuel = new ItemStackHandler() {
        @Override
        protected void onContentsChanged(int slot) {
            TileAdvancedFurnace.this.markDirty();
        }
    };
    protected ItemStackHandler outputCraft = new ItemStackHandler() {
        @Override
        protected void onContentsChanged(int slot) {
            TileAdvancedFurnace.this.markDirty();
        }
    };

    protected boolean isBurning;
    protected static final boolean doubleFuel = FutureConfig.general.furnaceDoubleFuel;

    public int fuelLeft, progress; // Time before current fuel burns out
    // The time this machine has been cooking

    protected CombinedInvWrapper combinedInventoryHandler = new CombinedInvWrapper(inputCraft, inputFuel, outputCraft);
    protected BlockFurnaceAdvanced.FurnaceType type; // The type of block this is

    public TileAdvancedFurnace(BlockFurnaceAdvanced.FurnaceType type) {
        this.type = type;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("inputCraft")) {
            inputCraft.deserializeNBT((NBTTagCompound) compound.getTag("inputCraft"));
        }
        if(compound.hasKey("inputFuel")) {
            inputFuel.deserializeNBT((NBTTagCompound) compound.getTag("inputFuel"));
        }
        if(compound.hasKey("outputCraft")) {
            outputCraft.deserializeNBT((NBTTagCompound) compound.getTag("outputCraft"));
        }
        progress = compound.getInteger("progress");
        fuelLeft = compound.getInteger("fuelLeft");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("inputCraft", inputCraft.serializeNBT());
        compound.setTag("inputFuel", inputFuel.serializeNBT());
        compound.setTag("outputCraft", outputCraft.serializeNBT());
        compound.setInteger("progress", progress);
        compound.setInteger("fuelLeft", fuelLeft);
        return compound;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return false;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
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

    public abstract BlockFurnaceAdvanced.FurnaceType getType();

    protected void function() {
        if(!world.isRemote) {
            if(fuelLeft == 0) { // Skips if smelting
                if(trySmelt()) { // Checks if the machine has a valid recipe
                    startSmelt(); // Consumes fuel
                }
            }
            if(fuelLeft != 0 && getType().canCraft(inputCraft.getStackInSlot(0))) { // Checks if the machine can operate
                doSmelt(); // Updates the progress, fuelTime, and isBurning
            }
            else {
                setIsBurning(false); // Moved to after smelt starts, otherwise it blinks when it runs out of fuel
            }

            decreaseFuel();

            if(progress == 100) {
                finishSmelt(); // Consumes the input stack, inserts the output stack, and resets the progress
            }
            if(!getType().canCraft(inputCraft.getStackInSlot(0)) || fuelLeft == 0) { // Cancels the craft cycle
                setIsBurning(false); // Fixes furnaces burning when not supposed to.
                progress = 0;
            }
        }
    }

    protected void setIsBurning(boolean isBurning) {
        if(this.isBurning != isBurning) {
            this.isBurning = isBurning;
            markDirty();
            BlockFurnaceAdvanced.setState(isBurning, world, pos);
            IBlockState state = world.getBlockState(pos);
            getWorld().notifyBlockUpdate(pos, state, state, 3);
        }
    }

    public boolean trySmelt() {
        if(TileEntityFurnace.isItemFuel(fuelStack()) && getType().canCraft(inputCraft.getStackInSlot(0))) {
            return putOutput(true);
        }
        return false;
    }

    public ItemStack fuelStack() {
        return inputFuel.getStackInSlot(0);
    }

    public void startSmelt() {
        setIsBurning(true);
        int burnTime = TileEntityFurnace.getItemBurnTime(fuelStack());
        fuelStack().shrink(1);
        fuelLeft = burnTime;

    }

    public void decreaseFuel() {
        if(fuelLeft != 0) {
            fuelLeft--;
            if(doubleFuel && fuelLeft != 0) {
                fuelLeft--;
            }
        }
    }

    public void doSmelt() {
        progress++;
    }

    private void finishSmelt() {
        progress = 0;
        if(putOutput(false)) {
            inputCraft.extractItem(0, 1, false);
        }
    }

    private boolean putOutput(boolean simulate) {
        ItemStack result = FurnaceRecipes.instance().getSmeltingResult(new ItemStack(inputCraft.getStackInSlot(0).getItem(), 1, inputCraft.getStackInSlot(0).getMetadata()));
        ItemStack remaining = outputCraft.insertItem(0, result.copy(), simulate);

        return remaining.isEmpty();
    }

    @Override
    public void update() {
        function();
    }
}