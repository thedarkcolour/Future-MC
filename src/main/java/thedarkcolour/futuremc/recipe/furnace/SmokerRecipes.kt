package thedarkcolour.futuremc.recipe.furnace

import net.minecraft.item.ItemFood
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import thedarkcolour.futuremc.recipe.Recipes
import java.util.*

object SmokerRecipes : Recipes<FurnaceRecipe>() {
    override val recipes = ArrayList<FurnaceRecipe>().also { recipes ->
        for ((key, value) in FurnaceRecipes.instance().smeltingList) {
            if (key.item is ItemFood || value.item is ItemFood) {
                recipes.add(FurnaceRecipe(key, value))
            }
        }
    }

    fun addRecipe(input: ItemStack, output: ItemStack) {
        recipes.add(FurnaceRecipe(input, output))
    }
}