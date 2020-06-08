package thedarkcolour.futuremc.compat.actuallyadditions

import de.ellpeck.actuallyadditions.mod.blocks.InitBlocks
import thedarkcolour.futuremc.api.BeePollinationHandler
import thedarkcolour.futuremc.api.BeePollinationTargetsJVM

object ActuallyAdditionsCompat {
    fun registerPollinationTargets() {
        BeePollinationTargetsJVM.addPollinationTarget(InitBlocks.blockBlackLotus.defaultState)
    }

    fun registerPollinationHandlers() {
        BeePollinationHandler.registerHandler(InitBlocks.blockRice, BeePollinationHandler.blockCropsHandler)
        BeePollinationHandler.registerHandler(InitBlocks.blockCanola, BeePollinationHandler.blockCropsHandler)
        BeePollinationHandler.registerHandler(InitBlocks.blockFlax, BeePollinationHandler.blockCropsHandler)
        BeePollinationHandler.registerHandler(InitBlocks.blockCoffee, BeePollinationHandler.blockCropsHandler)
    }
}