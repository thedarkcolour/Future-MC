package thedarkcolour.futuremc.recipe

import net.minecraft.item.ItemStack

// todo add support for Ingredient
abstract class Recipe<T : Recipe<T>> {
    abstract val input: ItemStack

    open fun matches(input: ItemStack): Boolean {
        return input.isItemEqual(this.input)
    }
}