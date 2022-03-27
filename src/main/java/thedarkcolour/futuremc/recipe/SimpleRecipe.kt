package thedarkcolour.futuremc.recipe

import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient

/**
 * Simple recipe with one input and one output.
 *
 * @param input the input item requirement of this recipe
 * @param output the result of this recipe
 *
 * @author TheDarkColour
 */
open class SimpleRecipe(override val input: Ingredient, override val output: ItemStack) : Recipe<SimpleRecipe>()