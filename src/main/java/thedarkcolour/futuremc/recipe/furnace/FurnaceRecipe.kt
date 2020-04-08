package thedarkcolour.futuremc.recipe.furnace

import net.minecraft.item.ItemStack
import thedarkcolour.futuremc.recipe.Recipe

open class FurnaceRecipe(override val input: ItemStack, val output: ItemStack) : Recipe() {
    override fun matches(input: ItemStack): Boolean {
        return compareItems(this.input, input)
    }

    private fun compareItems(input: ItemStack, output: ItemStack): Boolean {
        return output.item == input.item && (input.metadata == 32767 || output.metadata == input.metadata)
    }
}