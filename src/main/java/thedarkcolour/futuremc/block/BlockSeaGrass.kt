package thedarkcolour.futuremc.block

import net.minecraft.block.state.IBlockState
import thedarkcolour.core.block.BlockBase
import thedarkcolour.futuremc.tile.TileWaterlogged

class BlockSeaGrass : BlockWaterPlant("seagrass") {
    override fun getAlternateState(te: TileWaterlogged): IBlockState {
        return null!!
    }

    companion object {
        val AABB = BlockBase.makeAABB(2.0, 0.0, 2.0, 14.0, 12.0, 14.0)
    }
}