package thedarkcolour.futuremc.feature

import com.mojang.datafixers.Dynamic
import net.minecraft.block.Blocks
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.IWorld
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.GenerationSettings
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.NoFeatureConfig
import thedarkcolour.futuremc.registry.FBlocks
import java.util.*

class WeepingVinesFeature(configFactory: (Dynamic<*>) -> NoFeatureConfig) : Feature<NoFeatureConfig>(configFactory) {
    override fun place(worldIn: IWorld, generator: ChunkGenerator<out GenerationSettings>, rand: Random, pos: BlockPos, config: NoFeatureConfig): Boolean {
        return placeVines(worldIn, rand, pos)
    }

    companion object {
        fun placeVines(worldIn: IWorld, rand: Random, pos: BlockPos): Boolean {
            return if (isValidSpawn(worldIn, pos)) {
                placeVinesInArea(worldIn, rand, pos)
                placeWartBlocksInArea(worldIn, rand, pos)
                true
            } else false
        }

        private fun isValidSpawn(worldIn: IWorld, pos: BlockPos): Boolean {
            return if (!worldIn.isAirBlock(pos)) {
                false
            } else {
                val block = worldIn.getBlockState(pos.up()).block
                block == Blocks.NETHERRACK || block == Blocks.NETHER_WART_BLOCK
            }
        }

        private fun placeVinesInArea(worldIn: IWorld, rand: Random, pos: BlockPos) {
            val mutable = BlockPos.Mutable()

            for (l in 0 until 100) {
                mutable.setPos(pos).move(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(2) - rand.nextInt(7), rand.nextInt(8) - rand.nextInt(8))

                if (isValidSpawn(worldIn, pos)) {
                    var j = MathHelper.nextInt(rand, 1, 8)

                    if (rand.nextInt(6) == 0) {
                        j *= 2
                    }
                    if (rand.nextInt(5) == 0) {
                        j = 1
                    }

                    placeVine(worldIn, rand, mutable, j, 17, 25)
                }
            }
        }

        private fun placeWartBlocksInArea(worldIn: IWorld, rand: Random, pos: BlockPos) {
            worldIn.setBlockState(pos, Blocks.NETHER_WART_BLOCK.defaultState, 2)
            val mutable1 = BlockPos.Mutable()
            val mutable2 = BlockPos.Mutable()

            for (i in 0 until 200) {
                mutable1.setPos(pos.x + rand.nextInt(6) - rand.nextInt(6), pos.y + rand.nextInt(2) - rand.nextInt(5), pos.z + rand.nextInt(6) - rand.nextInt(6))
                if (worldIn.isAirBlock(mutable1)) {
                    var j = 0
                    for (direction in Direction.values()) {
                        val block = worldIn.getBlockState(mutable2.setPos(pos.offset(direction))).block

                        if (block == Blocks.NETHERRACK || block == Blocks.NETHER_WART_BLOCK) {
                            ++j
                        }

                        if (j > 1) {
                            break
                        }
                    }

                    if (j == 1) {
                        worldIn.setBlockState(mutable1, Blocks.NETHER_WART_BLOCK.defaultState, 2)
                    }
                }
            }
        }

        fun placeVine(worldIn: IWorld, rand: Random, pos: BlockPos.Mutable, length: Int, minAge: Int, maxAge: Int) {
            for (i in 0..length) {
                if (worldIn.isAirBlock(pos)) {
                    if (i == length || !worldIn.isAirBlock(pos.down())) {
                        worldIn.setBlockState(pos, FBlocks.WEEPING_VINES.defaultState.with(BlockStateProperties.AGE_0_25, MathHelper.nextInt(rand, minAge, maxAge)), 2)
                        break
                    }
                    worldIn.setBlockState(pos, FBlocks.WEEPING_VINES_PLANT.defaultState, 2)
                }
                pos.move(Direction.DOWN)
            }
        }


    }
}