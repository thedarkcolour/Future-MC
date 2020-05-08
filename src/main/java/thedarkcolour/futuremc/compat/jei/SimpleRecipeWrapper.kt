package thedarkcolour.futuremc.compat.jei

import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.ingredients.VanillaTypes
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.item.ItemStack
import thedarkcolour.futuremc.recipe.SimpleRecipe

class SimpleRecipeWrapper(val input: ItemStack, val output: ItemStack) : IRecipeWrapper {
    constructor(recipe: SimpleRecipe) : this(recipe.input, recipe.output)

    override fun getIngredients(ingredients: IIngredients) {
        ingredients.setInput(VanillaTypes.ITEM, input)
        ingredients.setOutput(VanillaTypes.ITEM, output)
    }
}