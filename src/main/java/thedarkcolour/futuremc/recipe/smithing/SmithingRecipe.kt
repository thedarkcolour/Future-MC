package thedarkcolour.futuremc.recipe.smithing

import net.minecraft.item.ItemStack
import thedarkcolour.futuremc.recipe.Recipe

class SmithingRecipe(override val input: ItemStack, val material: ItemStack, override val output: ItemStack) : Recipe<SmithingRecipe>() {
    /**
     * Checks if the inventory has enough material to upgrade
     */
    fun matchesMaterial(input: ItemStack, material: ItemStack): Boolean {
        return doesInputMatchIgnoreDurability(this.input, input) && doesInputMatch(this.material, material)
    }
}