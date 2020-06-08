package thedarkcolour.futuremc.recipe.campfire

import net.minecraft.item.ItemStack
import thedarkcolour.futuremc.recipe.Recipe

/**
 * Recipe class used for the campfire.
 *
 * @property input the input item requirement for this recipe
 * @property output the result for this recipe
 * @property duration the number of ticks this recipe takes to complete
 *
 * @author TheDarkColour
 */
class CampfireRecipe(override val input: ItemStack, override val output: ItemStack, val duration: Int) : Recipe<CampfireRecipe>() {
    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String {
        return "CampfireRecipe(input = $input, output = $output, duration = $duration)"
    }
}