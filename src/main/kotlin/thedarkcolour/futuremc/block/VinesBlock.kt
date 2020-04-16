package thedarkcolour.futuremc.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorld
import net.minecraft.world.IWorldReader
import net.minecraft.world.server.ServerWorld
import java.util.*

open class VinesBlock(private val direction: Direction, private val shape: VoxelShape, private val plant: () -> (Block), properties: Properties) : Block(properties) {
    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, ctx: ISelectionContext): VoxelShape {
        return shape
    }

    override fun isValidPosition(state: BlockState?, worldIn: IWorldReader, pos: BlockPos): Boolean {
        val pos1 = pos.offset(direction)
        val state1 = worldIn.getBlockState(pos1)
        val block = state1.block

        return block == this || block == plant || state1.isSideSolidFullSquare(worldIn, pos1, Direction.DOWN)
    }

    override fun scheduledTick(state: BlockState, worldIn: ServerWorld, pos: BlockPos, rand: Random) {
        if (!state.isValidPosition(worldIn, pos)) {
            worldIn.breakBlock(pos, true, null)
        } else {
            if (state.get(AGE) < 25 && rand.nextDouble() < 0.1) {
                val pos1 = pos.offset(Direction.DOWN)
                if (worldIn.getBlockState(pos1).isAir(worldIn, pos1)) {
                    worldIn.setBlockState(pos1, state.cycle(AGE))
                }
            }
        }
    }

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>) {
        builder.add(AGE)
    }

    override fun updatePostPlacement(state: BlockState, direction: Direction, neighborState: BlockState, worldIn: IWorld, pos: BlockPos, neighborPos: BlockPos): BlockState {
        if (direction == direction && !state.isValidPosition(worldIn, pos)) {
            worldIn.pendingBlockTicks.scheduleTick(pos, this, 1)
        }

        return if (direction == Direction.DOWN && neighborState.block == this) {
            plant().defaultState
        } else {
            super.updatePostPlacement(state, direction, neighborState, worldIn, pos, neighborPos)
        }
    }

    fun getRandomGrowthState(worldIn: IWorld): BlockState {
        return defaultState.with(AGE, worldIn.random.nextInt(25))
    }

    companion object {
        val SHAPE: VoxelShape = makeCuboidShape(4.0, 9.0, 4.0, 12.0, 16.0, 12.0)
        val AGE = BlockStateProperties.AGE_0_25
    }
}