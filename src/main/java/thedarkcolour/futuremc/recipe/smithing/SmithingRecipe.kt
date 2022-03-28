package thedarkcolour.futuremc.recipe.smithing

import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import thedarkcolour.futuremc.recipe.SimpleRecipe

class SmithingRecipe(input: Ingredient, val material: Ingredient, output: ItemStack) : SimpleRecipe(input, output) {
    /**
     * Checks if the inventory has enough material to upgrade
     */
    fun matchesMaterial(input: ItemStack, material: ItemStack): Boolean {
        return this.input.test(input) && this.material.test(material)
    }
}