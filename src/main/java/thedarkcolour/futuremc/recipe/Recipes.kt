package thedarkcolour.futuremc.recipe

import net.minecraft.item.ItemStack
import java.util.*

abstract class Recipes<T : Recipe> {
    abstract val recipes: ArrayList<T>

    abstract fun addDefaults()

    open fun removeRecipe(input: ItemStack) {
        recipes.removeIf { it.matches(input) }
    }

    open fun getRecipe(input: ItemStack): Optional<T> {
        for (recipe in recipes) {
            if (recipe.matches(input)) {
                return Optional.of(recipe)
            }
        }
        return Optional.empty()
    }

    open fun clear() {
        recipes.clear()
    }

    /**
     * Used to check for any missing items in recipes before adding to JEI.
     */
    open fun verify() {

    }
}