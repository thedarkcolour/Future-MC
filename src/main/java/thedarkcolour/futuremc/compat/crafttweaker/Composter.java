package thedarkcolour.futuremc.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.block.BlockComposter;
import thedarkcolour.futuremc.tile.TileComposter;

@ZenRegister
@ZenClass("mods.minecraftfuture.Composter")
public final class Composter {
    @ZenMethod
    public static void addValidItem(IIngredient stack, int rarity) {
        if(BlockComposter.ItemsForComposter.getChance(CraftTweakerMC.getItemStack(stack)) == -1)  {
            if(!TileComposter.isBoneMeal(CraftTweakerMC.getItemStack(stack))) {
                CraftTweakerAPI.apply(new Add(stack, rarity));
            } else {
                FutureMC.logger.log(Level.ERROR, "Cannot add bone meal to compostable items!");
            }
        } else {
            FutureMC.logger.log(Level.WARN, "Failed to add duplicate recipe for item " + stack.toCommandString());
        }
    }

    private static class Add implements IAction {
        private final ItemStack stack;
        private final int rarity;

        private Add(IIngredient stack, int rarity) {
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
    public static void removeValidItem(IIngredient stack) {
        if(BlockComposter.ItemsForComposter.getChance(CraftTweakerMC.getItemStack(stack)) != -1) {
            CraftTweakerAPI.apply(new Remove(CraftTweakerMC.getItemStack(stack)));
        } else {
            FutureMC.logger.log(Level.WARN, "Cannot remove non-existent item from valid composter items " + stack.toCommandString());
        }
    }

    private static class Remove implements IAction {
        private final ItemStack stack;

        private Remove(ItemStack stack) {
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
    public static void replaceValidItemChance(IIngredient stack, int newRarity) {
        if(BlockComposter.ItemsForComposter.getChance(CraftTweakerMC.getItemStack(stack)) != -1) {
            CraftTweakerAPI.apply(new ReplaceItemChance(CraftTweakerMC.getItemStack(stack), newRarity));
        } else {
            FutureMC.logger.log(Level.WARN, "Cannot change chance for invalid item " + stack.toCommandString() +
                    " If you wish to make the item valid, use mods.minecraftfuture.Composter.addValidItem");
        }
    }

    private static class ReplaceItemChance implements IAction {
        private final ItemStack stack;
        private final int newRarity, oldRarity;

        private ReplaceItemChance(ItemStack stack, int newRarity) {
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