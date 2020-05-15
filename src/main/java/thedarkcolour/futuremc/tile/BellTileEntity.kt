package thedarkcolour.futuremc.tile

import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import thedarkcolour.futuremc.registry.FBlocks

class BellTileEntity : TileEntity(), ITickable {
    var ringingTicks = 0
    var isRinging = false
    var ringFacing: EnumFacing? = null

    override fun update() {
        if (isRinging) {
            ++ringingTicks
        }

        if (ringingTicks >= 50) {
            isRinging = false
            ringingTicks = 0
        }
    }

    override fun receiveClientEvent(id: Int, param: Int): Boolean {
        return if (id == 1) {
            ringFacing = EnumFacing.byIndex(param)
            ringingTicks = 0
            isRinging = true
            true
        } else {
            super.receiveClientEvent(id, param)
        }
    }

    fun ring(facing: EnumFacing) {
        ringFacing = facing
        if (isRinging) {
            ringingTicks = 0
        } else {
            isRinging = true
        }
        world.addBlockEvent(pos, FBlocks.BELL, 1, facing.index)
    }
}