package thedarkcolour.futuremc.recipe

import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.ForgeRegistries

abstract class Recipes<out T : Recipe<T>> {
    /** List of all recipes for this class. */
    abstract val recipes: ArrayList<out T>

    abstract fun addRecipe(input: ItemStack, output: ItemStack)

    /**
     * Removes a recipe with the given input
     * Ignores if the recipe doesn't exist.
     */
    open fun removeRecipeForInput(input: ItemStack) {
        val iterator = recipes.iterator()

        while (iterator.hasNext()) {
            if (iterator.next().matchesInput(input)) {
                iterator.remove()
            }
        }
    }

    open fun removeRecipeForOutput(output: ItemStack) {
        val iterator = recipes.iterator()

        while (iterator.hasNext()) {
            if (iterator.next().matchesOutput(output)) {
                iterator.remove()
            }
        }
    }

    /**
     * Gets a recipe for the given input.
     */
    open fun getRecipe(input: ItemStack): T? {
        for (recipe in recipes) {
            if (recipe.matchesInput(input)) {
                return recipe
            }
        }
        return null
    }

    /**
     * Gets all available recipes for the input and puts them in a list.
     *
     * If no recipes match the input, then return an empty list.
     */
    open fun getRecipes(input: ItemStack): List<T> {
        return recipes.filter { it.matchesInput(input) }
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
            val a = !registry.containsValue(recipe.output.item) || !registry.containsValue(recipe.input.item)
            if (a) println(recipe)
            a
        }
    }
}