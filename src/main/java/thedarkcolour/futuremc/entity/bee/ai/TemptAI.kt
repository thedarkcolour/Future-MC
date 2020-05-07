package thedarkcolour.futuremc.entity.bee.ai

import net.minecraft.block.Block
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import thedarkcolour.futuremc.entity.bee.BeeEntity

class TemptAI(private val bee: BeeEntity) : EntityAIBase() {
    private var closestPlayer: EntityPlayer? = null
    private var coolDown: Int = 0

    init {
        mutexBits = 0x3
    }

    override fun shouldExecute(): Boolean {
        return if (coolDown > 0) {
            --coolDown
            false
        } else {
            closestPlayer = bee.world.getClosestPlayerToEntity(bee, 10.0)
            if (closestPlayer == null) {
                false
            } else {
                isTempting(closestPlayer!!.heldItemMainhand) || isTempting(closestPlayer!!.heldItemOffhand)
            }
        }
    }

    private fun isTempting(stack: ItemStack): Boolean {
        return bee.isFlowerValid(Block.getBlockFromItem(stack.item).getStateFromMeta(stack.metadata))
    }

    override fun shouldContinueExecuting(): Boolean {
        return shouldExecute()
    }

    override fun resetTask() {
        closestPlayer = null
        bee.navigator.clearPath()
        coolDown = 100
    }

    override fun updateTask() {
        bee.lookHelper.setLookPositionWithEntity(closestPlayer!!, 75.0f, 40.0f)
        if (bee.getDistanceSq(closestPlayer!!) < 6.25) {
            bee.navigator.clearPath()
        } else {
            bee.navigator.tryMoveToEntityLiving(closestPlayer!!, 1.25)
        }
    }
}