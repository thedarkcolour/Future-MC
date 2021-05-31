package thedarkcolour.futuremc.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;

public final class RecipeUtil {
    static ItemStack toItemStack(IItemStack item) {
        return CraftTweakerMC.getItemStack(item);
    }

    static ItemStack toItemStack(IIngredient ingredient) {
        return CraftTweakerMC.getItemStack(ingredient);
    }

    static void applyAction(IAction action) {
        CraftTweakerAPI.apply(action);
    }
}
