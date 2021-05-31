package thedarkcolour.futuremc.compat.crafttweaker;

import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.block.villagepillage.ComposterBlock;
import thedarkcolour.futuremc.tile.TileComposter;

import static thedarkcolour.futuremc.compat.crafttweaker.RecipeUtil.applyAction;
import static thedarkcolour.futuremc.compat.crafttweaker.RecipeUtil.toItemStack;

@ZenRegister
@ZenClass("mods.futuremc.Composter")
public final class Composter {
    @ZenMethod
    public static void addValidItem(IIngredient stack, int rarity) {
        if (ComposterBlock.ItemsForComposter.getChance(toItemStack(stack)) == -1) {
            if (!TileComposter.isBoneMeal(toItemStack(stack))) {
                applyAction(new Add(stack, rarity));
            } else {
                FutureMC.LOGGER.log(Level.ERROR, "Cannot add bone meal to compostable item!");
            }
        } else {
            FutureMC.LOGGER.log(Level.WARN, "Failed to add duplicate recipe for item " + stack.toCommandString());
        }
    }

    @ZenMethod
    public static void addValidItem(IOreDictEntry ore, int rarity) {
        for (IItemStack a : ore.getItems()) {
            addValidItem(a, rarity);
        }
    }

    private static final class Add implements IAction {
        private final ItemStack stack;
        private final int rarity;

        private Add(IIngredient stack, int rarity) {
            this.stack = toItemStack(stack);
            this.rarity = rarity;
        }

        @Override
        public void apply() {
            ComposterBlock.ItemsForComposter.add(stack, rarity);
        }

        @Override
        public String describe() {
            return "Adding recipe for item " + stack.getDisplayName();
        }
    }

    @ZenMethod
    public static void removeValidItem(IIngredient stack) {
        if (ComposterBlock.ItemsForComposter.getChance(toItemStack(stack)) != -1) {
            applyAction(new Remove(toItemStack(stack)));
        } else {
            FutureMC.LOGGER.log(Level.WARN, "Cannot remove non-existent item from valid composter item " + stack.toCommandString());
        }
    }

    private static final class Remove implements IAction {
        private final ItemStack stack;

        private Remove(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public void apply() {
            ComposterBlock.ItemsForComposter.remove(stack);
        }

        @Override
        public String describe() {
            return "Removed item " + stack.getItem().getRegistryName() + " from the list of valid Composter item.";
        }
    }

    @ZenMethod
    public static void replaceValidItemChance(IIngredient stack, int newRarity) {
        if (ComposterBlock.ItemsForComposter.getChance(toItemStack(stack)) != -1) {
            applyAction(new ReplaceItemChance(toItemStack(stack), newRarity));
        } else {
            FutureMC.LOGGER.log(Level.WARN, "Cannot change chance for invalid item " + stack.toCommandString() +
                    " If you wish to make the item valid, use mods.futuremc.Composter.addValidItem");
        }
    }

    private static final class ReplaceItemChance implements IAction {
        private final ItemStack stack;
        private final int newRarity, oldRarity;

        private ReplaceItemChance(ItemStack stack, int newRarity) {
            this.newRarity = newRarity;
            this.stack = stack;
            this.oldRarity = ComposterBlock.ItemsForComposter.getChance(stack);
        }

        @Override
        public void apply() {
            ComposterBlock.ItemsForComposter.remove(stack);
            ComposterBlock.ItemsForComposter.add(stack, newRarity);
        }

        @Override
        public String describe() {
            return "Changed Composter value for item " + stack.getItem().getRegistryName() + " from " + oldRarity + " to " + newRarity;
        }
    }

    @ZenMethod
    public static void clearValidItems() {
        ComposterBlock.ItemsForComposter.clear();
    }
}