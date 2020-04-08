package thedarkcolour.futuremc.entity.bee.ai

import net.minecraft.entity.EntityCreature
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.ai.EntityAIHurtByTarget
import thedarkcolour.futuremc.entity.bee.BeeEntity

class RevengeAI(bee: BeeEntity) : EntityAIHurtByTarget(bee, true) {
    override fun setEntityAttackTarget(creatureIn: EntityCreature, entityLivingBaseIn: EntityLivingBase) {
        if (creatureIn is BeeEntity && taskOwner.canEntityBeSeen(entityLivingBaseIn) && creatureIn.setBeeAttacker(
                entityLivingBaseIn
            )
        ) {
            creatureIn.setAttackTarget(entityLivingBaseIn)
        }
    }
}