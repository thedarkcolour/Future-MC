package thedarkcolour.futuremc.feature

import com.mojang.datafixers.Dynamic
import net.minecraft.block.Blocks
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.IWorld
import net.minecraft.world.World
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.GenerationSettings
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.NoFeatureConfig
import thedarkcolour.futuremc.registry.FBlocks
import java.util.*

class TwistingVinesFeature(configFactory: (Dynamic<*>) -> NoFeatureConfig) : Feature<NoFeatureConfig>(configFactory) {
    override fun place(worldIn: IWorld, generator: ChunkGenerator<out GenerationSettings>, rand: Random, pos: BlockPos, config: NoFeatureConfig): Boolean {
        return placeVines(worldIn, rand, pos, 8, 4, 8)
    }

    companion object {
        fun placeVines(worldIn: IWorld, rand: Random, pos: BlockPos, i: Int, i1: Int, i2: Int): Boolean {
            return if (isValidSpawn(worldIn, pos)) {
                placeVinesInArea(worldIn, rand, pos, i, i1, i2)
                true
            } else false
        }

        private fun isValidSpawn(worldIn: IWorld, pos: BlockPos): Boolean {
            return if (!worldIn.isAirBlock(pos)) {
                false
            } else {
                val block = worldIn.getBlockState(pos.down()).block
                block == Blocks.NETHERRACK || block == FBlocks.WARPED_NYLIUM || block == FBlocks.WARPED_WART_BLOCK
            }
        }

        private fun placeVinesInArea(worldIn: IWorld, rand: Random, pos: BlockPos, i: Int, j: Int, k: Int) {
            val mutable = BlockPos.Mutable()

            for (l in 0 until i * i) {
                mutable.setPos(pos).move(MathHelper.nextInt(rand, -i, i), MathHelper.nextInt(rand, -j, j), MathHelper.nextInt(rand, -i, i))
                if (heightFits(worldIn, mutable) && isValidSpawn(worldIn, mutable)) {
                    var m = MathHelper.nextInt(rand, 1, k)
                    if (rand.nextInt(6) == 0) {
                        m *= 2
                    }

                    if (rand.nextInt(5) == 0) {
                        m = 1
                    }

                    generateVine(worldIn, rand, mutable, m, 17, 25)
                }
            }
        }

        private fun heightFits(worldIn: IWorld, mutable: BlockPos.Mutable): Boolean {
            do {
                mutable.move(0, -1, 0)
                if (World.isOutsideBuildHeight(mutable)) {
                    return false
                }
            } while (worldIn.isAirBlock(mutable))

            mutable.move(0, 1, 0)
            return true
        }

        private fun generateVine(worldIn: IWorld, rand: Random, pos: BlockPos.Mutable, maxLength: Int, minAge: Int, maxAge: Int) {
            for (i in 1..maxLength) {
                if (worldIn.isAirBlock(pos)) {
                    if (i == maxLength || !worldIn.isAirBlock(pos.up())) {
                        worldIn.setBlockState(pos, FBlocks.TWISTING_VINES.defaultState.with(BlockStateProperties.AGE_0_25, MathHelper.nextInt(rand, minAge, maxAge)), 2)
                        break
                    }
                    worldIn.setBlockState(pos, FBlocks.TWISTING_VINES_PLANT.defaultState, 2)
                }
                pos.move(0, 1, 0)
            }
        }
    }
}