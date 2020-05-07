package thedarkcolour.core.inventory

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.items.SlotItemHandler

class DarkInventorySlot(private val inv: DarkInventory, i: Int, x: Int, y: Int) : SlotItemHandler(inv, i, x, y) {
    override fun canTakeStack(playerIn: EntityPlayer): Boolean {
        return inv.canTakeStack(playerIn, slotIndex)
    }

    override fun onTake(playerIn: EntityPlayer, stack: ItemStack): ItemStack {
        onSlotChanged()
        return inv.onTake(playerIn, stack, slotIndex)
    }
}