package thedarkcolour.futuremc.recipe.smithing

import net.minecraft.item.ItemStack
import thedarkcolour.futuremc.recipe.Recipe

class SmithingRecipe(override val input: ItemStack, val material: ItemStack, val result: ItemStack) : Recipe<SmithingRecipe>() {
    /**
     * Checks if the inventory has enough material to upgrade
     */
    fun matches(input: ItemStack, material: ItemStack): Boolean {
        return super.matches(input) && material.count >= this.material.count
    }
}