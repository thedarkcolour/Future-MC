package thedarkcolour.futuremc.block.netherupdate

import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import thedarkcolour.core.block.FBlock

class ChainBlock(properties: Properties) : FBlock(properties) {
    override fun getBoundingBox(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): AxisAlignedBB {
        return BOX
    }

    companion object {
        private val BOX = makeAABB(6.5, 0.0, 6.5, 9.5, 16.0, 9.5)
    }
}