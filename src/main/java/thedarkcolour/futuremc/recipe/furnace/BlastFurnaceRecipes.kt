package thedarkcolour.futuremc.recipe.furnace

import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraft.item.crafting.Ingredient
import net.minecraftforge.oredict.OreDictionary
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.recipe.Recipes
import thedarkcolour.futuremc.recipe.SimpleRecipe

object BlastFurnaceRecipes : Recipes<SimpleRecipe>() {
    override val recipes = ArrayList<SimpleRecipe>()

    init {
        FutureMC.LOGGER.debug("Initializing default Blast Furnace recipes")
        val start = System.currentTimeMillis()

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

        FutureMC.LOGGER.debug("Completed adding default Blast Furnace recipes in {}ms", System.currentTimeMillis() - start)
    }

    override fun addRecipe(input: Ingredient, output: ItemStack) {
        recipes.add(SimpleRecipe(input, output))
    }
}