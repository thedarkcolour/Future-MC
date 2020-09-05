package thedarkcolour.futuremc.block

import thedarkcolour.core.block.FBlock

class BlockSlab(properties: Properties) : FBlock(properties) {
    init {
        useNeighborBrightness = false
    }
}