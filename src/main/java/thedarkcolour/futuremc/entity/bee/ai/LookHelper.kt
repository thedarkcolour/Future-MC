package thedarkcolour.futuremc.entity.bee.ai

import net.minecraft.entity.ai.EntityLookHelper
import thedarkcolour.futuremc.entity.bee.BeeEntity

class LookHelper(private val bee: BeeEntity) : EntityLookHelper(bee) {
    override fun onUpdateLook() {
        if (!bee.isAngry()) {
            super.onUpdateLook()
        }
    }
}