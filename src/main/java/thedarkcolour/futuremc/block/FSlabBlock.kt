package thedarkcolour.futuremc.block

import thedarkcolour.core.block.FBlock

class FSlabBlock(properties: Properties) : FBlock(properties) {
    init {
        useNeighborBrightness = false
    }

    override fun canSilkHarvest(): Boolean {
        return false
    }
}