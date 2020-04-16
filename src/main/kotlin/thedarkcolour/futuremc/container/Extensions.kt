package thedarkcolour.futuremc.container

import net.minecraft.inventory.IInventory
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.IRecipeType
import net.minecraft.item.crafting.RecipeManager
import net.minecraft.world.World

fun <C : IInventory, T : IRecipe<C>>RecipeManager.getRecipeNullable(type: IRecipeType<T>, inventory: C, worldIn: World): T? {
    return getRecipes(type).values.filter { type.matches(it, worldIn, inventory).isPresent }.map { it as T? }.firstOrNull()
}