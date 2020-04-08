package thedarkcolour.core.inventory

import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.SlotItemHandler

class SlotDarkInventory(itemHandler: IItemHandler, index: Int, xPosition: Int, yPosition: Int) :
    SlotItemHandler(itemHandler, index, xPosition, yPosition) {
}