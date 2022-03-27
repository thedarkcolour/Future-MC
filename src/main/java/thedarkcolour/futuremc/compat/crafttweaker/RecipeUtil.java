package thedarkcolour.futuremc.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public final class RecipeUtil {
    static ItemStack toItemStack(IItemStack item) {
        return CraftTweakerMC.getItemStack(item);
    }

    static Ingredient toIngredient(IIngredient ingredient) {
        return CraftTweakerMC.getIngredient(ingredient);
    }

    static void applyAction(IAction action) {
        CraftTweakerAPI.apply(action);
    }

    static class NamedAction implements IAction {
        private final String name;
        private final Runnable action;

        NamedAction(String name, Runnable action) {
            this.name = name;
            this.action = action;
        }

        @Override
        public void apply() {
            action.run();
        }

        @Override
        public String describe() {
            return name;
        }
    }
}
