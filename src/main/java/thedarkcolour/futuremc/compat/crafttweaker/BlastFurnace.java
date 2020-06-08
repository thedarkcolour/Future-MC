package thedarkcolour.futuremc.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thedarkcolour.futuremc.recipe.furnace.BlastFurnaceRecipes;

@ZenRegister
@ZenClass("mods.futuremc.BlastFurnace")
public final class BlastFurnace {
    /**
     * Adds a recipe with the specified input and output.
     *
     * Does not warn if you have duplicate recipes,
     * so make sure you aren't adding any duplicate recipes.
     *
     * @param input the input for the recipe.
     * @param output the output for the recipe.
     */
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output) {
        CraftTweakerAPI.apply(new AddRecipe(input, output));
    }

    /**
     * Removes any recipes that have the specified input.
     *
     * @param input the input to check for
     */
    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        CraftTweakerAPI.apply(new RemoveRecipe(input));
    }

    /**
     * Removes all default recipes.
     */
    @ZenMethod
    public static void clearRecipes() {
        BlastFurnaceRecipes.INSTANCE.clear();
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
            BlastFurnaceRecipes.INSTANCE.addRecipe(input, output);
        }

        @Override
        public String describe() {
            return "Adding Blast Furnace recipe (input: " + input.toString() + ") -> (output: " + output.toString() + ")";
        }
    }

    private static final class RemoveRecipe implements IAction {
        private final ItemStack input;

        private RemoveRecipe(IItemStack input) {
            this.input = CraftTweakerMC.getItemStack(input);
        }

        @Override
        public void apply() {
            BlastFurnaceRecipes.INSTANCE.removeRecipe(input);
        }

        @Override
        public String describe() {
            return "Removed " + input.getItem().getRegistryName() + " from smeltable item BlastFurnace";
        }
    }
}