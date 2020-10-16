package thedarkcolour.futuremc.block.vine

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.fluid.Fluids
import net.minecraft.state.IntegerProperty
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorld
import net.minecraft.world.World
import net.minecraft.world.server.ServerWorld
import java.util.*

abstract class AbstractPlantStemBlock(
    properties: Properties,
    growthDirection: Direction,
    outlineShape: VoxelShape,
    tickWater: Boolean,
    private val growthChance: Double,
) : AbstractPlantPartBlock(properties, growthDirection, outlineShape, tickWater) {

    override val stem: AbstractPlantStemBlock
        get() = this

    init {
        defaultState = defaultState.with(AGE, 0)
    }

    fun getRandomGrowthState(world: IWorld): BlockState {
        return defaultState.with(AGE, world.random.nextInt(25))
    }

    override fun scheduledTick(state: BlockState, worldIn: ServerWorld, pos: BlockPos, rand: Random) {
        if (!state.isValidPosition(worldIn, pos)) {
            worldIn.breakBlock(pos, true, null)
        }
    }

    /**
     * Returns whether or not this block is of a type that needs random ticking. Called for ref-counting purposes by
     * ExtendedBlockStorage in order to broadly cull a chunk from the random chunk update list for efficiency's sake.
     */
    override fun ticksRandomly(state: BlockState): Boolean {
        return state.get(AGE) < 25
    }

    override fun randomTick(state: BlockState, worldIn: ServerWorld, pos: BlockPos, random: Random) {
        if (state.get(AGE) < 25 && random.nextDouble() < growthChance) {
            val pos1 = pos.offset(growthDirection)
            if (worldIn.getBlockState(pos1).isAir(worldIn, pos1)) {
                worldIn.setBlockState(pos1, state.cycle(AGE))
            }
        }
    }

    /**
     * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
     * returns its solidified counterpart.
     * Note that this method should ideally consider only the specific face passed in.
     */
    override fun updatePostPlacement(
        state: BlockState,
        direction: Direction,
        newState: BlockState,
        worldIn: IWorld,
        pos: BlockPos,
        posFrom: BlockPos
    ): BlockState {
        if (direction == growthDirection.opposite && !state.isValidPosition(worldIn, pos)) {
            worldIn.pendingBlockTicks.scheduleTick(pos, this, 1)
        }

        return if (direction == growthDirection && newState.block == this) {
            plant.defaultState
        } else {
            if (tickWater) {
                worldIn.pendingFluidTicks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn))
            }
            super.updatePostPlacement(state, direction, newState, worldIn, pos, posFrom)
        }
    }

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>) {
        builder.add(AGE)
    }

    /**
     * Whether this IGrowable can grow
     */
    override fun canGrow(worldIn: IBlockReader, pos: BlockPos, state: BlockState, isClient: Boolean): Boolean {
        val a = pos.offset(growthDirection)
        return worldIn.getBlockState(a).isAir(worldIn, a)
    }

    override fun canUseBonemeal(worldIn: World, rand: Random, pos: BlockPos, state: BlockState): Boolean {
        return true
    }

    override fun grow(worldIn: ServerWorld, rand: Random, pos: BlockPos, state: BlockState) {
        val pos1 = BlockPos.Mutable(pos).move(growthDirection)
        var i = (state[BlockStateProperties.AGE_0_25] + 1).coerceAtMost(25)
        val j = getGrowth(rand)
        var k = 0

        while (k < j && worldIn.isAirBlock(pos1)) {
            worldIn.setBlockState(pos1, state.with(BlockStateProperties.AGE_0_25, i))
            pos1.move(growthDirection)
            i = (i + 1).coerceAtMost(25)
            ++k
        }
    }

    private fun getGrowth(rand: Random): Int {
        var d = 1.0

        var i = 0
        while (rand.nextDouble() < d) {
            d *= 0.826
            ++i
        }

        return i
    }

    companion object {
        val AGE: IntegerProperty = BlockStateProperties.AGE_0_25
    }
}