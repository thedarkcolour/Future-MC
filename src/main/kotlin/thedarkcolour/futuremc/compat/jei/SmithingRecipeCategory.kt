package thedarkcolour.futuremc.compat.jei

import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.config.Constants
import net.minecraft.client.resources.I18n
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.ResourceLocation
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.recipe.SmithingRecipe

class SmithingRecipeCategory(helper: IGuiHelper) : IRecipeCategory<SmithingRecipe> {
    private val background: IDrawable
    private val icon: IDrawable

    init {
        background = helper.drawableBuilder(Constants.RECIPE_GUI_VANILLA, 0, 168, 125, 18).build()
        icon = helper.createDrawableIngredient(ItemStack(Items.SMITHING_TABLE))
    }

    override fun getUid(): ResourceLocation {
        return NAME
    }

    override fun getTitle(): String {
        return I18n.format(TITLE)
    }

    override fun getBackground(): IDrawable {
        return background
    }

    override fun setRecipe(recipeLayout: IRecipeLayout, recipe: SmithingRecipe, ingredients: IIngredients) {
        val stacks = recipeLayout.itemStacks

        stacks.init(0, true, 0, 0)
        stacks.init(1, true, 49, 0)
        stacks.init(2, false, 107, 0)
        stacks.set(ingredients)
    }

    override fun setIngredients(recipe: SmithingRecipe, ingredients: IIngredients) {
        ingredients.setInputIngredients(listOf(recipe.ingredient, recipe.material))
        ingredients.setOutput(VanillaTypes.ITEM, recipe.result)
    }

    override fun getRecipeClass(): Class<out SmithingRecipe> {
        return SmithingRecipe::class.java
    }

    override fun getIcon(): IDrawable {
        return icon
    }

    companion object {
        const val TITLE = "container.jei.futuremc.smithing.name"
        val NAME = ResourceLocation(FutureMC.ID, "smithing")
    }
}
