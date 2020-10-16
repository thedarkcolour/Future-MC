package thedarkcolour.futuremc.container;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.SoundCategory;
import thedarkcolour.futuremc.recipe.SmithingRecipe;
import thedarkcolour.futuremc.registry.FContainers;
import thedarkcolour.futuremc.registry.FRecipes;
import thedarkcolour.futuremc.registry.FSounds;

import javax.annotation.Nullable;
import java.util.Optional;

public class SmithingContainers extends Container {
    private final IWorldPosCallable worldPos;
    private SmithingRecipe recipe;
    private final CraftResultInventory output = new CraftResultInventory();
    private final Inventory input = new Inventory();

    protected SmithingContainers(int id, PlayerInventory playerInv) {
        this(FContainers.INSTANCE.getSMITHING_TABLE(), id, playerInv, IWorldPosCallable.DUMMY);
    }

    protected SmithingContainers(int id, PlayerInventory playerInv, IWorldPosCallable worldPos) {
        this(FContainers.INSTANCE.getSMITHING_TABLE(), id, playerInv, worldPos);
    }

    protected SmithingContainers(@Nullable ContainerType<?> type, int id, PlayerInventory playerInv, IWorldPosCallable worldPos) {
        super(type, id);
        this.worldPos = worldPos;

        addSlot(new Slot(input, 0, 27, 47));
        addSlot(new Slot(input, 1, 76, 47));
        addSlot(new Slot(output, 2, 134, 47) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }

            @Override
            public boolean canTakeStack(PlayerEntity playerIn) {
                return recipe != null;
            }

            @Override
            public ItemStack onTake(PlayerEntity playerIn, ItemStack stack) {
                ItemStack stack1 = input.getStackFrom(1);
                stack1.shrink(recipe.getMaterialCost());
                input.getStackFrom(0).shrink(1);
                input.setInventorySlotContents(1, stack1);
                worldPos.consume(((worldIn, pos) -> {
                    worldIn.playSound(null, pos, FSounds.INSTANCE.getBLOCK_SMITHING_TABLE_USE(), SoundCategory.BLOCKS, 1.0f, worldIn.rand.nextFloat() * 0.1f + 0.9f);
                }));
                return stack.copy();
            }
        });

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        super.onCraftMatrixChanged(inventoryIn);
        if (inventoryIn == input) {
            ItemStack stack = input.getStackFrom(0);
            worldPos.consume((worldIn, pos) -> {
                Optional<SmithingRecipe> recipe = worldIn.getRecipeManager().getRecipe(FRecipes.INSTANCE.getSMITHING(), input, worldIn);

                if (recipe.isPresent()) {
                    ItemStack result = recipe.get().getRecipeOutput().copy();
                    CompoundNBT tag = stack.getTag();
                    result.setTag(tag);
                    output.setInventorySlotContents(0, result);
                } else {
                    output.clear();
                }
            });
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return worldPos.applyOrElse((worldIn, pos) -> {
            if (worldIn.getBlockState(pos).getBlock() != Blocks.SMITHING_TABLE) {
                return false;
            } else {
                return playerIn.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64;
            }
        }, true);
    }
}