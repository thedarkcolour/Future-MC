package com.herobrine.future.compat.crafttweaker;

import com.herobrine.future.FutureMC;
import com.herobrine.future.blocks.BlockComposter;
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
@ZenClass("mods.minecraftfuture.Composter")
public class Composter {
    @ZenMethod
    public static void addValidItem(IItemStack stack, int rarity) {
        if(BlockComposter.ItemsForComposter.getChance(CraftTweakerMC.getItemStack(stack)) == -1) CraftTweakerAPI.apply(new Add(stack, rarity));
        else FutureMC.LOGGER.log(Level.WARN, "Failed to add duplicate recipe for item " + stack.getDisplayName());
    }

    private static class Add implements IAction {
        private final ItemStack stack;
        private final int rarity;

        public Add(IItemStack stack, int rarity) {
            this.stack = CraftTweakerMC.getItemStack(stack);
            this.rarity = rarity;
        }

        @Override
        public void apply() {
            BlockComposter.ItemsForComposter.add(stack, rarity);
        }

        @Override
        public String describe() {
            return "Adding recipe for item " + stack.getDisplayName();
        }
    }

    @ZenMethod
    public static void removeValidItem(IItemStack stack) {
        if(BlockComposter.ItemsForComposter.getChance(CraftTweakerMC.getItemStack(stack)) != -1)
            CraftTweakerAPI.apply(new Remove(CraftTweakerMC.getItemStack(stack)));
        else FutureMC.LOGGER.log(Level.WARN, "Cannot remove non-existent item from valid composter items " + CraftTweakerMC.getItemStack(stack).getItem().getRegistryName());
    }

    private static class Remove implements IAction {
        private final ItemStack stack;

        public Remove(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public void apply() {
            BlockComposter.ItemsForComposter.remove(stack);
        }

        @Override
        public String describe() {
            return "Removed item " + stack.getItem().getRegistryName() + " from the list of valid Composter items.";
        }
    }

    @ZenMethod
    public static void replaceValidItemChance(IItemStack stack, int newRarity) {
        if(BlockComposter.ItemsForComposter.getChance(CraftTweakerMC.getItemStack(stack)) != -1)
            CraftTweakerAPI.apply(new ReplaceItemChance(CraftTweakerMC.getItemStack(stack), newRarity));
        else CraftTweakerAPI.getLogger().logError("Cannot change chance for invalid item " + CraftTweakerMC.getItemStack(stack).getItem().getRegistryName() +
                " If you wish to make the item valid, use mods.minecraftfuture.Composter.addValidItem");
    }

    private static class ReplaceItemChance implements IAction {
        private final ItemStack stack;
        private final int newRarity, oldRarity;

        public ReplaceItemChance(ItemStack stack, int newRarity) {
            this.newRarity = newRarity;
            this.stack = stack;
            this.oldRarity = BlockComposter.ItemsForComposter.getChance(stack);
        }

        @Override
        public void apply() {
            BlockComposter.ItemsForComposter.remove(stack);
            BlockComposter.ItemsForComposter.add(stack, newRarity);
        }

        @Override
        public String describe() {
            return "Changed Composter value for item " + stack.getItem().getRegistryName() + " from " + oldRarity + " to " + newRarity;
        }
    }
}