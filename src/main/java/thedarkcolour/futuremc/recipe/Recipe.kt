package thedarkcolour.futuremc.recipe

import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient

/**
 * The base class for a recipe with an input and an output.
 *
 * @property input the input item requirement of this recipe
 * @property output the result of this recipe
 * @property T the type of [Recipe] this is
 */
abstract class Recipe<out T : Recipe<T>> {
    abstract val input: Ingredient
    abstract val output: ItemStack

    /**
     * Returns if the input and output match the requirements of this recipe.
     *
     * @see thedarkcolour.futuremc.recipe.stonecutter.StonecutterRecipes
     */
    fun isSameRecipe(input: ItemStack, output: ItemStack): Boolean {
        return doesItemMatch(this.input, input) && doesItemMatch(this.output, output)
    }

    /**
     * Determines if the other item matches the specified item.
     *
     * @param item the item to check for matching
     * @param other the item to test against the recipe input
     *
     * @return if [other] is the same item as [item] and has an equal or greater amount.
     */
    fun doesItemMatch(item: ItemStack, other: ItemStack): Boolean {
        return other.item == item.item && (item.metadata == 32767 || other.metadata == item.metadata) && other.count >= item.count
    }

    /**
     * Tests equality between tools ignoring durability **if they are damageable.**
     *
     * If the item is not a tool, then normal equality checking is used.
     */
    fun doesItemMatchIgnoreDurability(recipeInput: ItemStack, other: ItemStack): Boolean {
        return if (recipeInput.maxDamage == 0) {
            doesItemMatch(recipeInput, other)
        } else {
            other.item == recipeInput.item
        }
    }

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String {
        return javaClass.simpleName + "(input = $input, output = $output)"
    }
}