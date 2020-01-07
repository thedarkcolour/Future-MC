package thedarkcolour.futuremc.recipe.furnace

import net.minecraft.item.ItemStack
import thedarkcolour.futuremc.recipe.Recipe

open class FurnaceRecipe(override val input: ItemStack, val output: ItemStack) : Recipe()