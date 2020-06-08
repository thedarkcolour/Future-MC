package thedarkcolour.futuremc.recipe

import net.minecraft.item.ItemStack

/**
 * The base class for a recipe with an input and an output.
 *
 * @property input the input item requirement of this recipe
 * @property output the result of this recipe
 * @property T the type of [Recipe] this is
 */
abstract class Recipe<out T : Recipe<T>> {
    abstract val input: ItemStack
    abstract val output: ItemStack

    /**
     * Returns if the input matches the requirements of this recipe.
     */
    open fun matches(input: ItemStack): Boolean {
        return doesInputMatch(this.input, input)
    }

    /**
     * Returns if the input and output match the requirements of this recipe.
     *
     * @see thedarkcolour.futuremc.recipe.stonecutter.StonecutterRecipes
     */
    fun matches(input: ItemStack, output: ItemStack): Boolean {
        return doesInputMatch(this.input, input) && doesInputMatch(this.output, output)
    }

    /**
     * Determines if the other item matches the specified input.
     *
     * @param recipeInput the required input
     * @param other the item to test against the recipe input
     *
     * @return if [other] is the same item as [recipeInput] and has an equal or greater amount.
     */
    fun doesInputMatch(recipeInput: ItemStack, other: ItemStack): Boolean {
        return other.item == recipeInput.item && (recipeInput.metadata == 32767 || other.metadata == recipeInput.metadata) &&
                other.count >= recipeInput.count
    }

    /**
     * Tests equality between tools ignoring durability **if they are damageable.**
     *
     * If the item is not a tool, then normal equality checking is used.
     */
    fun doesInputMatchIgnoreDurability(recipeInput: ItemStack, other: ItemStack): Boolean {
        return (if (recipeInput.maxDamage == 0) {
            doesInputMatch(recipeInput, other)
        } else other.item == recipeInput.item)
    }

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String {
        return "CampfireRecipe(input = $input, output = $output)"
    }
}