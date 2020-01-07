package thedarkcolour.futuremc.recipe.campfire

import net.minecraft.item.ItemStack
import thedarkcolour.futuremc.recipe.furnace.FurnaceRecipe

class CampfireRecipe(input: ItemStack, output: ItemStack, val duration: Int) : FurnaceRecipe(input, output)