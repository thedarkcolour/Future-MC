package thedarkcolour.futuremc.compat.jei.category

import mezz.jei.api.IGuiHelper
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.ingredients.VanillaTypes
import thedarkcolour.futuremc.compat.jei.FutureMCJEIPlugin
import thedarkcolour.futuremc.compat.jei.recipe.ComposterRecipeWrapper

class ComposterRecipeCategory(helper: IGuiHelper) :
    FRecipeCategory<ComposterRecipeWrapper>(helper, FutureMCJEIPlugin.RECIPE_BACKGROUNDS, NAME, 80, 0, 58, 44) {

    override fun setRecipe(
        recipeLayout: IRecipeLayout,
        recipeWrapper: ComposterRecipeWrapper,
        ingredients: IIngredients
    ) {
        recipeLayout.itemStacks.init(0, true, 20, 13)
        recipeLayout.itemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM)[0])
    }

    companion object {
        const val NAME = "container.jei.futuremc.composter.name"
    }
}