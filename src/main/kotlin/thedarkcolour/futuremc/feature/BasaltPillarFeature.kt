package thedarkcolour.futuremc.feature

import com.mojang.datafixers.Dynamic
import net.minecraft.util.Direction
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

class BasaltPillarFeature(configFactory: (Dynamic<*>) -> NoFeatureConfig) : Feature<NoFeatureConfig>(configFactory) {
    override fun place(worldIn: IWorld, generator: ChunkGenerator<out GenerationSettings>, rand: Random, pos: BlockPos, config: NoFeatureConfig): Boolean {
        if (worldIn.isAirBlock(pos) && !worldIn.isAirBlock(pos.up())) {
            val mutable = BlockPos.Mutable(pos)
            val mutable2 = BlockPos.Mutable(pos)
            var bl = true
            var bl2 = true
            var bl3 = true
            var bl4 = true

            while (worldIn.isAirBlock(mutable)) {
                if (World.isOutsideBuildHeight(mutable)) {
                    return true
                }
                worldIn.setBlockState(mutable, FBlocks.BASALT.defaultState, 2)
                bl = bl && stopOrPlaceBasalt(worldIn, rand, mutable2.setPos(mutable).move(Direction.NORTH))
                bl2 = bl2 && stopOrPlaceBasalt(worldIn, rand, mutable2.setPos(mutable).move(Direction.SOUTH))
                bl3 = bl3 && stopOrPlaceBasalt(worldIn, rand, mutable2.setPos(mutable).move(Direction.WEST))
                bl4 = bl4 && stopOrPlaceBasalt(worldIn, rand, mutable2.setPos(mutable).move(Direction.EAST))
                mutable.move(Direction.DOWN)
            }

            mutable.move(Direction.UP)
            tryPlaceBasalt(worldIn, rand, mutable2.setPos(mutable).move(Direction.NORTH))
            tryPlaceBasalt(worldIn, rand, mutable2.setPos(mutable).move(Direction.SOUTH))
            tryPlaceBasalt(worldIn, rand, mutable2.setPos(mutable).move(Direction.WEST))
            tryPlaceBasalt(worldIn, rand, mutable2.setPos(mutable).move(Direction.EAST))
            val mutable3 = BlockPos.Mutable()

            for (i in -3..3) {
                for (j in -3..3) {
                    val k = MathHelper.abs(i) * MathHelper.abs(j)
                    if (rand.nextInt(10) < 10 - k) {
                        mutable3.setPos(mutable).move(i, 0, j)
                        var l = 3
                        while (worldIn.isAirBlock(mutable2.setPos(mutable3).move(Direction.DOWN))) {
                            mutable3.move(Direction.DOWN)
                            --l
                            if (l <= 0) {
                                break
                            }
                        }
                        if (!worldIn.isAirBlock(mutable2.setPos(mutable3).move(Direction.DOWN))) {
                            worldIn.setBlockState(mutable3, FBlocks.BASALT.defaultState, 2)
                        }
                    }
                }
            }

            return true
        } else return false
    }

    private fun stopOrPlaceBasalt(worldIn: IWorld, rand: Random, pos: BlockPos.Mutable): Boolean {
        return if (rand.nextInt(10) != 0) {
            worldIn.setBlockState(pos, FBlocks.BASALT.defaultState, 2)
            true
        } else {
            false
        }
    }

    private fun tryPlaceBasalt(worldIn: IWorld, rand: Random, pos: BlockPos.Mutable) {
        if (rand.nextBoolean()) {
            worldIn.setBlockState(pos, FBlocks.BASALT.defaultState, 2)
        }
    }
}