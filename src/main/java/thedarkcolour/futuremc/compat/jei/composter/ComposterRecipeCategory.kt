package thedarkcolour.futuremc.compat.jei.composter

import mezz.jei.api.IGuiHelper
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import thedarkcolour.futuremc.compat.jei.FRecipeCategory
import thedarkcolour.futuremc.compat.jei.FutureMCJEIPlugin

class ComposterRecipeCategory(helper: IGuiHelper) :
    FRecipeCategory<ComposterRecipeWrapper>(helper, FutureMCJEIPlugin.RECIPE_BACKGROUNDS, NAME, 80, 0, 58, 44) {

    override fun setRecipe(
        recipeLayout: IRecipeLayout,
        recipeWrapper: ComposterRecipeWrapper,
        ingredients: IIngredients
    ) {
        recipeLayout.itemStacks.init(0, true, 20, 13)
        recipeLayout.itemStacks.set(0, recipeWrapper.input)
    }

    companion object {
        const val NAME = "container.jei.futuremc.composter.name"
    }
}