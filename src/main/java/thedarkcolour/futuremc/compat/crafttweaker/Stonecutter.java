package thedarkcolour.futuremc.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thedarkcolour.futuremc.recipe.stonecutter.StonecutterRecipes;

import java.util.Arrays;

@ZenRegister
@ZenClass("mods.minecraftfuture.Stonecutter")
public final class Stonecutter {
    @ZenMethod
    public static void addOutputs(IItemStack input, IItemStack... outputs) {
        CraftTweakerAPI.apply(Action.of(() -> StonecutterRecipes.INSTANCE.addOrCreateRecipe(CraftTweakerMC.getItemStack(input), Arrays.stream(outputs).map(CraftTweakerMC::getItemStack).toArray(ItemStack[]::new))));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        CraftTweakerAPI.apply(Action.of(() -> StonecutterRecipes.INSTANCE.removeRecipe(CraftTweakerMC.getItemStack(input))));
    }

    @ZenMethod
    public static void removeOutputs(IItemStack input, IItemStack... outputs) {
        CraftTweakerAPI.apply(Action.of(() -> StonecutterRecipes.INSTANCE.removeOutputs(CraftTweakerMC.getItemStack(input), Arrays.stream(outputs).map(CraftTweakerMC::getItemStack).toArray(ItemStack[]::new))));
    }

    @ZenMethod
    public static void removeAllOutputsForInput(IItemStack input) {
        CraftTweakerAPI.apply(Action.of(() -> StonecutterRecipes.INSTANCE.removeRecipe(CraftTweakerMC.getItemStack(input))));
    }

    @ZenMethod
    public static void clearRecipes() {
        CraftTweakerAPI.apply(Action.of(Stonecutter::clearRecipes));
    }

    private interface Action extends IAction {
        @Override
        default String describe() {
            return "Stonecutter";
        }

        static Action of(Runnable r) {
            return r::run;
        }
    }
}