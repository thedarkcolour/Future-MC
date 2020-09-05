package thedarkcolour.futuremc.compat.jei.smithing

import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.ingredients.VanillaTypes
import mezz.jei.api.recipe.IRecipeWrapper
import thedarkcolour.futuremc.recipe.smithing.SmithingRecipe

class SmithingRecipeWrapper(recipe: SmithingRecipe) : IRecipeWrapper {
    val inputs = listOf(listOf(recipe.input), listOf(recipe.material))
    val output = recipe.output

    /**
     * Gets all the recipe's ingredients by filling out an instance of [IIngredients].
     *
     * @since JEI 3.11.0
     */
    override fun getIngredients(ingredients: IIngredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, inputs)
        ingredients.setOutput(VanillaTypes.ITEM, output)
    }
}