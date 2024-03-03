package thedarkcolour.futuremc.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thedarkcolour.futuremc.recipe.smithing.SmithingRecipe;
import thedarkcolour.futuremc.recipe.smithing.SmithingRecipes;

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
        CraftTweakerAPI.apply(new AddAction(input, material, result));
    }

    private static final class AddAction implements IAction {
        private final Ingredient input;
        private final Ingredient material;
        private final ItemStack result;

        private AddAction(IIngredient input, IIngredient material, IItemStack result) {
            this.input = CraftTweakerMC.getIngredient(input);
            this.material = CraftTweakerMC.getIngredient(material);
            this.result = CraftTweakerMC.getItemStack(result);
        }

        @Override
        public void apply() {
            ItemStack[] stacks = input.getMatchingStacks();
            for (ItemStack stack : stacks) {
                if (stack.isItemStackDamageable() && stack.getMetadata() == 0) {
                    stack.setItemDamage(OreDictionary.WILDCARD_VALUE);
                }
            }
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
        CraftTweakerAPI.apply(new RemoveAction(input, material));
    }

    private static final class RemoveAction implements IAction {
        private final IItemStack input;
        private final IItemStack material;

        private RemoveAction(IItemStack input, IItemStack material) {
            this.input = input;
            this.material = material;
        }

        @Override
        public void apply() {
            SmithingRecipes.INSTANCE.removeRecipe(CraftTweakerMC.getItemStack(input), CraftTweakerMC.getItemStack(material));
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
