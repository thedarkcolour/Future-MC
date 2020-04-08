package thedarkcolour.futuremc.entity.bee.ai

import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.entity.ai.RandomPositionGenerator
import thedarkcolour.futuremc.entity.bee.BeeEntity

class WanderAI(private val bee: BeeEntity) : EntityAIBase() {
    init {
        mutexBits = 0x1
    }

    override fun shouldExecute(): Boolean {
        return bee.navigator.noPath() && bee.rng.nextInt(10) == 0 || !bee.hasHive()
    }

    override fun shouldContinueExecuting(): Boolean {
        return !bee.navigator.noPath()
    }

    override fun startExecuting() {
        val vec3d = RandomPositionGenerator.findRandomTarget(bee, 8, 7)
        if (vec3d != null) {
            bee.navigator.tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1.0)
        }
    }

    override fun resetTask() {
        bee.navigator.setPath(null, 1.0)
    }
}