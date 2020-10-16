package thedarkcolour.futuremc.block.vine

import net.minecraft.block.Block
import net.minecraft.util.Direction
import thedarkcolour.futuremc.registry.FBlocks

class TwistingVinesBlock(properties: Properties) : AbstractPlantStemBlock(properties, Direction.UP, SHAPE, false, 0.1) {
    override val plant: Block
        get() = FBlocks.TWISTING_VINES_PLANT

    companion object {
        private val SHAPE = makeCuboidShape(4.0, 0.0, 4.0, 12.0, 15.0, 12.0)
    }
}