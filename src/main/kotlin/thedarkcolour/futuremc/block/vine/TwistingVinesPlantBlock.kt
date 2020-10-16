package thedarkcolour.futuremc.block.vine

import net.minecraft.util.Direction
import thedarkcolour.futuremc.registry.FBlocks

class TwistingVinesPlantBlock(properties: Properties) : AbstractPlantBlock(properties, Direction.UP, SHAPE, false) {
    override val stem: AbstractPlantStemBlock
        get() = FBlocks.TWISTING_VINES

    companion object {
        private val SHAPE = makeCuboidShape(4.0, 0.0, 4.0, 12.0, 16.0, 12.0)
    }
}