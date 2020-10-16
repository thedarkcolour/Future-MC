package thedarkcolour.futuremc.block.vine

import net.minecraft.util.Direction
import thedarkcolour.futuremc.registry.FBlocks

class WeepingVinesPlantBlock(properties: Properties) : AbstractPlantBlock(properties, Direction.DOWN, SHAPE, false) {
    override val stem: AbstractPlantStemBlock
        get() = FBlocks.WEEPING_VINES

    companion object {
        private val SHAPE = makeCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0)
    }
}