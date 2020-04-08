package thedarkcolour.futuremc.recipe

import net.minecraft.item.ItemStack
import java.util.*

abstract class Recipes<T : Recipe> {
    abstract val recipes: ArrayList<T>

    abstract fun addDefaults()

    /**
     * Removes a recipe with the given input
     * Ignores if the recipe doesn't exist.
     */
    open fun removeRecipe(input: ItemStack) {
        recipes.removeIf { it.matches(input) }
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
     * Removes default recipes.
     */
    open fun clear() {
        recipes.clear()
    }

    /**
     * Used to check for any missing items in recipes before adding to JEI.
     */
    open fun validate() {

    }
}