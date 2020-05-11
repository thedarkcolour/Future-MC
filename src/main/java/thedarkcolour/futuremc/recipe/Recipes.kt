package thedarkcolour.futuremc.recipe

import net.minecraft.item.ItemStack
import java.util.*

abstract class Recipes<T : Recipe<T>> {
    abstract val recipes: ArrayList<T>

    /**
     * Removes a recipe with the given input
     * Ignores if the recipe doesn't exist.
     */
    open fun removeRecipe(input: ItemStack) {
        val iterator = recipes.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().matches(input))
                iterator.remove()
        }
    }

    /**
     * Gets a recipe for the given input.
     */
    open fun getRecipe(input: ItemStack): T? {
        for (recipe in recipes) {
            if (recipe.matches(input)) {
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
        return recipes.filter { it.matches(input) }
    }

    /**
     * Removes default recipes.
     */
    open fun clear() {
        recipes.clear()
    }

    /**
     * Removes invalid recipes with missing item
     */
    abstract fun validate()
}