package thedarkcolour.futuremc.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thedarkcolour.futuremc.block.villagepillage.ComposterBlock;
import thedarkcolour.futuremc.tile.TileComposter;

import static thedarkcolour.futuremc.compat.crafttweaker.RecipeUtil.applyAction;
import static thedarkcolour.futuremc.compat.crafttweaker.RecipeUtil.toItemStack;

@ZenRegister
@ZenClass("mods.futuremc.Composter")
public final class Composter {
    @ZenMethod
    public static void addValidItem(IIngredient stack, int rarity) {
        applyAction(new Add(stack, rarity, false));
    }

    @ZenMethod
    public static void removeValidItem(IIngredient stack) {
        applyAction(new Remove(stack));
    }

    @ZenMethod
    public static void replaceValidItemChance(IIngredient stack, int newRarity) {
        applyAction(new Add(stack, newRarity, true));
    }

    @ZenMethod
    public static void clearValidItems() {
        applyAction(new RecipeUtil.NamedAction("Cleared composter recipes", ComposterBlock.ItemsForComposter::clear));
    }

    private static final class Add implements IAction {
        private final IIngredient ingredient;
        private final int rarity;
        private final boolean replace;

        private Add(IIngredient ingredient, int rarity, boolean replace) {
            this.ingredient = ingredient;
            this.rarity = rarity;
            this.replace = replace;
        }

        @Override
        public void apply() {
            for (IItemStack item : ingredient.getItems()) {
                ItemStack stack = toItemStack(item);

                if (TileComposter.isBoneMeal(stack)) {
                    CraftTweakerAPI.logWarning("Cannot add bone meal as compostable item!");
                } else {
                    if (ComposterBlock.ItemsForComposter.getChance(stack) == -1) {
                        if (!replace) {
                            CraftTweakerAPI.logWarning("Failed to add duplicate recipe for item " + item.toCommandString());
                            continue;
                        }
                    } else {
                        if (replace) {
                            CraftTweakerAPI.logWarning("Tried change chance for invalid item " + item.toCommandString() +
                                    " If you wish to add a chance to the item, use mods.futuremc.Composter.addValidItem");
                            continue;
                        }
                    }

                    ComposterBlock.ItemsForComposter.add(stack, rarity);
                }
            }
        }

        @Override
        public String describe() {
            return "Adding recipe for item " + ingredient.toCommandString();
        }
    }

    private static final class Remove implements IAction {
        private final IIngredient ingredient;

        private Remove(IIngredient ingredient) {
            this.ingredient = ingredient;
        }

        @Override
        public void apply() {
            for (IItemStack item : ingredient.getItems()) {
                ItemStack stack = toItemStack(item);

                if (ComposterBlock.ItemsForComposter.getChance(stack) != -1) {
                    ComposterBlock.ItemsForComposter.remove(stack);
                } else {
                    CraftTweakerAPI.logWarning("Tried to remove non-existent item from valid composter items: " + item.toCommandString());
                }
            }
        }

        @Override
        public String describe() {
            return "Removed item " + ingredient.toCommandString() + " from the list of valid Composter item.";
        }
    }
}