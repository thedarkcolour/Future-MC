package thedarkcolour.futuremc.container

import net.minecraft.inventory.IInventory
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.IRecipeType
import net.minecraft.item.crafting.RecipeManager
import net.minecraft.world.World

fun <C : IInventory, T : IRecipe<C>>RecipeManager.getRecipeNullable(type: IRecipeType<T>, inventory: C, worldIn: World): T? {
    return getRecipes(type).values.map { it as T }.firstOrNull { type.matches(it, worldIn, inventory).isPresent }
}
/*
@Suppress("UNCHECKED_CAST")
fun <T : DarkRecipe>RecipeManager.getRecipeNullable(type: DarkRecipeType<T>, inventory: DarkInventory, worldIn: World): T? {
    return getRecipes(type).map { it as T }.firstOrNull { type.matches(it, worldIn, inventory) != null }
}
*/