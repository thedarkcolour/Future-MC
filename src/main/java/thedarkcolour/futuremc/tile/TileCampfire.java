package thedarkcolour.futuremc.tile;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.ItemStackHandler;
import thedarkcolour.core.tile.InteractionTile;
import thedarkcolour.futuremc.block.BlockCampfire;

import javax.annotation.Nonnull;
import java.util.ArrayList;

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

        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            super.setStackInSlot(slot, stack);
            markDirty();
        }
    };

    @Override
    public void update() {
        if (!world.isRemote) {
            if (getBlockMetadata() == 1) {
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
        if (state.getValue(BlockCampfire.LIT)) {
            ItemStack stack = playerIn.getHeldItem(hand);
            if (!world.isRemote) {
                addItem(stack);
            }
        }

        return true;
    }

    private void cookItems() {
        for (int i = 0; i < 4; ++i) {
            ItemStack stack = buffer.getStackInSlot(i);
            if (!stack.isEmpty()) {
                ++cookingTimes[i];
                if (cookingTimes[i] >= cookingTotalTimes[i]) {
                    if (Recipes.getRecipe(stack) != null)  {
                        ItemStack output = Recipes.getRecipe(stack).output;
                        Block.spawnAsEntity(world, pos, output);
                        buffer.setStackInSlot(i, ItemStack.EMPTY);
                    }
                }
            }
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
            System.arraycopy(arr, 0, cookingTimes, 0, Math.min(cookingTotalTimes.length, arr.length));
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
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 13, getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setTag("Buffer", buffer.serializeNBT());
        return compound;
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
                return;
            }
        }
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

    public static final class Recipes {
        private static final ArrayList<Recipe> RECIPES = Lists.newArrayListWithExpectedSize(10);

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
    }

    private static class Recipe {
        private final ItemStack input;
        private final ItemStack output;
        private final int duration;
        private final float experience;

        private Recipe(ItemStack input, ItemStack output, int duration, float experience) {
            this.input = input;
            this.output = output;
            this.duration = duration;
            this.experience = experience;
        }
    }
}