package thedarkcolour.futuremc.compat.plants

import shadows.plants2.init.ModRegistry
import thedarkcolour.futuremc.api.BeePollinationHandler
import thedarkcolour.futuremc.api.BeePollinationHandlerJVM
import thedarkcolour.futuremc.api.BeePollinationTargetsJVM

object PlantsCompat {
    fun registerPollinationTargets() {
        @Suppress("UsePropertyAccessSyntax")
        val a = arrayOf(ModRegistry.PLANT_0, ModRegistry.PLANT_1, ModRegistry.PLANT_2, ModRegistry.PLANT_3, ModRegistry.PLANT_4, ModRegistry.PLANT_5, ModRegistry.PLANT_6, ModRegistry.DESERT_0, ModRegistry.DESERT_1).flatMap { block ->
            block.getBlockState().validStates
        }

        for (b in a) {
            BeePollinationTargetsJVM.addPollinationTarget(b)
        }

        BeePollinationHandlerJVM.registerHandler(ModRegistry.CROP_0, BeePollinationHandler.blockCropsHandler)
        BeePollinationHandlerJVM.registerHandler(ModRegistry.CROP_0, BeePollinationHandler.blockCropsHandler)
    }
}