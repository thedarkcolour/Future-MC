package thedarkcolour.futuremc.feature

import com.mojang.datafixers.Dynamic
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.GenerationSettings
import net.minecraft.world.gen.feature.BlockStateProvidingFeatureConfig
import net.minecraft.world.gen.feature.Feature
import thedarkcolour.futuremc.registry.FBlocks.NYLIUM
import java.util.*

class NetherForestVegetationFeature(configFactory: (Dynamic<*>) -> BlockStateProvidingFeatureConfig) : Feature<BlockStateProvidingFeatureConfig>(configFactory) {
    override fun place(worldIn: IWorld, generator: ChunkGenerator<out GenerationSettings>, rand: Random, posIn: BlockPos, config: BlockStateProvidingFeatureConfig): Boolean {
        var pos = posIn
        var block = worldIn.getBlockState(pos.down()).block

        while (!block.isIn(NYLIUM) && pos.y > 0) {
            pos = pos.down()
            block = worldIn.getBlockState(pos).block
        }

        val y = pos.y
        return if (y >= 1 && y + 1 < 256) {
            var i = 0

            for (k in 0..63) {
                val pos2 = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8))
                val blockState = config.field_227268_a_.getBlockState(rand, pos2)

                if (worldIn.isAirBlock(pos2) && pos2.y > 0 && blockState.isValidPosition(worldIn, pos2)) {
                    worldIn.setBlockState(pos2, blockState, 2)
                    ++i
                }
            }

            i > 0
        } else {
            false
        }
    }
}