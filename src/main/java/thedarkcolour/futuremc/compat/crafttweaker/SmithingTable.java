package thedarkcolour.futuremc.compat.crafttweaker;

import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thedarkcolour.futuremc.recipe.smithing.SmithingRecipe;
import thedarkcolour.futuremc.recipe.smithing.SmithingRecipes;

import static thedarkcolour.futuremc.compat.crafttweaker.RecipeUtil.*;

@ZenRegister
@ZenClass("mods.futuremc.SmithingTable")
public final class SmithingTable {
    /**
     * Adds a recipe.
     *
     * @param input the type of item (ignores tool durability)
     * @param material the material and how much of it
     * @param result the result item with NBT from input (id)
     */
    @ZenMethod
    public static void addRecipe(IIngredient input, IIngredient material, IItemStack result) {
        applyAction(new AddAction(input, material, result));
    }

    private static final class AddAction implements IAction {
        private final Ingredient input;
        private final Ingredient material;
        private final ItemStack result;

        private AddAction(IIngredient input, IIngredient material, IItemStack result) {
            this.input = toIngredient(input);
            this.material = toIngredient(material);
            this.result = toItemStack(result);
        }

        @Override
        public void apply() {
            SmithingRecipes.INSTANCE.getRecipes().add(new SmithingRecipe(input, material, result));
        }

        @Override
        public String describe() {
            return "Added a recipe for the smithing table - Input: " + input + ", Material: " + ", Output: " + result;
        }
    }

    /**
     * Removes a recipe if it exists.
     *
     * @param input the type of item, id and meta (ignores tool durability)
     * @param material the material and how much of it (id, metadata, quantity)
     */
    @ZenMethod
    public static void removeRecipe(IItemStack input, IItemStack material) {
        applyAction(new RemoveAction(input, material));
    }

    private static final class RemoveAction implements IAction {
        private Runnable remove;

        private RemoveAction(IItemStack input, IItemStack material) {
            this.remove = () -> {
                SmithingRecipes.INSTANCE.removeRecipe(toItemStack(input), toItemStack(material));
            };
        }

        @Override
        public void apply() {
            remove.run();
            remove = null;
        }

        @Override
        public String describe() {
            return "Removed a recipe from the smithing table";
        }
    }

    @ZenMethod
    public static void clearDefaults() {
        SmithingRecipes.INSTANCE.getRecipes().clear();
    }
}
