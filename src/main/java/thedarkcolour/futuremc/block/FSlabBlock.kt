package thedarkcolour.futuremc.block

import thedarkcolour.core.block.FBlock

class FSlabBlock(properties: Properties) : FBlock(properties) {
    override fun canSilkHarvest(): Boolean {
        return false
    }
}