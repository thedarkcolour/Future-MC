package thedarkcolour.core.gui

import net.minecraft.block.Block
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class ContainerBase : Container() {
    abstract fun getGuiContainer(): GuiContainer

    fun isBlockInRange(block: Block, worldIn: World, pos: BlockPos, playerIn: EntityPlayer): Boolean {
        return if (worldIn.getBlockState(pos).block != block) {
            false
        } else {
            playerIn.getDistanceSq(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5) <= 64.0
        }
    }

    fun isTileInRange(te: TileEntity, playerIn: EntityPlayer): Boolean {
        return playerIn.getDistanceSq(te.pos.add(0.5, 0.5, 0.5)) <= 64.0
    }
}