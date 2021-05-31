package thedarkcolour.futuremc.recipe.furnace

import net.minecraft.item.ItemStack
import thedarkcolour.futuremc.recipe.Recipes
import thedarkcolour.futuremc.recipe.SimpleRecipe

object BlastFurnaceRecipes : Recipes<SimpleRecipe>() {
    override val recipes = ArrayList<SimpleRecipe>()

    override fun addRecipe(input: ItemStack, output: ItemStack) {
        recipes.add(SimpleRecipe(input, output))
    }
}