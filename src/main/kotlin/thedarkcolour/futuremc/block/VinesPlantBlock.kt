package thedarkcolour.futuremc.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.IGrowable
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
import net.minecraft.world.World
import net.minecraft.world.server.ServerWorld
import java.util.*

open class VinesPlantBlock(private val shape: VoxelShape, private val stem: () -> VinesBlock, properties: Properties) : Block(properties), IGrowable {
    override fun isValidPosition(state: BlockState?, worldIn: IWorldReader, pos: BlockPos): Boolean {
        val stem = stem()
        val pos1 = pos.offset(stem.direction.opposite)
        val state1 = worldIn.getBlockState(pos1)
        val block = state1.block

        return block == stem || block == this || state1.isSideSolidFullSquare(worldIn, pos1, stem.direction)
    }

    override fun scheduledTick(state: BlockState, worldIn: ServerWorld, pos: BlockPos, rand: Random) {
        if (!state.isValidPosition(worldIn, pos)) {
            worldIn.breakBlock(pos, true, null)
        }
    }

    override fun updatePostPlacement(state: BlockState, direction: Direction, neighborState: BlockState, worldIn: IWorld, pos: BlockPos, neighborPos: BlockPos): BlockState {
        val stem = stem()

        if (direction == stem.direction.opposite && !state.isValidPosition(worldIn, pos)) {
            worldIn.pendingBlockTicks.scheduleTick(pos, this, 1)
        }

        if (direction == stem.direction) {
            val block = neighborState.block

            if (block != this && block != stem) {
                return stem.getRandomGrowthState(worldIn)
            }
        }

        return state
    }

    override fun getPickBlock(state: BlockState?, target: RayTraceResult?, world: IBlockReader?, pos: BlockPos?, player: PlayerEntity?): ItemStack {
        return stem().getPickBlock(state, target, world, pos, player)
    }

    override fun getShape(state: BlockState?, worldIn: IBlockReader?, pos: BlockPos?, context: ISelectionContext?): VoxelShape {
        return shape
    }

    override fun canGrow(worldIn: IBlockReader, pos: BlockPos, state: BlockState, isClient: Boolean): Boolean {
        val stemPos = getStemPos(worldIn, pos, state)?.offset(stem().direction)
        return stemPos != null && worldIn.getBlockState(stemPos).isAir(worldIn, stemPos)
    }

    private fun getStemPos(worldIn: IBlockReader, pos: BlockPos, state: BlockState): BlockPos? {
        val stem = stem()
        val direction = stem.direction
        var pos1 = BlockPos.Mutable(pos)

        var block: BlockState
        do {
            pos1 = pos1.move(direction)
            block = worldIn.getBlockState(pos1)
        } while (block == state)

        return if (state.block == stem) pos1 else null
    }

    override fun canUseBonemeal(worldIn: World?, rand: Random?, pos: BlockPos?, state: BlockState?): Boolean {
        return true
    }

    override fun grow(worldIn: ServerWorld, rand: Random, pos: BlockPos, state: BlockState) {
        TODO("not implemented")
    }
}