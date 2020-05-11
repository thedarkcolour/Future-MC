package thedarkcolour.futuremc.entity.bee.ai

import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.entity.ai.RandomPositionGenerator
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import thedarkcolour.core.util.isInRange
import thedarkcolour.futuremc.entity.bee.BeeEntity
import java.util.function.ToDoubleFunction
import kotlin.math.PI

class WanderAI(private val bee: BeeEntity) : EntityAIBase() {
    init {
        mutexBits = 0x1
    }

    override fun shouldExecute(): Boolean {
        return bee.navigator.noPath() && bee.rng.nextInt(10) == 0
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

    private fun getRandomLocation(): Vec3d {
        val vec3d = if (bee.isHiveValid() && !bee.isWithinDistance(bee.hivePos, 40)) {
            val vec3d1 = Vec3d(bee.hivePos!!)
            vec3d1.subtract(bee.positionVector).normalize()
        } else {
            bee.getLook(0.0f)
        }

        val vec3d2 = findAirTarget(bee, 8, 7, vec3d, PI / 2.0f, 2, 1)
        return vec3d2 ?: findGroundTarget(bee, 8, 4, -2, vec3d, PI / 2.0f)
    }

    private fun findAirTarget(bee: BeeEntity, i: Int, i1: Int, vec3d: Vec3d, d: Double, i2: Int, i3: Int): Vec3d? {
        return findTarget(bee, i, i1, 0, vec3d, false, d, ToDoubleFunction(bee::getBlockPathWeight), true, i2, i3, true)
    }

    private fun findGroundTarget(bee: BeeEntity, i: Int, i1: Int, i2: Int, vec3d: Vec3d, d: Double): Vec3d {
        TODO("not implemented")
    }

    private fun findTarget(
        bee: BeeEntity,
        i: Int,
        i1: Int,
        i4: Int,
        vec3d: Vec3d,
        b: Boolean,
        d: Double,
        kFunction1: ToDoubleFunction<BlockPos>,
        b1: Boolean,
        i2: Int,
        i3: Int,
        b2: Boolean
    ): Vec3d? {
        val navigator = bee.navigator
        val rand = bee.rng
        val flag = if (bee.maximumHomeDistance != -1.0f) {
            // see Extensions.kt
            bee.homePosition.isInRange()
        } else {
            false
        }

    }
}