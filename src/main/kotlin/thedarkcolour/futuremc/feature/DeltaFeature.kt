package thedarkcolour.futuremc.feature

import com.mojang.datafixers.Dynamic
import net.minecraft.block.Blocks
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.GenerationSettings
import net.minecraft.world.gen.feature.Feature
import thedarkcolour.futuremc.util.clampAxis
import java.util.*

class DeltaFeature(configFactoryIn: (Dynamic<*>) -> DeltaFeatureConfig) : Feature<DeltaFeatureConfig>(configFactoryIn) {
    override fun place(
        worldIn: IWorld,
        generator: ChunkGenerator<out GenerationSettings>,
        rand: Random,
        pos: BlockPos,
        config: DeltaFeatureConfig
    ): Boolean {
        val pos1 = getStartingPosition(worldIn, BlockPos.Mutable(pos).clampAxis(Direction.Axis.Y, 1, worldIn.height - 1))

        if (pos1 == null) {
            return false
        } else {
            var bl = false
            val bl2 = rand.nextDouble() < 0.9
            val i = if (bl2) getRimSize(rand, config) else 0
            val j = if (bl2) getRimSize(rand, config) else 0
            val bl3 = bl2 && i != 0 && j != 0
            val k = getRadius(rand, config)
            val l = getRadius(rand, config)
            val m = k.coerceAtLeast(l)
            for (blockPos3 in BlockPosUtil.iterateOutwards(pos, k, 0, l)) {
                if (blockPos3.manhattanDistance(pos1) > m) {
                    break
                }
                if (canPlace(worldIn, blockPos3, config)) {
                    if (bl3) {
                        bl = true
                        setBlockState(worldIn, blockPos3, config.rim)
                    }
                    val blockPos4 = blockPos3.add(i, 0, j)
                    if (canPlace(worldIn, blockPos4, config)) {
                        bl = true
                        setBlockState(worldIn, blockPos4, config.contents)
                    }
                }
            }

            return bl
        }
    }

    private fun getStartingPosition(worldIn: IWorld, mutable: BlockPos.Mutable): BlockPos? {
        while (mutable.y > 1) {
            if (worldIn.isAirBlock(mutable)) {
                val state = worldIn.getBlockState(mutable.move(Direction.DOWN))
                mutable.move(Direction.UP)

                if (state.block != Blocks.LAVA) {
                    return mutable
                }
            }
            mutable.move(Direction.DOWN)
        }

        return null
    }

    private fun getRimSize(rand: Random, config: DeltaFeatureConfig): Int {
        return rand.nextInt(config.maxRim + 1)
    }

    private fun getRadius(rand: Random, config: DeltaFeatureConfig): Int {
        return config.minRadius + rand.nextInt(config.maxRadius - config.minRadius + 1)
    }

    private fun canPlace(
        worldAccess: IWorld,
        blockPos: BlockPos,
        deltaFeatureConfig: DeltaFeatureConfig,
    ): Boolean {
        val state = worldAccess.getBlockState(blockPos)

        return when {
            state.block == deltaFeatureConfig.contents.block -> false
            INVALID_BLOCKS.contains(state.block) -> false
            else -> {
                for (direction in DIRECTIONS) {
                    val bl = worldAccess.isAirBlock(blockPos.offset(direction))

                    if (bl && direction != Direction.UP || !bl && direction == Direction.UP) {
                        return false
                    }
                }
                true
            }
        }
    }

    companion object {
        val INVALID_BLOCKS = setOf(
            Blocks.BEDROCK,
            Blocks.NETHER_BRICKS,
            Blocks.NETHER_BRICK_FENCE,
            Blocks.NETHER_BRICK_STAIRS,
            Blocks.NETHER_WART,
            Blocks.CHEST,
            Blocks.SPAWNER,
        )
        val DIRECTIONS = Direction.values()
    }
}