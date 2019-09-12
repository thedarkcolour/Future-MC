package com.herobrine.future.compat.crafttweaker;

import com.herobrine.future.FutureMC;
import com.herobrine.future.block.BlockFurnaceAdvanced;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.minecraftfuture.BlastFurnace")
public final class BlastFurnace {
    @ZenMethod
    public static void addValidInput(IItemStack input) {
        if (!BlockFurnaceAdvanced.FurnaceType.BLAST_FURNACE.canCraft(CraftTweakerMC.getItemStack(input))) {
            CraftTweakerAPI.apply(new BlastFurnace.AddRecipe(input));
        } else {
            FutureMC.LOGGER.log(Level.WARN, "Failed to add duplicate valid BlastFurnace input for " + input.getDefinition().getId());
        }
    }

    private static class AddRecipe implements IAction {
        private ItemStack input;

        private AddRecipe(IItemStack input) {
            this.input = CraftTweakerMC.getItemStack(input);
        }

        @Override
        public void apply() {
            BlockFurnaceAdvanced.ValidItemExceptionsForBlastFurnace.addSmeltableItem(input);
        }

        @Override
        public String describe() {
            return "Added " + input.getItem().getRegistryName() + " to smeltable items BlastFurnace";
        }
    }

    @ZenMethod
    public static void removeValidInput(IItemStack input) {
        if (!BlockFurnaceAdvanced.ValidItemExceptionsForBlastFurnace.isBlacklisted(CraftTweakerMC.getItemStack(input))) {
            CraftTweakerAPI.apply(new BlastFurnace.RemoveRecipe(input));
        } else {
            FutureMC.LOGGER.log(Level.WARN, "Failed to remove BlastFurnace input for " + input.getDefinition().getId());
        }
    }

    private static class RemoveRecipe implements IAction {
        private ItemStack input;

        private RemoveRecipe(IItemStack input) {
            this.input = CraftTweakerMC.getItemStack(input);
        }

        @Override
        public void apply() {
            BlockFurnaceAdvanced.ValidItemExceptionsForBlastFurnace.removeSmeltableItem(input);
        }

        @Override
        public String describe() {
            return "Removed " + input.getItem().getRegistryName() + " from smeltable items BlastFurnace";
        }
    }
}