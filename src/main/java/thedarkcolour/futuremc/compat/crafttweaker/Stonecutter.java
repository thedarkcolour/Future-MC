package thedarkcolour.futuremc.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thedarkcolour.futuremc.recipe.stonecutter.StonecutterRecipes;

import java.util.Arrays;

@ZenRegister
@ZenClass("mods.futuremc.Stonecutter")
public final class Stonecutter {
    @ZenMethod
    public static void addOutputs(IItemStack input, IItemStack... outputs) {
        Arrays.stream(outputs).forEach(output -> addOutput(input, output));
    }

    @ZenMethod
    public static void addOutputs(IOreDictEntry input, IItemStack... outputs) {
        for (IItemStack i : input.getItems()) {
            Arrays.stream(outputs).forEach(output -> addOutput(i, output));
        }
    }

    @ZenMethod
    public static void addOutput(IItemStack input, IItemStack output) {
        CraftTweakerAPI.apply(Action.of(() -> StonecutterRecipes.INSTANCE.addRecipe(CraftTweakerMC.getItemStack(input), CraftTweakerMC.getItemStack(output))));
    }

    @ZenMethod
    public static void addOutputs(IOreDictEntry input, IItemStack output) {
        for (IItemStack i : input.getItems()) {
            addOutput(i, output);
        }
    }

    @ZenMethod
    public static void removeOutputs(IItemStack input, IItemStack... outputs) {
        Arrays.stream(outputs).forEach(output -> {
            CraftTweakerAPI.apply(Action.of(() -> StonecutterRecipes.INSTANCE.removeRecipe(CraftTweakerMC.getItemStack(input), CraftTweakerMC.getItemStack(output))));
        });
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        CraftTweakerAPI.apply(Action.of(() -> StonecutterRecipes.INSTANCE.removeRecipeForInput(CraftTweakerMC.getItemStack(input))));
    }

    // todo turn into overloaded version of #removeOutputs and add separate functionality for #removeRecipe
    @ZenMethod
    public static void removeAllOutputsForInput(IItemStack input) {
        CraftTweakerAPI.apply(Action.of(() -> StonecutterRecipes.INSTANCE.removeRecipeForInput(CraftTweakerMC.getItemStack(input))));
    }

    @ZenMethod
    public static void clearRecipes() {
        CraftTweakerAPI.apply(Action.of(StonecutterRecipes.INSTANCE::clear));
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

    // initialize recipes todo check if this is needed
    static {
        StonecutterRecipes.INSTANCE.getClass();
    }
}