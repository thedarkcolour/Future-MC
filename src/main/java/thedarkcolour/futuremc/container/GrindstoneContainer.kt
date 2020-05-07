package thedarkcolour.futuremc.container

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.core.gui.FContainer
import thedarkcolour.core.inventory.DarkInventory

class GrindstoneContainer(private val playerInv: InventoryPlayer, private val worldIn: World, private val pos: BlockPos) : FContainer() {
    private val inventory = object : DarkInventory(3) {
        override fun onContentsChanged(slot: Int) {
            if (slot != 2) {
                updateResult()
                detectAndSendChanges()
            }
        }

        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return slot != 2
        }
    }

    private fun updateResult() {

    }

    override fun getGuiContainer(): GuiContainer {
        TODO("not implemented")
    }

    override fun canInteractWith(playerIn: EntityPlayer): Boolean {
        TODO("not implemented")
    }

}