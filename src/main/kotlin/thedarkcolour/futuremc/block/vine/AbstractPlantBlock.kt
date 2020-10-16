package thedarkcolour.futuremc.block.vine

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluids
import net.minecraft.item.BlockItemUseContext
import net.minecraft.item.ItemStack
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorld
import net.minecraft.world.IWorldReader
import net.minecraft.world.World
import net.minecraft.world.server.ServerWorld
import java.util.*

abstract class AbstractPlantBlock(
    properties: Properties,
    direction: Direction,
    outlineShape: VoxelShape,
    tickWater: Boolean,
) : AbstractPlantPartBlock(properties, direction, outlineShape, tickWater) {
    override val plant: Block
        get() = this

    override fun scheduledTick(state: BlockState, worldIn: ServerWorld, pos: BlockPos, rand: Random) {
        if (!state.isValidPosition(worldIn, pos)) {
            worldIn.breakBlock(pos, true, null)
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
        posFrom: BlockPos,
    ): BlockState {
        if (direction == growthDirection.opposite && !state.isValidPosition(worldIn, pos)) {
            worldIn.pendingBlockTicks.scheduleTick(pos, this, 1)
        }

        if (direction == growthDirection) {
            val block = newState.block
            val stem = this.stem

            if (block != this && block != stem) {
                return stem.getRandomGrowthState(worldIn)
            }
        }

        if (tickWater) {
            worldIn.pendingFluidTicks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn))
        }

        return super.updatePostPlacement(state, direction, newState, worldIn, pos, posFrom)
    }

    /**
     *
     * Called when A user uses the creative pick block button on this block
     *
     * @param target The full target the player is looking at
     * @return A ItemStack to add to the player's inventory, empty ItemStack if nothing should be added.
     */
    override fun getPickBlock(
        state: BlockState,
        target: RayTraceResult,
        world: IBlockReader,
        pos: BlockPos,
        player: PlayerEntity,
    ): ItemStack {
        return ItemStack(stem)
    }

    /**
     * Whether this IGrowable can grow
     */
    override fun canGrow(worldIn: IBlockReader, pos: BlockPos, state: BlockState, isClient: Boolean): Boolean {
        val stemPos = getStemPos(worldIn, pos, state)?.offset(stem.growthDirection)
        return stemPos != null && worldIn.getBlockState(stemPos).isAir(worldIn, stemPos)
    }

    override fun canUseBonemeal(worldIn: World, rand: Random, pos: BlockPos, state: BlockState): Boolean {
        return true
    }

    override fun grow(worldIn: ServerWorld, rand: Random, pos: BlockPos, state: BlockState) {
        val a = getStemPos(worldIn, pos, state)

        if (a != null) {
            val state1 = worldIn.getBlockState(a)
            (state1.block as AbstractPlantStemBlock).grow(worldIn, rand, a, state1)
        }
    }

    private fun getStemPos(worldIn: IBlockReader, pos: BlockPos, state: BlockState): BlockPos? {
        var pos1 = BlockPos.Mutable(pos)
        var block: BlockState

        do {
            pos1 = pos1.move(growthDirection)
            block = worldIn.getBlockState(pos1)
        } while (block == state)

        return if (block.block == stem) pos1 else null
    }

    override fun isReplaceable(state: BlockState, context: BlockItemUseContext): Boolean {
        val bl = super.isReplaceable(state, context)
        return if (bl && context.item.item == stem.asItem()) false else bl
    }

    /**
     * Checks if a player or entity can use this block to 'climb' like a ladder.
     *
     * @param state The current state
     * @param world The current world
     * @param pos Block position in world
     * @param entity The entity trying to use the ladder, CAN be null.
     * @return True if the block should act like a ladder
     */
    override fun isLadder(state: BlockState?, world: IWorldReader?, pos: BlockPos?, entity: LivingEntity?): Boolean {
        return true
    }
}