package thedarkcolour.futuremc.compat.jei.category

import mezz.jei.api.IGuiHelper
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.ingredients.VanillaTypes
import mezz.jei.config.Constants
import thedarkcolour.futuremc.compat.jei.recipe.SmithingRecipeWrapper

class SmithingRecipeCategory(helper: IGuiHelper) : FRecipeCategory<SmithingRecipeWrapper>(helper, Constants.RECIPE_GUI_VANILLA, NAME, 0, 168, 125, 18) {
    override fun setRecipe(
        recipeLayout: IRecipeLayout,
        recipeWrapper: SmithingRecipeWrapper,
        ingredients: IIngredients
    ) {
        val stacks = recipeLayout.itemStacks

        stacks.init(0, true, 0, 0)
        stacks.init(1, true, 49, 0)
        stacks.init(2, false, 107, 0)

        val inputs = ingredients.getInputs(VanillaTypes.ITEM)

        stacks.set(0, inputs[0])
        stacks.set(1, inputs[1])
        stacks.set(2, ingredients.getOutputs(VanillaTypes.ITEM)[0])
    }

    companion object {
        const val NAME = "container.jei.futuremc.smithing.name"
    }
}