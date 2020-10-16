package thedarkcolour.futuremc.block.vine

import net.minecraft.block.Block
import net.minecraft.util.Direction
import thedarkcolour.futuremc.registry.FBlocks

class WeepingVinesBlock(properties: Properties) : AbstractPlantStemBlock(properties, Direction.DOWN, SHAPE, false, 0.1) {
    override val plant: Block
        get() = FBlocks.WEEPING_VINES_PLANT

    companion object {
        private val SHAPE = makeCuboidShape(4.0, 9.0, 4.0, 12.0, 16.0, 12.0)
    }
}