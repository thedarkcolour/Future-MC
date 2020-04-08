package thedarkcolour.futuremc.entity.bee.ai

import thedarkcolour.futuremc.entity.bee.BeeEntity
import thedarkcolour.futuremc.tile.BeeHiveTile

class EnterHiveAI(bee: BeeEntity) : PassiveAI(bee) {
    override fun canBeeStart(): Boolean {
        val hivePos = bee.hivePos ?: return false

        if (bee.shouldReturnToHive() && bee.isWithinDistance(hivePos, 2)) {
            val te = bee.world.getTileEntity(hivePos)

            if (te is BeeHiveTile) {
                if (!te.isFullOfBees()) {
                    return true
                }

                bee.hivePos = null
            }
        }
        return false
    }

    override fun canBeeContinue() = false

    override fun startExecuting() {
        val te = bee.world.getTileEntity(bee.hivePos!!)
        if (te is BeeHiveTile) {
            te.tryEnterHive(bee, bee.hasPollen())
        }
    }
}