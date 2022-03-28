package thedarkcolour.futuremc.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thedarkcolour.futuremc.block.villagepillage.ComposterBlock;
import thedarkcolour.futuremc.tile.TileComposter;

@ZenRegister
@ZenClass("mods.futuremc.Composter")
public final class Composter {
    @ZenMethod
    public static void addValidItem(IIngredient stack, int rarity) {
        CraftTweakerAPI.apply(new Add(stack, rarity));
    }

    @ZenMethod
    public static void removeValidItem(IItemStack stack) {
        CraftTweakerAPI.apply(new Remove(stack));
    }

    @ZenMethod
    public static void replaceValidItemChance(IIngredient stack, int newRarity) {
        CraftTweakerAPI.apply(new Add(stack, newRarity));
    }

    @ZenMethod
    public static void clearValidItems() {
        CraftTweakerAPI.apply(new RecipeUtil.NamedAction("Cleared composter recipes", ComposterBlock.ItemsForComposter::clear));
    }

    private static final class Add implements IAction {
        private final IIngredient ingredient;
        private final byte rarity;

        private Add(IIngredient ingredient, int rarity) {
            this.ingredient = ingredient;
            this.rarity = (byte) rarity;
        }

        @Override
        public void apply() {
            for (IItemStack item : ingredient.getItems()) {
                ItemStack stack = CraftTweakerMC.getItemStack(item);

                if (TileComposter.isBoneMeal(stack)) {
                    CraftTweakerAPI.logWarning("Cannot add bone meal as compostable item!");
                    return;
                }
            }

            ComposterBlock.ItemsForComposter.INSTANCE.add(CraftTweakerMC.getIngredient(ingredient), rarity);
        }

        @Override
        public String describe() {
            return "Adding recipe for item " + ingredient.toCommandString();
        }
    }

    private static final class Remove implements IAction {
        private final IItemStack stack;

        private Remove(IItemStack stack) {
            this.stack = stack;
        }

        @Override
        public void apply() {
            ComposterBlock.ItemsForComposter.remove(CraftTweakerMC.getItemStack(stack));
        }

        @Override
        public String describe() {
            return "Removed item " + stack.toCommandString() + " from the list of valid Composter item.";
        }
    }
}