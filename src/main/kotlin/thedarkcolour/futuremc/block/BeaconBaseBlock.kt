package thedarkcolour.futuremc.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorldReader

class BeaconBaseBlock(properties: Properties) : Block(properties) {
    override fun isBeaconBase(state: BlockState?, world: IWorldReader?, pos: BlockPos?, beacon: BlockPos?): Boolean {
        return true
    }
}