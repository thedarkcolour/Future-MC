package thedarkcolour.futuremc.recipe.furnace

import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraftforge.oredict.OreDictionary
import thedarkcolour.futuremc.recipe.Recipes
import java.util.*

object BlastFurnaceRecipes : Recipes<FurnaceRecipe>() {
    override val recipes = ArrayList<FurnaceRecipe>()

    override fun addDefaults() {
        for (string in OreDictionary.getOreNames()) {
            if (string.startsWith("ore") || string.startsWith("dust")) {
                val ores = OreDictionary.getOres(string)

                ores.forEach { stack ->
                    val result = FurnaceRecipes.instance().getSmeltingResult(stack)
                    if (!result.isEmpty) {
                        addRecipe(stack, result)
                    }
                }
            }
        }
    }

    fun addRecipe(input: ItemStack, output: ItemStack) {
        println("Added a recipe: $input, $output")
        recipes.add(FurnaceRecipe(input, output))
    }
}