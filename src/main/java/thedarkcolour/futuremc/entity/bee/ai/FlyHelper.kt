package thedarkcolour.futuremc.entity.bee.ai

import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.EntityMoveHelper
import net.minecraft.util.math.MathHelper
import thedarkcolour.futuremc.entity.bee.BeeEntity
import kotlin.math.atan2
import kotlin.math.sqrt

class FlyHelper(bee: BeeEntity) : EntityMoveHelper(bee) {
    override fun onUpdateMoveHelper() {
        if (action == Action.MOVE_TO) {
            action = Action.WAIT
            entity.setNoGravity(true)
            val d0 = posX - entity.posX
            val d1 = posY - entity.posY
            val d2 = posZ - entity.posZ
            val d3 = d0 * d0 + d1 * d1 + d2 * d2

            if (d3 < 2.500000277905201E-7) {
                entity.setMoveVertical(0.0f)
                entity.setMoveForward(0.0f)
                return
            }

            val f0 = (atan2(d2, d0) * 57.2957763671875).toFloat() - 90.0f
            entity.rotationYaw = limitAngle(entity.rotationYaw, f0, 90.0f)
            val f1 = if (entity.onGround) {
                (speed * entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).attributeValue).toFloat()
            } else {
                (speed * entity.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).attributeValue).toFloat()
            }

            entity.aiMoveSpeed = f1
            val d4 = sqrt(d0 * d0 + d2 * d2)
            val f2 = (-(MathHelper.atan2(d1, d4) * 57.2957763671875)).toFloat()
            entity.rotationPitch = limitAngle(entity.rotationPitch, f2, 20f)
            entity.setMoveVertical(if (d1 > 0.0) f1 else -f1)
        } else {
            entity.setMoveVertical(0.0f)
            entity.setMoveForward(0.0f)
        }
    }
}