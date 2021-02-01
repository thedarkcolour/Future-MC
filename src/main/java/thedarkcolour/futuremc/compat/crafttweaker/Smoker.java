package thedarkcolour.futuremc.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thedarkcolour.futuremc.recipe.furnace.SmokerRecipes;

@ZenRegister
@ZenClass("mods.futuremc.Smoker")
public final class Smoker {
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output) {
        CraftTweakerAPI.apply(new AddRecipe(input, output));
    }

    @ZenMethod
    public static void addRecipe(IOreDictEntry input, IItemStack output) {
        for (IItemStack i : input.getItems()) {
            addRecipe(i, output);
        }
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        CraftTweakerAPI.apply(new RemoveRecipe(input));
    }

    @ZenMethod
    public static void clearRecipes() {
        SmokerRecipes.INSTANCE.clear();
    }

    private static final class AddRecipe implements IAction {
        private final ItemStack input;
        private final ItemStack output;

        private AddRecipe(IItemStack input, IItemStack output) {
            this.input = CraftTweakerMC.getItemStack(input);
            this.output = CraftTweakerMC.getItemStack(output);
        }

        @Override
        public void apply() {
            SmokerRecipes.INSTANCE.addRecipe(input, output);
        }

        @Override
        public String describe() {
            return "Adding Smoker recipe (input: " + input.toString() + ") -> (output: " + output.toString() + ")";
        }
    }

    private static final class RemoveRecipe implements IAction {
        private final ItemStack input;

        private RemoveRecipe(IItemStack input) {
            this.input = CraftTweakerMC.getItemStack(input);
        }

        @Override
        public void apply() {
            SmokerRecipes.INSTANCE.removeRecipe(input);
        }

        @Override
        public String describe() {
            return "Removed " + input.getItem().getRegistryName() + " from smeltable item Smoker";
        }
    }
}