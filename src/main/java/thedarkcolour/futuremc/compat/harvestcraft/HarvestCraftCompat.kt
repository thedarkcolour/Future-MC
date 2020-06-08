package thedarkcolour.futuremc.compat.harvestcraft

import com.pam.harvestcraft.blocks.CropRegistry
import com.pam.harvestcraft.blocks.growables.BlockPamCrop
import net.minecraftforge.common.util.Constants
import thedarkcolour.futuremc.api.BeePollinationHandler

object HarvestCraftCompat {
    fun registerPollinationHandlers() {
        val handler = BeePollinationHandler.create { worldIn, pos, state, bee ->
            if (worldIn.getBlockState(pos).getValue(BlockPamCrop.CROPS_AGE) < 3) {
                bee.world.playEvent(Constants.WorldEvents.BONEMEAL_PARTICLES, pos, 0)
                bee.world.setBlockState(pos, state.withProperty(BlockPamCrop.CROPS_AGE, state.getValue(BlockPamCrop.CROPS_AGE) + 1))
                true
            } else {
                false
            }
        }
        for (crop in CropRegistry.cropNames) {
            val block = CropRegistry.getCrop(crop)

            BeePollinationHandler.registerHandler(block, handler)
        }
    }
}