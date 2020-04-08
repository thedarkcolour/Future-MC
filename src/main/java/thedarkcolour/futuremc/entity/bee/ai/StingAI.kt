package thedarkcolour.futuremc.entity.bee.ai

import net.minecraft.entity.ai.EntityAIAttackMelee
import thedarkcolour.futuremc.entity.bee.BeeEntity

class StingAI(private val bee: BeeEntity, speedIn: Double, useLongMemory: Boolean) : EntityAIAttackMelee(bee, speedIn, useLongMemory) {
    override fun shouldExecute(): Boolean {
        return super.shouldExecute() && bee.isAngry() && !bee.hasStung()
    }

    override fun shouldContinueExecuting(): Boolean {
        return shouldExecute()
    }
}