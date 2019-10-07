package thedarkcolour.futuremc.tile;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.items.ItemStackHandler;
import thedarkcolour.core.item.ItemDebugger;
import thedarkcolour.core.tile.InteractionTile;
import thedarkcolour.futuremc.block.BlockCampfire;
import thedarkcolour.futuremc.init.Init;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;

public class TileCampfire extends InteractionTile implements ITickable {
    private final int[] cookingTimes = new int[4];
    private final int[] cookingTotalTimes = new int[4];

    private final ItemStackHandler buffer = new ItemStackHandler(4) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return Recipes.getRecipe(stack) != null;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };

    @Override
    public void update() {
        if (!world.isRemote) {
            if ((getBlockMetadata() & 4) != 0) {
                cookItems();
            } else {
                for (int i = 0; i < 4; ++i) {
                    if (cookingTimes[i] > 0) {
                        cookingTimes[i] = MathHelper.clamp(cookingTimes[i] - 2, 0, cookingTotalTimes[i]);
                    }
                }
            }
        }
    }

    @Override
    public boolean activated(IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        if (stack.getItem() instanceof ItemDebugger) {
            return false;
        }

        Block block = world.getBlockState(pos.up()).getBlock();

        if (!state.getValue(BlockCampfire.LIT)) {
            if (stack.getItem() == Items.FLINT_AND_STEEL || stack.getItem() == Items.FIRE_CHARGE) {
                if (!(block instanceof BlockLiquid | block instanceof BlockFluidBase)) {
                    Init.CAMPFIRE.setLit(world, pos, true);
                    stack.damageItem(1, playerIn);
                }
            }
        } else if (stack.getItem().getToolClasses(stack).contains("shovel")) {
            world.setBlockState(pos, Init.CAMPFIRE.getBlockState().getBaseState().withProperty(BlockCampfire.LIT, false));
        }

        if (state.getValue(BlockCampfire.LIT)) {
            if (!world.isRemote) {
                addItem(stack);
            }
        }

        return true;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    private void cookItems() {
        for (int i = 0; i < 4; ++i) {
            ItemStack stack = buffer.getStackInSlot(i);
            if (!stack.isEmpty()) {
                ++cookingTimes[i];
                if (cookingTimes[i] >= cookingTotalTimes[i]) {
                    if (Recipes.getRecipe(stack) != null)  {
                        ItemStack output = Recipes.getRecipe(stack).output.copy();
                        buffer.setStackInSlot(i, ItemStack.EMPTY);
                        drop(output);
                        cookingTimes[i] = 0;
                    }
                }
            }
        }

        inventoryChanged();
    }

    public void drop(ItemStack stack) {
        if (!world.isRemote) {
            double d0 = (world.rand.nextFloat() * 0.5F) + 0.25D;
            double d1 = (world.rand.nextFloat() * 0.5F) + 0.25D;
            double d2 = (world.rand.nextFloat() * 0.5F) + 0.25D;
            EntityItem entityitem = new EntityItem(world, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, stack);
            entityitem.setDefaultPickupDelay();
            world.spawnEntity(entityitem);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("Buffer")) {
            buffer.deserializeNBT((NBTTagCompound) compound.getTag("Buffer"));
        }
        if (compound.hasKey("CookingTimes", 11)) {
            int[] arr = compound.getIntArray("CookingTimes");
            System.arraycopy(arr, 0, cookingTimes, 0, Math.min(cookingTotalTimes.length, arr.length));
        }

        if (compound.hasKey("CookingTotalTimes", 11)) {
            int[] arr = compound.getIntArray("CookingTotalTimes");
            System.arraycopy(arr, 0, cookingTotalTimes, 0, Math.min(cookingTotalTimes.length, arr.length));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("Buffer", buffer.serializeNBT());
        compound.setIntArray("CookingTimes", cookingTimes);
        compound.setIntArray("CookingTotalTimes", cookingTotalTimes);
        return compound;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 13, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
    }

    public void addItem(ItemStack stack) {
        if (!buffer.isItemValid(0, stack)) {
            return;
        }

        for (int i = 0; i < 4; ++i) {
            ItemStack stack1 = buffer.getStackInSlot(i);
            if (stack1.isEmpty()) {
                cookingTotalTimes[i] = Recipes.getRecipe(stack).duration;
                cookingTimes[i] = 0;
                buffer.setStackInSlot(i, stack.splitStack(1));
                inventoryChanged();
                return;
            }
        }
    }

    public void dropAllItems() {
        for (int i = 0; i < 4; ++i) {
            InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), buffer.getStackInSlot(i));
        }
        inventoryChanged();
    }

    public ItemStackHandler getBuffer() {
        return buffer;
    }

    public int[] getCookingTimes() {
        return cookingTimes;
    }

    public int[] getCookingTotalTimes() {
        return cookingTotalTimes;
    }

    private void inventoryChanged() {
        markDirty();
        getWorld().notifyBlockUpdate(getPos(), world.getBlockState(pos), world.getBlockState(pos), 3);
    }

    public static final class Recipes {
        private static final ArrayList<Recipe> RECIPES = Lists.newArrayListWithExpectedSize(8);

        public static void registerDefaults() {
            recipe(new ItemStack(Items.FISH), new ItemStack(Items.COOKED_FISH), 600, 0.35F);
            recipe(new ItemStack(Items.CHICKEN), new ItemStack(Items.COOKED_CHICKEN), 600, 0.35F);
            recipe(new ItemStack(Items.PORKCHOP), new ItemStack(Items.COOKED_PORKCHOP), 600, 0.35F);
            recipe(new ItemStack(Items.FISH, 1, 1), new ItemStack(Items.COOKED_FISH, 1, 1), 600, 0.35F);
            recipe(new ItemStack(Items.BEEF), new ItemStack(Items.COOKED_BEEF), 600, 0.35F);
            recipe(new ItemStack(Items.MUTTON), new ItemStack(Items.COOKED_MUTTON), 600, 0.35F);
            recipe(new ItemStack(Items.RABBIT), new ItemStack(Items.COOKED_RABBIT), 600, 0.35F);
            recipe(new ItemStack(Items.POTATO), new ItemStack(Items.BAKED_POTATO), 600, 0.35F);
        }

        public static void recipe(ItemStack input, ItemStack output, int duration, float experience) {
            RECIPES.add(new Recipe(input, output, duration, experience));
        }

        public static void remove(ItemStack input) {
            RECIPES.removeIf(recipe -> recipe.input.isItemEqual(input));
        }

        public static Recipe getRecipe(ItemStack input) {
            for (Recipe recipe: RECIPES) {
                if (recipe.input.isItemEqual(input)) {
                    return recipe;
                }
            }

            return null;
        }

        public static Collection<?> getAllRecipes() {
            return RECIPES;
        }
    }

    public static class Recipe {
        public final ItemStack input;
        public final ItemStack output;
        public final int duration;
        public final float experience;

        private Recipe(ItemStack input, ItemStack output, int duration, float experience) {
            this.input = input;
            this.output = output;
            this.duration = duration;
            this.experience = experience;
        }
    }
}