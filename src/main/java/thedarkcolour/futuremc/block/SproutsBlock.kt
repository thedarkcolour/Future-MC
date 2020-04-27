package thedarkcolour.futuremc.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.pathfinding.PathType
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorld
import net.minecraft.world.IWorldReader
import thedarkcolour.futuremc.registry.FBlocks

class SproutsBlock(properties: Properties) : Block(properties) {
    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return SHAPE
    }

    override fun allowsMovement(state: BlockState, worldIn: IBlockReader, pos: BlockPos, type: PathType): Boolean {
        return false
    }

    override fun updatePostPlacement(state: BlockState, facing: Direction, facingState: BlockState, worldIn: IWorld, pos: BlockPos, facingPos: BlockPos): BlockState {
        return if (!state.isValidPosition(worldIn, pos)) {
            Blocks.AIR.defaultState
        } else {
            super.updatePostPlacement(state, facing, facingState, worldIn, pos, facingPos)
        }
    }

    override fun isValidPosition(state: BlockState, worldIn: IWorldReader, pos: BlockPos): Boolean {
        val block = worldIn.getBlockState(pos.down())
        return block.isIn(FBlocks.NYLIUM) || block == FBlocks.SOUL_SOIL || block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL || block == Blocks.FARMLAND
    }

    companion object {
        val SHAPE = makeCuboidShape(2.0, 0.0, 2.0, 14.0, 3.0, 14.0)
    }
}