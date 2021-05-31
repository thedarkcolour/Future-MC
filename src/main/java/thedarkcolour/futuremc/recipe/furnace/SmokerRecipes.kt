package thedarkcolour.futuremc.recipe.furnace

import net.minecraft.item.ItemFood
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import thedarkcolour.futuremc.recipe.Recipes
import thedarkcolour.futuremc.recipe.SimpleRecipe

object SmokerRecipes : Recipes<SimpleRecipe>() {
    override val recipes = ArrayList<SimpleRecipe>()

    init {
        for ((key, value) in FurnaceRecipes.instance().smeltingList) {
            if (key.item is ItemFood || value.item is ItemFood) {
                recipes.add(SimpleRecipe(key, value))
            }
        }
    }

    override fun addRecipe(input: ItemStack, output: ItemStack) {
        recipes.add(SimpleRecipe(input, output))
    }
}