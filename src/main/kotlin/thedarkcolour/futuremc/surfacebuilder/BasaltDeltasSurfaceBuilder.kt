package thedarkcolour.futuremc.surfacebuilder

import com.mojang.datafixers.Dynamic
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig
import thedarkcolour.futuremc.registry.FBlocks
import java.util.function.Function

class BasaltDeltasSurfaceBuilder(configFactory: Function<Dynamic<*>, out SurfaceBuilderConfig>) : AbstractNetherSurfaceBuilder(configFactory) {
    override fun getMiddleBlocks() = middleBlocks
    override fun getBottomBlocks() = bottomBlocks

    private companion object {
        private val BASALT = FBlocks.BASALT.defaultState
        private val middleBlocks = listOf(BASALT, FBlocks.BLACKSTONE.defaultState)
        private val bottomBlocks = listOf(BASALT)
    }
}