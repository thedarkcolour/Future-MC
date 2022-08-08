package thedarkcolour.futuremc.recipe.furnace

import net.minecraft.item.ItemFood
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraft.item.crafting.Ingredient
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.recipe.Recipes
import thedarkcolour.futuremc.recipe.SimpleRecipe

object SmokerRecipes : Recipes<SimpleRecipe>() {
    override val recipes = ArrayList<SimpleRecipe>()

    init {
        FutureMC.LOGGER.debug("Initializing default Smoker recipes")
        val start = System.currentTimeMillis()

        for ((key, value) in FurnaceRecipes.instance().smeltingList) {
            if (key.item is ItemFood || value.item is ItemFood) {
                recipes.add(SimpleRecipe(key, value))
            }
        }

        FutureMC.LOGGER.debug("Completed adding default Smoker recipes in {}ms", System.currentTimeMillis() - start)
    }

    override fun addRecipe(input: Ingredient, output: ItemStack) {
        recipes.add(SimpleRecipe(input, output))
    }
}