package thedarkcolour.futuremc.recipe.smithing

import net.minecraftforge.fml.common.registry.ForgeRegistries
import thedarkcolour.futuremc.recipe.Recipes
import thedarkcolour.futuremc.recipe.stonecutter.StonecutterRecipes

object SmithingRecipes : Recipes<SmithingRecipe>() {
    override val recipes = arrayListOf<SmithingRecipe>()

    override fun validate() {
        val registry = ForgeRegistries.ITEMS

        StonecutterRecipes.recipes.removeIf { recipe ->
            !registry.containsValue(recipe.output.item) || !registry.containsValue(recipe.input.item)
        }
    }
}