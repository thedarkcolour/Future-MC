package thedarkcolour.futuremc.surfacebuilder

import com.mojang.datafixers.Dynamic
import net.minecraft.block.Blocks
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig
import thedarkcolour.futuremc.registry.FBlocks
import java.util.function.Function

class SoulSandValleySurfaceBuilder(configFactory: Function<Dynamic<*>, out SurfaceBuilderConfig>) : AbstractNetherSurfaceBuilder(configFactory) {
    override fun getMiddleBlocks() = blocks
    override fun getBottomBlocks() = blocks

    private companion object {
        private val blocks = listOf(Blocks.SOUL_SAND.defaultState, FBlocks.SOUL_SOIL.defaultState)
    }
}