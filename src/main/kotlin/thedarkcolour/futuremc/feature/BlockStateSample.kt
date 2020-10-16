package thedarkcolour.futuremc.feature

import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.fluid.IFluidState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader

class BlockStateSample(val states: Array<BlockState>) : IBlockReader {
    override fun getTileEntity(pos: BlockPos): TileEntity? {
        return null
    }

    override fun getBlockState(pos: BlockPos): BlockState {
        val y = pos.y
        return if (y >= 0 && y < states.size) states[y] else Blocks.AIR.defaultState
    }

    override fun getFluidState(pos: BlockPos): IFluidState {
        return getBlockState(pos).fluidState
    }
}