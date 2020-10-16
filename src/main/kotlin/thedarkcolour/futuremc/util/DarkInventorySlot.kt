package thedarkcolour.futuremc.util

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraftforge.items.SlotItemHandler

open class DarkInventorySlot(darkInventory: DarkInventory, index: Int, xPosition: Int, yPosition: Int) : SlotItemHandler(darkInventory, index, xPosition, yPosition) {
    override fun canTakeStack(playerIn: PlayerEntity): Boolean {
        return (itemHandler as DarkInventory).canTakeStack(slotIndex, playerIn)
    }

    override fun onTake(playerIn: PlayerEntity, stack: ItemStack): ItemStack {
        super.onTake(playerIn, stack)
        return (itemHandler as DarkInventory).onTake(slotIndex, playerIn, stack)
    }
}