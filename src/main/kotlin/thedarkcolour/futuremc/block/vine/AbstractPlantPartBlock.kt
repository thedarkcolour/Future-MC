package thedarkcolour.futuremc.block.vine

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.IGrowable
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorldReader

abstract class AbstractPlantPartBlock(
    properties: Properties,
    val growthDirection: Direction,
    protected val shape: VoxelShape,
    protected val tickWater: Boolean,
) : Block(properties), IGrowable {
    abstract val stem: AbstractPlantStemBlock
    abstract val plant: Block

    override fun isValidPosition(state: BlockState, worldIn: IWorldReader, pos: BlockPos): Boolean {
        val pos1 = pos.offset(growthDirection.opposite)
        val state1 = worldIn.getBlockState(pos1)
        val block = state1.block

        return if (!canAttachTo(block)) {
            false
        } else {
            block == stem || block == plant || state1.isSideSolidFullSquare(worldIn, pos1, growthDirection)
        }
    }

    protected open fun canAttachTo(block: Block): Boolean {
        return true
    }

    override fun getShape(
        state: BlockState,
        worldIn: IBlockReader,
        pos: BlockPos,
        context: ISelectionContext
    ): VoxelShape {
        return shape
    }
}