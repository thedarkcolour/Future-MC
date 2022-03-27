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
import thedarkcolour.futuremc.recipe.campfire.CampfireRecipes;

@ZenRegister
@ZenClass("mods.futuremc.Campfire")
public final class Campfire {
    @ZenMethod
    public static void addRecipe(IIngredient input, IItemStack output, int duration) {
        CraftTweakerAPI.apply(new AddRecipe(input, output, duration));
    }

    private static final class AddRecipe implements IAction {
        private final IIngredient input;
        private final IItemStack output;
        private final int duration;
        //private final int experience; Seems to not be given to the player

        private AddRecipe(IIngredient input, IItemStack output, int duration) {
            this.input = input;
            this.output = output;
            this.duration = duration;
        }

        @Override
        public void apply() {
            ItemStack output = CraftTweakerMC.getItemStack(this.output);

            for (IItemStack item : input.getItems()) {
                if (CampfireRecipes.INSTANCE.getRecipe(CraftTweakerMC.getItemStack(input)) != null) {
                    CraftTweakerAPI.logWarning("Cannot add duplicate recipe for " + input.toCommandString());
                } else {
                    CampfireRecipes.INSTANCE.addRecipe(CraftTweakerMC.getItemStack(item), output, duration);
                }
            }
        }

        @Override
        public String describe() {
            return "Added recipe for " + input.toCommandString();
        }
    }

    @ZenMethod
    public static void removeRecipe(IItemStack stack) {
        if (CampfireRecipes.INSTANCE.getRecipe(CraftTweakerMC.getItemStack(stack)) != null) {
            CraftTweakerAPI.apply(new RemoveRecipe(stack));
        }
    }

    private static final class RemoveRecipe implements IAction {
        private final IItemStack input;

        private RemoveRecipe(IItemStack input) {
            this.input = input;
        }

        @Override
        public void apply() {
            CampfireRecipes.INSTANCE.removeRecipeForInput(CraftTweakerMC.getItemStack(input));
        }

        @Override
        public String describe() {
            return "Added recipe for" + input.getDefinition().getId();
        }
    }

    @ZenMethod
    public static void clearRecipes() {
        CampfireRecipes.INSTANCE.clear();
    }
}