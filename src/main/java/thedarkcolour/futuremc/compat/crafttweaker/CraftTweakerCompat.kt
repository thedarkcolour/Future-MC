package thedarkcolour.futuremc.compat.crafttweaker

import crafttweaker.api.item.IItemStack
import net.minecraft.item.ItemStack
import thedarkcolour.core.util.cast

object CraftTweakerCompat {
    fun getItem(iStack: IItemStack): ItemStack {
        return cast<ItemStack>(iStack.internal).copy()
    }
}