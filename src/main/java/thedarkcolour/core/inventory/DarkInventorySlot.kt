package thedarkcolour.core.inventory

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.items.SlotItemHandler

/**
 * Delegates some logic to DarkInventory to keep everything organized.
 *
 * @property inv the [DarkInventory] that this slot delegates some logic to
 * @property alwaysTakeAll whether right click function should be same as default
 *
 * @author TheDarkColour
 */
open class DarkInventorySlot(private val inv: DarkInventory, i: Int, x: Int, y: Int) : SlotItemHandler(inv, i, x, y) {
    private var alwaysTakeAll = false

    override fun canTakeStack(playerIn: EntityPlayer): Boolean {
        return inv.canTakeStack(playerIn, slotIndex)
    }

    override fun onTake(playerIn: EntityPlayer, stack: ItemStack): ItemStack {
        onSlotChanged()
        return inv.onTake(playerIn, stack, slotIndex)
    }

    override fun decrStackSize(amount: Int): ItemStack {
        return if (alwaysTakeAll) {
            itemHandler.extractItem(slotIndex, 64, false)
        } else super.decrStackSize(amount)
    }

    /**
     * Specify whether "right click" behaviour should work normally
     * or if its behaviour is the same as "left click" behaviour.
     */
    fun alwaysTakeAll(alwaysTakeAll: Boolean): DarkInventorySlot {
        this.alwaysTakeAll = alwaysTakeAll
        return this
    }
}