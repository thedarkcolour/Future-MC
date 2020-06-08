package thedarkcolour.futuremc.compat.betterwithmods

import betterwithmods.common.registry.block.recipe.StateIngredient
import betterwithmods.common.registry.heat.BWMHeatRegistry
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Items
import net.minecraft.item.ItemStack

object BetterWithModsCompat {
    fun addHeatSource(heat: Int, states: List<IBlockState>) {
        BWMHeatRegistry.addHeatSource(StateIngredient(states, arrayListOf(ItemStack(Items.AIR))), heat)
    }
}