package thedarkcolour.futuremc.compat.crafttweaker

import crafttweaker.CraftTweakerAPI
import crafttweaker.IAction
import crafttweaker.api.item.IItemStack
import crafttweaker.api.minecraft.CraftTweakerMC
import net.minecraft.item.ItemStack

fun IItemStack.toItemStack(): ItemStack {
    return CraftTweakerMC.getItemStack(this)
}

fun applyAction(action: IAction) {
    CraftTweakerAPI.apply(action)
}