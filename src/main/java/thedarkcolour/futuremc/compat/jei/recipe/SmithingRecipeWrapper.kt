package thedarkcolour.futuremc.compat.jei.recipe

import mezz.jei.api.IJeiHelpers
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.ingredients.VanillaTypes
import mezz.jei.api.recipe.IRecipeWrapper
import thedarkcolour.futuremc.recipe.smithing.SmithingRecipe

class SmithingRecipeWrapper(helpers: IJeiHelpers, recipe: SmithingRecipe) : IRecipeWrapper {
    private val inputs = listOf(helpers.stackHelper.toItemStackList(recipe.input), helpers.stackHelper.toItemStackList(recipe.material))
    val output = recipe.output

    override fun getIngredients(ingredients: IIngredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, inputs)
        ingredients.setOutput(VanillaTypes.ITEM, output)
    }
}