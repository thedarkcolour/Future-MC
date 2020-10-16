package thedarkcolour.futuremc.block

import net.minecraft.block.BlockState
import net.minecraft.block.BushBlock
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import thedarkcolour.futuremc.registry.FBlocks

class RootsBlock(properties: Properties) : BushBlock(properties) {
    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return SHAPE
    }

    override fun isValidGround(floor: BlockState, worldIn: IBlockReader, pos: BlockPos): Boolean {
        val block = floor.block
        return block.isIn(FBlocks.NYLIUM) || block == FBlocks.SOUL_SOIL || super.isValidGround(floor, worldIn, pos)
    }

    companion object {
        private val SHAPE = makeCuboidShape(2.0, 0.0, 2.0, 14.0, 13.0, 14.0)
    }
}