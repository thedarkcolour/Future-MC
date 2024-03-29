package thedarkcolour.futuremc.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.block.villagepillage.BlockFurnaceAdvanced;
import thedarkcolour.futuremc.container.ContainerFurnaceAdvanced;

public class TileFurnaceAdvanced extends TileEntityLockable implements ITickable, ISidedInventory {
    private static final int MAX_COOK_TIME = 100;
    private static final int[] SLOTS_TOP = new int[]{0};
    private static final int[] SLOTS_BOTTOM = new int[]{2, 1};
    private static final int[] SLOTS_SIDES = new int[]{1};
    private final BlockFurnaceAdvanced.FurnaceType type;
    private NonNullList<ItemStack> furnaceItemStacks = NonNullList.withSize(3, ItemStack.EMPTY);
    private int furnaceBurnTime;
    private int currentItemBurnTime;
    private int cookTime;

    public TileFurnaceAdvanced(BlockFurnaceAdvanced.FurnaceType type) {
        this.type = type;
    }

    public static void setState(boolean active, World world, BlockPos pos) {
        if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileFurnaceAdvanced) {
            world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockFurnaceAdvanced.Companion.getLIT(), active));
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        furnaceItemStacks = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, furnaceItemStacks);
        furnaceBurnTime = compound.getInteger("BurnTime");
        cookTime = compound.getInteger("CookTime");
        currentItemBurnTime = compound.getInteger("CurrentItemBurnTime");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", furnaceBurnTime);
        compound.setInteger("CookTime", cookTime);
        compound.setInteger("CurrentItemBurnTime", currentItemBurnTime);
        ItemStackHelper.saveAllItems(compound, furnaceItemStacks);

        return compound;
    }

    @Override
    public void update() {
        boolean wasBurning = isBurning();
        boolean isDirty = false;

        if (isBurning()) {
            furnaceBurnTime -= 2;
            if (furnaceBurnTime < 0) furnaceBurnTime = 0;
        }

        if (!world.isRemote) {
            ItemStack fuel = getFuel();

            if (isBurning() || !fuel.isEmpty() && !getInput().isEmpty()) {
                if (!isBurning() && canSmelt()) {
                    this.furnaceBurnTime = TileEntityFurnace.getItemBurnTime(fuel);
                    this.currentItemBurnTime = furnaceBurnTime;

                    if (isBurning()) {
                        isDirty = true;

                        if (!fuel.isEmpty()) {
                            Item item = fuel.getItem();
                            fuel.shrink(1);

                            if (fuel.isEmpty()) {
                                ItemStack container = item.getContainerItem(fuel);
                                furnaceItemStacks.set(1, container);
                            }
                        }
                    }
                }

                if (isBurning() && canSmelt()) {
                    ++cookTime;

                    if (cookTime == MAX_COOK_TIME) {
                        cookTime = 0;
                        smeltItem();
                        isDirty = true;
                    }
                } else {
                    cookTime = 0;
                }
            } else if (!isBurning() && cookTime > 0) {
                cookTime = MathHelper.clamp(cookTime - 2, 0, MAX_COOK_TIME);
            }

            if (wasBurning != isBurning()) {
                isDirty = true;
                setState(isBurning(), world, pos);
            }
        }

        if (isDirty) {
            markDirty();
        }
    }

    public void smeltItem() {
        if (canSmelt()) {
            ItemStack input = getInput();
            ItemStack result = getResult();
            ItemStack output = getOutput();

            if (output.isEmpty()) {
                furnaceItemStacks.set(2, result.copy());
            } else if (output.getItem() == result.getItem()) {
                output.grow(result.getCount());
            }
            if ((input.getItem() == Item.getItemFromBlock(Blocks.SPONGE)) &&
                    (input.getMetadata() == 1) &&
                    !getFuel().isEmpty() &&
                    (getFuel().getItem() == Items.BUCKET)
            ) {
                furnaceItemStacks.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            input.shrink(1);
        }
    }

    public boolean isBurning() {
        return furnaceBurnTime > 0;
    }

    public ItemStack getInput() {
        return furnaceItemStacks.get(0);
    }

    public ItemStack getResult() {
        return type.getResult(getInput());
    }

    public ItemStack getFuel() {
        return furnaceItemStacks.get(1);
    }

    public ItemStack getOutput() {
        return furnaceItemStacks.get(2);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 2) {
            return false;
        } else if (index != 1) {
            return true;
        } else {
            ItemStack itemstack = getFuel();
            return TileEntityFurnace.isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack) && itemstack.getItem() != Items.BUCKET;
        }
    }

    private boolean canSmelt() {
        if (!type.canCraft(getInput())) {
            return false;
        } else {
            ItemStack result = getResult();

            if (result.isEmpty()) {
                return false;
            } else {
                ItemStack output = getOutput();

                if (output.isEmpty()) {
                    return true;
                } else if (!output.isItemEqual(result)) {
                    return false;
                } else if (output.getCount() + result.getCount() <= getInventoryStackLimit() && output.getCount() + result.getCount() <= output.getMaxStackSize()) {
                    return true;
                } else {
                    return output.getCount() + result.getCount() <= result.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        }
    }

    public int getField(int id) {
        switch (id) {
            case 0:
                return furnaceBurnTime;
            case 1:
                return currentItemBurnTime;
            case 2:
                return cookTime;
            default:
                return 0;
        }
    }

    public void setField(int id, int value) {
        switch (id) {
            case 0:
                furnaceBurnTime = value;
                return;
            case 1:
                currentItemBurnTime = value;
                return;
            case 2:
                cookTime = value;
        }
    }

    public int getFieldCount() {
        return 3;
    }

    public void clear() {
        furnaceItemStacks.clear();
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if (side == EnumFacing.DOWN) {
            return SLOTS_BOTTOM;
        } else {
            return side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
        }
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        if (direction == EnumFacing.DOWN && index == 1) {
            Item item = stack.getItem();

            return item == Items.WATER_BUCKET || item == Items.BUCKET;
        }

        return true;
    }

    @Override
    public int getSizeInventory() {
        return furnaceItemStacks.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : furnaceItemStacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return furnaceItemStacks.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(furnaceItemStacks, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(furnaceItemStacks, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = furnaceItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        furnaceItemStacks.set(index, stack);

        if (stack.getCount() > getInventoryStackLimit()) {
            stack.setCount(getInventoryStackLimit());
        }

        if (index == 0 && !flag) {
            cookTime = 0;
            markDirty();
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        if (world.getTileEntity(pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerFurnaceAdvanced(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return FutureMC.ID + ":" + getType().getType().toLowerCase();
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    private SidedInvWrapper handlerTop = new SidedInvWrapper(this, net.minecraft.util.EnumFacing.UP);
    private SidedInvWrapper handlerBottom = new SidedInvWrapper(this, net.minecraft.util.EnumFacing.DOWN);
    private SidedInvWrapper handlerSide = new SidedInvWrapper(this, net.minecraft.util.EnumFacing.WEST);

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, net.minecraft.util.EnumFacing facing) {
        if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            if (facing == EnumFacing.DOWN) {
                return (T) handlerBottom;
            } else if (facing == EnumFacing.UP) {
                return (T) handlerTop;
            } else {
                return (T) handlerSide;
            }
        return super.getCapability(capability, facing);
    }

    public BlockFurnaceAdvanced.FurnaceType getType() {
        return type;
    }

    public static class TileSmoker extends TileFurnaceAdvanced {
        public TileSmoker() {
            super(BlockFurnaceAdvanced.FurnaceType.SMOKER);
        }
    }

    public static class TileBlastFurnace extends TileFurnaceAdvanced {
        public TileBlastFurnace() {
            super(BlockFurnaceAdvanced.FurnaceType.BLAST_FURNACE);
        }
    }
}