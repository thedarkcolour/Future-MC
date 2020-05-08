package thedarkcolour.futuremc.recipe.furnace

import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.oredict.OreDictionary
import thedarkcolour.futuremc.recipe.Recipes
import thedarkcolour.futuremc.recipe.SimpleRecipe
import thedarkcolour.futuremc.recipe.stonecutter.StonecutterRecipes
import java.util.*

object BlastFurnaceRecipes : Recipes<SimpleRecipe>() {
    override val recipes = ArrayList<SimpleRecipe>().also { recipes ->
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

    fun addRecipe(input: ItemStack, output: ItemStack) {
        recipes.add(SimpleRecipe(input, output))
    }

    override fun validate() {
        val registry = ForgeRegistries.ITEMS

        StonecutterRecipes.recipes.removeIf { recipe ->
            !registry.containsValue(recipe.output.item) || !registry.containsValue(recipe.input.item)
        }
    }
}