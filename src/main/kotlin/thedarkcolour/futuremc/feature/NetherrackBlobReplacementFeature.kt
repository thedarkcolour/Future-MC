package thedarkcolour.futuremc.feature

import com.mojang.datafixers.Dynamic
import net.minecraft.block.Block
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.GenerationSettings
import net.minecraft.world.gen.feature.Feature
import thedarkcolour.futuremc.util.clampAxis
import java.util.*
import java.util.function.Function

class NetherrackBlobReplacementFeature(configFactory: Function<Dynamic<*>, out NetherrackBlobReplacementConfig>) : Feature<NetherrackBlobReplacementConfig>(configFactory) {
    override fun place(
        worldIn: IWorld,
        generator: ChunkGenerator<out GenerationSettings>,
        rand: Random,
        pos: BlockPos,
        config: NetherrackBlobReplacementConfig
    ): Boolean {
        val block = config.target.block
        val pos1 = findTargetBlock(worldIn, BlockPos.Mutable(pos).clampAxis(Direction.Axis.Y, 1, generator.maxHeight - 1), block)

        if (pos1 == null) {
            return false
        } else {
            val i = config.getRandomSpread(rand)
            var f = false

            for (p in BlockPosUtil.iterateOutwards(pos1, i, i, i)) {
                if (p.manhattanDistance(pos1) > i) {
                    break
                }

                val state = worldIn.getBlockState(p)

                if (state.block == block) {
                    setBlockState(worldIn, p, config.state)
                    f = true
                }
            }
            return f
        }
    }

    private fun findTargetBlock(worldIn: IWorld, mutable: BlockPos.Mutable, block: Block): BlockPos? {
        while (mutable.y > 1) {
            val state = worldIn.getBlockState(mutable)
            if (state.block == block) {
                return mutable
            }
            mutable.move(Direction.DOWN)
        }
        return null
    }
}