package thedarkcolour.futuremc.recipe

import net.minecraft.inventory.IInventory
import net.minecraft.item.crafting.IRecipe
import net.minecraft.world.World
import thedarkcolour.futuremc.util.DarkInventory

interface DarkRecipe : IRecipe<IInventory> {
    override fun canFit(width: Int, height: Int): Boolean {
        return true
    }

    override fun getType(): DarkRecipeType<*>

    fun matches(inv: DarkInventory, worldIn: World): Boolean
}