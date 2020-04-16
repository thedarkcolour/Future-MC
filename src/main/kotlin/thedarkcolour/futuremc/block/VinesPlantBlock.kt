package thedarkcolour.futuremc.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorld
import net.minecraft.world.IWorldReader
import net.minecraft.world.server.ServerWorld
import java.util.*

open class VinesPlantBlock(private val shape: VoxelShape, private val stem: () -> VinesBlock, properties: Properties) : Block(properties) {
    override fun isValidPosition(state: BlockState?, worldIn: IWorldReader, pos: BlockPos): Boolean {
        val pos1 = pos.offset(Direction.UP)
        val state1 = worldIn.getBlockState(pos1)
        val block = state1.block

        return block == stem || block == this || state1.isSideSolidFullSquare(worldIn, pos1, Direction.DOWN)
    }

    override fun scheduledTick(state: BlockState, worldIn: ServerWorld, pos: BlockPos, p_225534_4_: Random) {
        if (!state.isValidPosition(worldIn, pos)) {
            worldIn.breakBlock(pos, true, null)
        }
    }

    override fun updatePostPlacement(state: BlockState, direction: Direction, neighborState: BlockState, worldIn: IWorld, pos: BlockPos, neighborPos: BlockPos): BlockState {
        if (direction == Direction.UP && !state.isValidPosition(worldIn, pos)) {
            worldIn.pendingBlockTicks.scheduleTick(pos, this, 1)
        }

        if (direction == Direction.DOWN) {
            val block = neighborState.block

            if (block != this && block != stem) {
                return stem().getRandomGrowthState(worldIn)
            }
        }

        return super.updatePostPlacement(state, direction, neighborState, worldIn, pos, neighborPos)
    }

    override fun getPickBlock(state: BlockState?, target: RayTraceResult?, world: IBlockReader?, pos: BlockPos?, player: PlayerEntity?): ItemStack {
        return stem().getPickBlock(state, target, world, pos, player)
    }

    override fun getShape(p_220053_1_: BlockState, p_220053_2_: IBlockReader, p_220053_3_: BlockPos, p_220053_4_: ISelectionContext): VoxelShape {
        return shape
    }

    companion object {
        val SHAPE = makeCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0)
    }
}