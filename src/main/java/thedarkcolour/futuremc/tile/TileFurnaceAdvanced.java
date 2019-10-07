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
import net.minecraft.item.crafting.FurnaceRecipes;
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
import thedarkcolour.futuremc.block.BlockFurnaceAdvanced;
import thedarkcolour.futuremc.container.ContainerFurnaceAdvanced;

public class TileFurnaceAdvanced extends TileEntityLockable implements ITickable, ISidedInventory {
    private static final int[] SLOTS_TOP = new int[] {0};
    private static final int[] SLOTS_BOTTOM = new int[] {2, 1};
    private static final int[] SLOTS_SIDES = new int[] {1};
    private final BlockFurnaceAdvanced.FurnaceType type;
    private NonNullList<ItemStack> furnaceItemStacks = NonNullList.withSize(3, ItemStack.EMPTY);
    private int furnaceBurnTime;
    private int currentItemBurnTime;
    private int cookTime;

    // Borrows from TileEntityFurnace to reduce headaches
    public TileFurnaceAdvanced(BlockFurnaceAdvanced.FurnaceType type) {
        this.type = type;
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
        compound.setInteger("BurnTime", (short)furnaceBurnTime);
        compound.setInteger("CookTime", (short)cookTime);
        compound.setInteger("CurrentItemBurnTime", currentItemBurnTime);
        ItemStackHelper.saveAllItems(compound, furnaceItemStacks);

        return compound;
    }

    @Override
    public void update() {
        boolean flag = this.isBurning();
        boolean flag1 = false;

        if (isBurning()) {
            this.furnaceBurnTime -= 2;
            if(furnaceBurnTime < 0) furnaceBurnTime = 0;
        }

        if (!world.isRemote) {
            ItemStack itemstack = this.furnaceItemStacks.get(1);

            int totalCookTime = 100;
            if (isBurning() || !itemstack.isEmpty() && !furnaceItemStacks.get(0).isEmpty()) {
                if (!isBurning() && canSmelt()) {
                    furnaceBurnTime = TileEntityFurnace.getItemBurnTime(itemstack);
                    currentItemBurnTime = furnaceBurnTime;

                    if (this.isBurning()) {
                        flag1 = true;

                        if (!itemstack.isEmpty()) {
                            Item item = itemstack.getItem();
                            itemstack.shrink(1);

                            if (itemstack.isEmpty()) {
                                ItemStack item1 = item.getContainerItem(itemstack);
                                this.furnaceItemStacks.set(1, item1);
                            }
                        }
                    }
                }

                if (isBurning() && canSmelt()) {
                    ++cookTime;

                    if (cookTime == totalCookTime) {
                        cookTime = 0;
                        smeltItem();
                        flag1 = true;
                    }
                } else {
                    cookTime = 0;
                }
            } else if (!isBurning() && cookTime > 0) {
                cookTime = MathHelper.clamp(cookTime - 2, 0, totalCookTime);
            }

            if (flag != isBurning())  {
                flag1 = true;
                BlockFurnaceAdvanced.setState(isBurning(), world, pos);
            }
        }

        if (flag1) {
            markDirty();
        }
    }

    public void smeltItem() {
        if (canSmelt()) {
            ItemStack itemstack = furnaceItemStacks.get(0);
            ItemStack itemstack1 = FurnaceRecipes.instance().getSmeltingResult(itemstack);
            ItemStack itemstack2 = furnaceItemStacks.get(2);

            if (itemstack2.isEmpty()) {
                furnaceItemStacks.set(2, itemstack1.copy());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.grow(itemstack1.getCount());
            }
            if ((itemstack.getItem() == Item.getItemFromBlock(Blocks.SPONGE)) && (itemstack.getMetadata() == 1) &&
                    !furnaceItemStacks.get(1).isEmpty() && (furnaceItemStacks.get(1).getItem() == Items.BUCKET)) {
                furnaceItemStacks.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemstack.shrink(1);
        }
    }

    public boolean isBurning() {
        return furnaceBurnTime > 0;
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
            ItemStack itemstack = furnaceItemStacks.get(1);
            return TileEntityFurnace.isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack) && itemstack.getItem() != Items.BUCKET;
        }
    }

    private boolean canSmelt() {
        if (!getType().canCraft(furnaceItemStacks.get(0))) {
            return false;
        } else {
            ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(furnaceItemStacks.get(0));

            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = furnaceItemStacks.get(2);

                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.isItemEqual(itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= this.getInventoryStackLimit() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) {
                    return true;
                } else {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
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
                break;
            case 1:
                currentItemBurnTime = value;
                break;
            case 2:
                cookTime = value;
        }
    }

    public int getFieldCount() {
        return 3;
    }

    public void clear() {
        this.furnaceItemStacks.clear();
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
        return this.isItemValidForSlot(index, itemStackIn);
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
        for (ItemStack itemstack : this.furnaceItemStacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.furnaceItemStacks.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.furnaceItemStacks, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.furnaceItemStacks, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.furnaceItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.furnaceItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag) {
            this.cookTime = 0;
            this.markDirty();
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
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
        return FutureMC.ID + ":" + getType().getName().toLowerCase();
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

    @Override @SuppressWarnings("unchecked")
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, net.minecraft.util.EnumFacing facing) {
        if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            if (facing == EnumFacing.DOWN) {
                return (T) handlerBottom;
            }
            else if (facing == EnumFacing.UP) {
                return (T) handlerTop;
            }
            else{
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