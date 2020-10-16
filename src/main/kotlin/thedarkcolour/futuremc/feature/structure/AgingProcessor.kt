package thedarkcolour.futuremc.feature.structure

import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.types.DynamicOps
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.StairsBlock
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.state.properties.Half
import net.minecraft.tags.BlockTags
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorldReader
import net.minecraft.world.gen.feature.template.IStructureProcessorType
import net.minecraft.world.gen.feature.template.PlacementSettings
import net.minecraft.world.gen.feature.template.StructureProcessor
import net.minecraft.world.gen.feature.template.Template
import thedarkcolour.futuremc.registry.FBlocks
import java.util.*

/**
 * "Ages" blocks in a structure by making parts of the structure mossy.
 *
 * @property mossiness the rate at which blocks in the structure are aged
 */
class AgingProcessor(private val mossiness: Float) : StructureProcessor() {

    /** Deserializer */
    constructor(serialized: Dynamic<*>) : this(serialized["mossiness"].asFloat(0.0f))

    /**
     * FORGE: Add template parameter
     *
     * @param worldReaderIn
     * @param pos
     * @param p_215194_3_
     * @param blockInfo
     * @param placementSettingsIn
     * @param template The template being placed, can be null due to deprecated
     * method calls.
     * @see process
     */
    override fun process(
        worldReaderIn: IWorldReader,
        pos: BlockPos,
        p_215194_3_: Template.BlockInfo,
        blockInfo: Template.BlockInfo,
        placementSettingsIn: PlacementSettings,
        template: Template?
    ): Template.BlockInfo? {
        val rand = placementSettingsIn.getRandom(blockInfo.pos)
        val state = blockInfo.state
        val pos1 = blockInfo.pos
        var state1: BlockState? = null

        if (state.block != Blocks.STONE_BRICKS && state.block != Blocks.STONE && state.block != Blocks.CHISELED_STONE_BRICKS) {
            when {
                state.isIn(BlockTags.STAIRS)   -> state1 = processStairs(rand, blockInfo.state)
                state.isIn(BlockTags.SLABS)    -> state1 = processSlabs(rand)
                state.isIn(BlockTags.WALLS)    -> state1 = processWalls(rand)
                state.block == Blocks.OBSIDIAN -> state1 = processObsidian(rand)
            }
        } else {
            state1 = processBlocks(rand)
        }

        return if (state1 != null) {
            Template.BlockInfo(pos1, state1, blockInfo.nbt)
        } else blockInfo
    }

    private fun processStairs(rand: Random, state: BlockState): BlockState? {
        val direction = state.get(BlockStateProperties.HORIZONTAL_FACING)
        val blockHalf = state.get(BlockStateProperties.HALF)

        return if (rand.nextFloat() >= 0.5f) {
            null
        } else {
            // mossy stone brick stairs w/ facing and half properties
            // mossy stone brick slab default state
            val mossyStates = arrayOf(Blocks.MOSSY_STONE_BRICK_STAIRS.defaultState.with(BlockStateProperties.HORIZONTAL_FACING, direction).with(BlockStateProperties.HALF, blockHalf), Blocks.MOSSY_STONE_BRICK_SLAB.defaultState)
            processBlockState(rand, DEFAULT_STATES, mossyStates)
        }
    }

    private fun processSlabs(rand: Random): BlockState? {
        return if (rand.nextFloat() < mossiness) Blocks.MOSSY_STONE_BRICK_SLAB.defaultState else null
    }

    private fun processWalls(rand: Random): BlockState? {
        return if (rand.nextFloat() < mossiness) Blocks.MOSSY_STONE_BRICK_WALL.defaultState else null
    }

    private fun processObsidian(rand: Random): BlockState? {
        return if (rand.nextFloat() < mossiness) FBlocks.CRYING_OBSIDIAN.defaultState else null
    }

    private fun processBlocks(rand: Random): BlockState? {
        return if (rand.nextFloat() >= 0.5f) {
            null
        } else {
            val defaultStates = arrayOf(
                Blocks.CRACKED_STONE_BRICKS.defaultState,
                randomStairProperties(rand, Blocks.STONE_BRICK_STAIRS)
            )
            val mossyStates = arrayOf(
                Blocks.MOSSY_STONE_BRICKS.defaultState,
                randomStairProperties(rand, Blocks.MOSSY_STONE_BRICK_STAIRS)
            )

            processBlockState(rand, defaultStates, mossyStates)
        }
    }

    private fun processBlockState(rand: Random, defaultStates: Array<BlockState>, mossyStates: Array<BlockState>): BlockState {
        return if (rand.nextFloat() < mossiness) pickRandomly(rand, mossyStates) else pickRandomly(rand, defaultStates)
    }

    private fun <T> pickRandomly(rand: Random, array: Array<T>): T {
        return array[rand.nextInt(array.size)]
    }

    private fun randomStairProperties(rand: Random, stairs: Block): BlockState {
        return stairs.defaultState
            .with(StairsBlock.FACING, Direction.Plane.HORIZONTAL.random(rand))
            .with(StairsBlock.HALF, Half.values()[rand.nextInt(Half.values().size)])
    }

    override fun <T : Any?> serialize0(ops: DynamicOps<T>): Dynamic<T> {
        TODO()
    }

    override fun getType(): IStructureProcessorType {
        TODO("not implemented")
    }

    companion object {
        private val DEFAULT_STATES = arrayOf(Blocks.STONE_SLAB.defaultState, Blocks.STONE_BRICK_SLAB.defaultState)
    }
}