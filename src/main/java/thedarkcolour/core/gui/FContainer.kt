package thedarkcolour.core.gui

import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.Slot
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class FContainer(@JvmField val playerInv: InventoryPlayer) : Container() {
    abstract fun createGui(): Any

    fun isBlockInRange(block: Block, worldIn: World, pos: BlockPos, playerIn: EntityPlayer): Boolean {
        return if (worldIn.getBlockState(pos).block != block) {
            false
        } else {
            playerIn.getDistanceSq(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5) <= 64.0
        }
    }

    // Add these at the end after custom slots have been added
    protected fun addPlayerSlots(playerInv: InventoryPlayer) {
        for (row in 0..2) {
            for (col in 0..8) {
                val x = col * 18 + 8
                val y = row * 18 + 84
                addSlotToContainer(Slot(playerInv, col + row * 9 + 9, x, y))
            }
        }

        for (row in 0..8) {
            val x = row * 18 + 8
            addSlotToContainer(Slot(playerInv, row, x, 142))
        }
    }

    fun isTileInRange(te: TileEntity, playerIn: EntityPlayer): Boolean {
        return playerIn.getDistanceSq(te.pos.add(0.5, 0.5, 0.5)) <= 64.0
    }
}