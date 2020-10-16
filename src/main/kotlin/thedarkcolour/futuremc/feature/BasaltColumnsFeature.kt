package thedarkcolour.futuremc.feature

import com.google.common.collect.ImmutableList
import com.mojang.datafixers.Dynamic
import net.minecraft.block.Blocks
import net.minecraft.tags.FluidTags
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.GenerationSettings
import net.minecraft.world.gen.feature.Feature
import thedarkcolour.futuremc.registry.FBlocks
import thedarkcolour.futuremc.util.clampAxis
import java.util.*
import java.util.function.Function
import kotlin.math.min

class BasaltColumnsFeature(codec: Function<Dynamic<*>, out BasaltColumnsConfig>) : Feature<BasaltColumnsConfig>(codec) {
    override fun place(
        worldIn: IWorld,
        generator: ChunkGenerator<out GenerationSettings>,
        rand: Random,
        pos: BlockPos,
        config: BasaltColumnsConfig
    ): Boolean {
        val seaLevel = generator.seaLevel
        val position = getPlacementPos(
            worldIn, seaLevel, BlockPos.Mutable(pos).clampAxis(
                Direction.Axis.Y,
                1,
                generator.maxHeight - 1
            ), Int.MAX_VALUE
        )

        if (position == null) {
            return false
        } else {
            val height = getHeight(rand, config)
            val large = rand.nextFloat() < 0.9f
            val k = min(height, if (large) 5 else 8)
            val l = if (large) 50 else 15
            var b1 = false

            for (p in BlockPosUtil.getColumnHeightmap(
                rand,
                l,
                position.x - k,
                position.y,
                position.z - k,
                position.x + k,
                position.y,
                position.z + k
            )) {
                val strength = height - p.manhattanDistance(position)

                if (strength >= 0) {
                    b1 = b1 || placeBasalt(worldIn, seaLevel, p, strength, getReach(rand, config))
                }
            }

            return b1
        }
    }

    companion object {
        private val NETHER_BLOCKS = ImmutableList.of(
            Blocks.LAVA,
            Blocks.BEDROCK,
            Blocks.MAGMA_BLOCK,
            Blocks.SOUL_SAND,
            Blocks.NETHER_BRICKS,
            Blocks.NETHER_BRICK_FENCE,
            Blocks.NETHER_BRICK_STAIRS,
            Blocks.NETHER_WART,
            Blocks.CHEST,
            Blocks.SPAWNER
        )

        private fun getPlacementPos(
            worldIn: IWorld,
            seaLevel: Int,
            mutable: BlockPos.Mutable,
            attempts: Int
        ): BlockPos? {
            var attemptsRemaining = attempts

            while (mutable.y > 1 && attemptsRemaining > 0) {
                --attemptsRemaining
                if (isReplaceableSpace(worldIn, seaLevel, mutable)) {
                    val state = worldIn.getBlockState(mutable.move(Direction.DOWN))
                    mutable.move(Direction.UP)
                    if (!state.isAir(worldIn, mutable.toImmutable()) && !NETHER_BLOCKS.contains(state.block)) {
                        return mutable
                    }
                }
                mutable.move(Direction.DOWN)
            }
            return null
        }

        private fun getHeight(rand: Random, config: BasaltColumnsConfig): Int {
            return config.minimumHeight + rand.nextInt(config.maximumHeight - config.minimumHeight + 1)
        }

        private fun placeBasalt(
            worldIn: IWorld,
            seaLevel: Int,
            pos: BlockPos,
            strength: Int,
            reach: Int
        ): Boolean {
            var flag = false
            for (p in BlockPos.getAllInBoxMutable(pos.x - reach, pos.y, pos.z - reach, pos.x + reach, pos.y, pos.z + reach)) {
                val distance = p.manhattanDistance(pos)
                val placementPos = if (isReplaceableSpace(worldIn, seaLevel, p)) {
                    getPlacementPos(worldIn, seaLevel, BlockPos.Mutable(p), distance)
                } else {
                    adjustHeight(worldIn, BlockPos.Mutable(p), distance)
                }

                if (placementPos != null) {
                    var j = strength - distance / 2
                    val mutable = BlockPos.Mutable(placementPos)
                    while (j >= 0) {
                        if (isReplaceableSpace(worldIn, seaLevel, mutable)) {
                            worldIn.setBlockState(mutable, FBlocks.BASALT.defaultState, 3)
                            mutable.move(Direction.UP)
                            flag = true
                        } else {
                            if (worldIn.getBlockState(mutable).block != FBlocks.BASALT) {
                                break
                            }
                            mutable.move(Direction.UP)
                        }
                        --j
                    }
                }
            }
            return flag
        }

        private fun adjustHeight(worldIn: IWorld, mutable: BlockPos.Mutable, distance: Int): BlockPos? {
            var i = distance
            while (mutable.y < worldIn.height && i > 0) {
                --i
                val state = worldIn.getBlockState(mutable)
                if (NETHER_BLOCKS.contains(state.block)) {
                    return null
                }
                if (state.isAir(worldIn, mutable.toImmutable())) {
                    return mutable
                }
                mutable.move(Direction.UP)
            }
            return null
        }

        private fun getReach(rand: Random, config: BasaltColumnsConfig): Int {
            return config.minimumReach + rand.nextInt(config.maximumReach - config.minimumReach + 1)
        }

        private fun isReplaceableSpace(worldIn: IWorld, seaLevel: Int, pos: BlockPos): Boolean {
            val state = worldIn.getBlockState(pos)
            return state.isAir(worldIn, pos) || state.fluidState.fluid.isIn(FluidTags.LAVA) && pos.y <= seaLevel
        }
    }
}