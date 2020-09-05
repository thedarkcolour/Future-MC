package thedarkcolour.futuremc.compat.jei.composter

import mezz.jei.api.IGuiHelper
import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeCategory
import net.minecraft.client.resources.I18n
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.compat.jei.FutureMCJEIPlugin

class ComposterRecipeCategory(helper: IGuiHelper) : IRecipeCategory<ComposterRecipeWrapper> {
    private val background: IDrawable

    init {
        background = helper.drawableBuilder(FutureMCJEIPlugin.RECIPE_BACKGROUNDS, 80, 0, 58, 44).build()
    }

    /**
     * Returns a unique ID for this recipe category.
     * Referenced from recipes to identify which recipe category they belong to.
     *
     * @see VanillaRecipeCategoryUid for vanilla examples
     */
    override fun getUid(): String {
        return NAME
    }

    /**
     * Return the mod name or id associated with this recipe category.
     * Used for the recipe category tab's tooltip.
     *
     * @since JEI 4.5.0
     */
    override fun getModName(): String {
        return FutureMC.ID
    }

    /**
     * Set the [IRecipeLayout] properties from the [IRecipeWrapper] and [IIngredients].
     *
     * @param recipeLayout  the layout that needs its properties set.
     * @param recipeWrapper the recipeWrapper, for extra information.
     * @param ingredients   the ingredients, already set by the recipeWrapper
     * @since JEI 3.11.0
     */
    override fun setRecipe(
        recipeLayout: IRecipeLayout,
        recipeWrapper: ComposterRecipeWrapper,
        ingredients: IIngredients
    ) {
        recipeLayout.itemStacks.init(0, true, 20, 13)
        recipeLayout.itemStacks.set(0, recipeWrapper.input)
    }

    /**
     * Returns the drawable background for a single recipe in this category.
     *
     * The size of the background determines how recipes are laid out by JEI,
     * make sure it is the right size to contains everything being displayed.
     */
    override fun getBackground(): IDrawable {
        return background
    }

    /**
     * Returns the localized name for this recipe type.
     * Drawn at the top of the recipe GUI pages for this category.
     */
    override fun getTitle(): String {
        return I18n.format(NAME)
    }

    companion object {
        const val NAME = "container.jei.futuremc.composter.name"
    }
}