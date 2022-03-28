package thedarkcolour.futuremc.recipe

import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraftforge.fml.common.registry.ForgeRegistries

abstract class Recipes<out T : SimpleRecipe> {
    /** List of all recipes for this class. */
    abstract val recipes: ArrayList<out T>

    abstract fun addRecipe(input: Ingredient, output: ItemStack)

    /**
     * Removes a recipe with the given input
     * Ignores if the recipe doesn't exist.
     */
    open fun removeRecipeForInput(input: ItemStack) {
        recipes.removeIf { recipe -> recipe.input.test(input)}
    }

    open fun removeRecipeForOutput(output: ItemStack) {
        recipes.removeIf { recipe -> recipe.output.isItemEqual(output)}
    }

    /**
     * Gets a recipe for the given input.
     */
    open fun getRecipe(input: ItemStack): T? {
        return recipes.firstOrNull { recipe -> recipe.input.test(input) }
    }

    /**
     * Gets all available recipes for the input and puts them in a list.
     *
     * If no recipes match the input, then return an empty list.
     */
    open fun getRecipes(input: ItemStack): List<T> {
        return recipes.filter { recipe -> recipe.input.test(input) }
    }

    /**
     * Removes all default recipes.
     */
    open fun clear() {
        recipes.clear()
    }

    /**
     * Removes empty recipes after all recipes have been registered.
     */
    fun validate() {
        val registry = ForgeRegistries.ITEMS

        recipes.removeIf { recipe ->
            val invalid = !registry.containsValue(recipe.output.item)

            if (invalid) {
                println("Removed invalid recipe: $recipe")
            }

            invalid
        }
    }
}