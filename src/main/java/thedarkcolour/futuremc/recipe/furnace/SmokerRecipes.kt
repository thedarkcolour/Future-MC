package thedarkcolour.futuremc.recipe.furnace

import net.minecraft.item.ItemFood
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import thedarkcolour.futuremc.recipe.Recipes
import java.util.*

object SmokerRecipes : Recipes<FurnaceRecipe>() {
    override val recipes = ArrayList<FurnaceRecipe>()

    override fun addDefaults() {
        for ((key, value) in FurnaceRecipes.instance().smeltingList) {
            if (key.item is ItemFood || value.item is ItemFood) {
                addRecipe(key, value)
            }
        }
    }

    fun addRecipe(input: ItemStack, output: ItemStack) {
        println("Added a recipe: $input, $output")
        recipes.add(FurnaceRecipe(input, output))
    }
}