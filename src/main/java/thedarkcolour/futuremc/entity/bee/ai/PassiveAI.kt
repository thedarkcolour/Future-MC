package thedarkcolour.futuremc.entity.bee.ai

import net.minecraft.entity.ai.EntityAIBase
import thedarkcolour.futuremc.entity.bee.BeeEntity

abstract class PassiveAI(protected val bee: BeeEntity) : EntityAIBase() {
    abstract fun canBeeStart(): Boolean

    abstract fun canBeeContinue(): Boolean

    override fun shouldExecute(): Boolean {
        return canBeeStart() && !bee.isAngry()
    }

    override fun shouldContinueExecuting(): Boolean {
        return canBeeContinue() && !bee.isAngry()
    }
}