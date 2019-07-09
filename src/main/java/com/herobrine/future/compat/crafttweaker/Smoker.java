package com.herobrine.future.compat.crafttweaker;

import com.herobrine.future.FutureMC;
import com.herobrine.future.blocks.BlockFurnaceAdvanced;
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
@ZenClass("mods.minecraftfuture.Smoker")
public class Smoker {
    @ZenMethod
    public static void addValidInput(IItemStack input) {
        if (!BlockFurnaceAdvanced.FurnaceType.SMOKER.canCraft(CraftTweakerMC.getItemStack(input))) {
            CraftTweakerAPI.apply(new AddRecipe(input));
        } else {
            FutureMC.LOGGER.log(Level.WARN, "Failed to add duplicate valid Smoker input for " + input.getDefinition().getId());
        }
    }

    private static class AddRecipe implements IAction {
        private ItemStack input;

        public AddRecipe(IItemStack input) {
            this.input = CraftTweakerMC.getItemStack(input);
        }

        @Override
        public void apply() {
            BlockFurnaceAdvanced.ValidItemExceptionsForSmoker.addSmeltableItem(input);
        }

        @Override
        public String describe() {
            return "Added " + input.getItem().getRegistryName() + " to smeltable items Smoker";
        }
    }

    @ZenMethod
    public static void removeValidInput(IItemStack input) {
        if (!BlockFurnaceAdvanced.ValidItemExceptionsForSmoker.isBlacklisted(CraftTweakerMC.getItemStack(input))) {
            CraftTweakerAPI.apply(new RemoveRecipe(input));
        } else {
            FutureMC.LOGGER.log(Level.WARN, "Failed to remove Smoker input for " + input.getDefinition().getId());
        }
    }

    private static class RemoveRecipe implements IAction {
        private ItemStack input;

        public RemoveRecipe(IItemStack input) {
            this.input = CraftTweakerMC.getItemStack(input);
        }

        @Override
        public void apply() {
            BlockFurnaceAdvanced.ValidItemExceptionsForSmoker.removeSmeltableItem(input);
        }

        @Override
        public String describe() {
            return "Removed " + input.getItem().getRegistryName() + " from smeltable items Smoker";
        }
    }
}