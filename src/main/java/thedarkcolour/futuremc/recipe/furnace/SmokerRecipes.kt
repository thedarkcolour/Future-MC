package thedarkcolour.futuremc.recipe.furnace

import net.minecraft.item.ItemFood
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraftforge.fml.common.registry.ForgeRegistries
import thedarkcolour.futuremc.recipe.Recipes
import thedarkcolour.futuremc.recipe.SimpleRecipe
import thedarkcolour.futuremc.recipe.stonecutter.StonecutterRecipes
import java.util.*

object SmokerRecipes : Recipes<SimpleRecipe>() {
    override val recipes = ArrayList<SimpleRecipe>().also { recipes ->
        for ((key, value) in FurnaceRecipes.instance().smeltingList) {
            if (key.item is ItemFood || value.item is ItemFood) {
                recipes.add(SimpleRecipe(key, value))
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