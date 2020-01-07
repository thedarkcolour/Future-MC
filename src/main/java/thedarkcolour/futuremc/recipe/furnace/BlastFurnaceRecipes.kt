package thedarkcolour.futuremc.recipe.furnace

import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import thedarkcolour.core.util.getOreName
import thedarkcolour.futuremc.recipe.Recipes
import java.util.*

object BlastFurnaceRecipes : Recipes<FurnaceRecipe>() {
    override val recipes = ArrayList<FurnaceRecipe>()

    override fun addDefaults() {
        for ((key, value) in FurnaceRecipes.instance().smeltingList) {
            if (getOreName(key).startsWith("ore") || getOreName(key).startsWith("dust")) {
                addRecipe(key, value)
            }
        }
    }

    fun addRecipe(input: ItemStack, output: ItemStack) {
        recipes.add(FurnaceRecipe(input, output))
    }
}
