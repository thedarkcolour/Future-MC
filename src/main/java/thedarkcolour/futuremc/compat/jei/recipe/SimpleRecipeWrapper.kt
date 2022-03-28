package thedarkcolour.futuremc.compat.jei.recipe

import mezz.jei.api.IJeiHelpers
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.ingredients.VanillaTypes
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import thedarkcolour.futuremc.recipe.SimpleRecipe

class SimpleRecipeWrapper(helpers: IJeiHelpers, input: Ingredient, val output: ItemStack) : IRecipeWrapper {
    private val input = listOf(helpers.stackHelper.toItemStackList(input))

    constructor(helpers: IJeiHelpers, recipe: SimpleRecipe) : this(helpers, recipe.input, recipe.output)

    override fun getIngredients(ingredients: IIngredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, input)
        ingredients.setOutput(VanillaTypes.ITEM, output)
    }
}