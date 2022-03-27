package thedarkcolour.futuremc.recipe.smithing

import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import thedarkcolour.futuremc.recipe.Recipe

class SmithingRecipe(override val input: Ingredient, val material: Ingredient, override val output: ItemStack) : Recipe<SmithingRecipe>() {
    /**
     * Checks if the inventory has enough material to upgrade
     */
    fun matchesMaterial(input: ItemStack, material: ItemStack): Boolean {
        return this.input.test(input) && this.material.test(material)
    }
}