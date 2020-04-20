package thedarkcolour.futuremc.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.IGrowable
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorld
import net.minecraft.world.IWorldReader
import net.minecraft.world.World
import net.minecraft.world.server.ServerWorld
import java.util.*

open class VinesBlock(val direction: Direction, private val shape: VoxelShape, private val plant: () -> (Block), properties: Properties) : Block(properties), IGrowable {
    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, ctx: ISelectionContext): VoxelShape {
        return shape
    }

    override fun isValidPosition(state: BlockState?, worldIn: IWorldReader, pos: BlockPos): Boolean {
        val pos1 = pos.offset(direction.opposite)
        val state1 = worldIn.getBlockState(pos1)
        val block = state1.block

        return block == this || block == plant || state1.isSideSolidFullSquare(worldIn, pos1, direction)
    }

    override fun scheduledTick(state: BlockState, worldIn: ServerWorld, pos: BlockPos, rand: Random) {
        if (state.get(BlockStateProperties.AGE_0_25) < 25 && rand.nextDouble() < 0.1) {
            val pos1 = pos.offset(Direction.DOWN)
            if (worldIn.getBlockState(pos1).isAir(worldIn, pos1)) {
                worldIn.setBlockState(pos1, state.cycle(BlockStateProperties.AGE_0_25))
            }
        }
    }

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>) {
        builder.add(BlockStateProperties.AGE_0_25)
    }

    override fun updatePostPlacement(state: BlockState, direction: Direction, neighborState: BlockState, worldIn: IWorld, pos: BlockPos, neighborPos: BlockPos): BlockState {
        if (direction == this.direction.opposite && !state.isValidPosition(worldIn, pos)) {
            worldIn.pendingBlockTicks.scheduleTick(pos, this, 1)
        }

        return if (direction == this.direction && neighborState.block == this) {
            plant().defaultState
        } else {
            super.updatePostPlacement(state, direction, neighborState, worldIn, pos, neighborPos)
        }
    }

    fun getRandomGrowthState(worldIn: IWorld): BlockState {
        return defaultState.with(BlockStateProperties.AGE_0_25, worldIn.random.nextInt(25))
    }

    override fun canGrow(worldIn: IBlockReader, pos: BlockPos, state: BlockState?, isClient: Boolean): Boolean {
        val offset = pos.offset(direction)
        return worldIn.getBlockState(offset).isAir(worldIn, offset)
    }

    override fun canUseBonemeal(worldIn: World?, rand: Random?, pos: BlockPos?, state: BlockState?): Boolean {
        return true
    }

    override fun grow(worldIn: ServerWorld, rand: Random, pos: BlockPos, state: BlockState) {
        val pos1 = BlockPos.Mutable(pos).move(direction)
        var i = (state[BlockStateProperties.AGE_0_25] + 1).coerceAtMost(25)
        val j = getGrowth(rand)
        var k = 0

        while (k < j && state.isAir(worldIn, pos1)) {
            worldIn.setBlockState(pos1, state.with(BlockStateProperties.AGE_0_25, i))
            pos1.move(direction)
            i = (i + 1).coerceAtMost(25)
            ++k
        }
    }

    private fun getGrowth(rand: Random): Int {
        var d = 1.0

        var i = 0
        while (rand.nextDouble() < d) {
            d *= 0.94
            ++i
        }

        return i
    }
}