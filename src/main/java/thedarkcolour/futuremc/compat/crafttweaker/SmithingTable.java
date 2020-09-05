package thedarkcolour.futuremc.compat.crafttweaker;

import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thedarkcolour.futuremc.recipe.smithing.SmithingRecipe;
import thedarkcolour.futuremc.recipe.smithing.SmithingRecipes;

import static thedarkcolour.futuremc.compat.crafttweaker.CraftTweakerUtilKt.applyAction;
import static thedarkcolour.futuremc.compat.crafttweaker.CraftTweakerUtilKt.toItemStack;

@ZenRegister
@ZenClass("mods.futuremc.SmithingTable")
public final class SmithingTable {
    /**
     * Adds a recipe.
     *
     * @param input the type of item, id and meta (ignores tool durability)
     * @param material the material and how much of it (id, metadata, quantity)
     * @param result the result item with NBT from input (id)
     */
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack material, IItemStack result) {
        applyAction(new AddAction(input, material, result));
    }

    /**
     * Overload that uses ores instead of item stacks
     */
    @ZenMethod
    public static void addRecipe(IOreDictEntry input, IOreDictEntry material, IItemStack result) {
        for (IItemStack a : input.getItems()) {
            for (IItemStack b : material.getItems()) {
                applyAction(new AddAction(a, b, result));
            }
        }
    }

    private static final class AddAction implements IAction {
        private final ItemStack input;
        private final ItemStack material;
        private final ItemStack result;

        private AddAction(IItemStack input, IItemStack material, IItemStack result) {
            this.input = toItemStack(input);
            this.material = toItemStack(material);
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
