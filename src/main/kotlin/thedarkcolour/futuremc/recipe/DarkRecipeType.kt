package thedarkcolour.futuremc.recipe

import net.minecraft.item.crafting.IRecipeType
import net.minecraft.util.ResourceLocation
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import thedarkcolour.futuremc.util.DarkInventory

interface DarkRecipeType<T : DarkRecipe> : IRecipeType<T> {
    fun matches(recipe: DarkRecipe, worldIn: World, inv: DarkInventory): T? {
        return if (recipe.matches(inv, worldIn)) recipe as T else null
    }

    companion object {
        fun <T : DarkRecipe> register(key: String): DarkRecipeType<T> {
            return Registry.register(Registry.RECIPE_TYPE, ResourceLocation(key), object : DarkRecipeType<T> {
                override fun toString(): String {
                    return key
                }
            })
        }
    }
}