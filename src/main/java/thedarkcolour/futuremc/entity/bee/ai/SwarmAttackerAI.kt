package thedarkcolour.futuremc.entity.bee.ai

import net.minecraft.entity.ai.EntityAINearestAttackableTarget
import net.minecraft.entity.player.EntityPlayer
import thedarkcolour.futuremc.entity.bee.BeeEntity

class SwarmAttackerAI(bee: BeeEntity) :
    EntityAINearestAttackableTarget<EntityPlayer>(bee, EntityPlayer::class.java, true) {
    override fun shouldExecute(): Boolean {
        return canSting() && super.shouldExecute()
    }

    override fun shouldContinueExecuting(): Boolean {
        return if (canSting() && taskOwner.attackTarget != null) {
            super.shouldContinueExecuting()
        } else {
            target = null
            false
        }
    }

    private fun canSting(): Boolean {
        val bee = taskOwner as BeeEntity
        return bee.isAngry() && !bee.hasStung()
    }
}