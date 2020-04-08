package thedarkcolour.futuremc.block

import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import thedarkcolour.core.block.BlockBase

class BlockSeaGrass : BlockWaterPlant("seagrass") {
    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos) = AABB

    companion object {
        val AABB = BlockBase.makeAABB(2.0, 0.0, 2.0, 14.0, 12.0, 14.0)
    }
}