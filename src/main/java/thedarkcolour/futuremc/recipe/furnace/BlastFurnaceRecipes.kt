package thedarkcolour.futuremc.recipe.furnace

import net.minecraft.item.ItemStack
import thedarkcolour.futuremc.recipe.Recipes
import thedarkcolour.futuremc.recipe.SimpleRecipe
import java.util.*

object BlastFurnaceRecipes : Recipes<SimpleRecipe>() {
    override val recipes = ArrayList<SimpleRecipe>()

    fun addRecipe(input: ItemStack, output: ItemStack) {
        recipes.add(SimpleRecipe(input, output))
    }
}