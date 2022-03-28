package thedarkcolour.futuremc.recipe.campfire

import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import thedarkcolour.futuremc.recipe.SimpleRecipe

/**
 * Recipe class used for the campfire.
 *
 * @property input the input item requirement for this recipe
 * @property output the result for this recipe
 * @property duration the number of ticks this recipe takes to complete
 *
 * @author TheDarkColour
 */
class CampfireRecipe(input: Ingredient, output: ItemStack, val duration: Int) : SimpleRecipe(input, output) {
    constructor(stack: ItemStack, output: ItemStack, duration: Int) : this(Ingredient.fromStacks(stack), output, duration)

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String {
        return "CampfireRecipe(input = $input, output = $output, duration = $duration)"
    }
}