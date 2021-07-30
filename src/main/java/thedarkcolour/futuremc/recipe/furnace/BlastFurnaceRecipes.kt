package thedarkcolour.futuremc.recipe.furnace

import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraftforge.oredict.OreDictionary
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.recipe.Recipes
import thedarkcolour.futuremc.recipe.SimpleRecipe

object BlastFurnaceRecipes : Recipes<SimpleRecipe>() {
    override val recipes = ArrayList<SimpleRecipe>()

    init {
        FutureMC.LOGGER.debug("Initializing default Blast Furnace recipes")

        for (string in OreDictionary.getOreNames()) {
            if (string.startsWith("ore") || string.startsWith("dust")) {
                val ores = OreDictionary.getOres(string)

                ores.forEach { stack ->
                    val result = FurnaceRecipes.instance().getSmeltingResult(stack)

                    if (!result.isEmpty) {
                        recipes.add(SimpleRecipe(stack, result))
                    }
                }
            }
        }
    }

    override fun addRecipe(input: ItemStack, output: ItemStack) {
        recipes.add(SimpleRecipe(input, output))
    }
}