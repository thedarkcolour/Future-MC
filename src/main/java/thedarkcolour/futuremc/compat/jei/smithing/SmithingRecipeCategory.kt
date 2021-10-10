package thedarkcolour.futuremc.compat.jei.smithing

import mezz.jei.api.IGuiHelper
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.config.Constants
import thedarkcolour.futuremc.compat.jei.FRecipeCategory

class SmithingRecipeCategory(helper: IGuiHelper) : FRecipeCategory<SmithingRecipeWrapper>(helper, Constants.RECIPE_GUI_VANILLA, NAME, 0, 168, 125, 18) {
    override fun setRecipe(
        recipeLayout: IRecipeLayout,
        recipeWrapper: SmithingRecipeWrapper,
        ingredients: IIngredients
    ) {
        recipeLayout.itemStacks.init(0, true, 0, 0)
        recipeLayout.itemStacks.init(1, true, 49, 0)
        recipeLayout.itemStacks.init(2, false, 107, 0)

        recipeLayout.itemStacks.set(0, recipeWrapper.inputs[0][0])
        recipeLayout.itemStacks.set(1, recipeWrapper.inputs[1][0])
        recipeLayout.itemStacks.set(2, recipeWrapper.output)
    }

    companion object {
        const val NAME = "container.jei.futuremc.smithing.name"
    }
}