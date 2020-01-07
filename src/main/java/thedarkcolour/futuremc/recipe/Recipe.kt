package thedarkcolour.futuremc.recipe

import net.minecraft.item.ItemStack

abstract class Recipe {
    abstract val input: ItemStack

    fun matches(input: ItemStack): Boolean {
        return input.isItemEqual(this.input)
    }
}