package thedarkcolour.futuremc.compat.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thedarkcolour.futuremc.recipe.StonecutterRecipes;

import java.util.Arrays;

@ZenRegister
@ZenClass("mods.minecraftfuture.Stonecutter")
public final class Stonecutter {
    @ZenMethod
    public static void addOutputs(IItemStack input, IItemStack... outputs) {
        StonecutterRecipes.addOrCreateRecipe(CraftTweakerMC.getItemStack(input), Arrays.stream(outputs).map(CraftTweakerMC::getItemStack).toArray(ItemStack[]::new));
    }

    @ZenMethod
    public static void removeOutputs(IItemStack input, IItemStack... outputs) {
        StonecutterRecipes.removeOutputs(CraftTweakerMC.getItemStack(input), Arrays.stream(outputs).map(CraftTweakerMC::getItemStack).toArray(ItemStack[]::new));
    }

    @ZenMethod
    public static void removeAllOutputsForInput(IItemStack input) {
        StonecutterRecipes.removeRecipe(CraftTweakerMC.getItemStack(input));
    }

    @ZenMethod
    public static void clearRecipes() {
        StonecutterRecipes.clear();
    }
}