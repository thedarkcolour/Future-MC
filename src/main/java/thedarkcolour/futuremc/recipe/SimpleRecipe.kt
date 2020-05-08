package thedarkcolour.futuremc.recipe

import net.minecraft.item.ItemStack

/**
 * Simple recipe with one input and one output.
 */
class SimpleRecipe(override val input: ItemStack, val output: ItemStack) : Recipe() {
    override fun matches(input: ItemStack): Boolean {
        return compareItems(this.input, input)
    }

    fun matches(input: ItemStack, output: ItemStack): Boolean {
        return compareItems(this.input, input) && compareItems(this.output, output)
    }

    private fun compareItems(expected: ItemStack, givenInput: ItemStack): Boolean {
        return givenInput.item == expected.item && (expected.metadata == 32767 || givenInput.metadata == expected.metadata)
    }
}