package thedarkcolour.futuremc.entity.bee.ai

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import thedarkcolour.core.util.manhattanDistance
import thedarkcolour.futuremc.entity.bee.BeeEntity

abstract class GoToBlockAI(bee: BeeEntity) : PassiveAI(bee) {
    protected var searchingTicks = bee.rng.nextInt(10)

    init {
        mutexBits = 0x1
    }

    fun isTooFar(pos: BlockPos): Boolean {
        return !bee.isWithinDistance(pos, 48)
    }

    fun startMovingTo(pos: BlockPos) {
        val vec3d = Vec3d(pos)
        var i = 0
        val beePos = BlockPos(bee)
        val j = vec3d.y.toInt() - beePos.y

        if (j > 2) {
            i = 4
        } else if (j < -2) {
            i = -4
        }

        var k = 6
        var l = 8
        // see Extensions.kt
        val i1 = beePos.manhattanDistance(pos)

        if (i1 < 15) {
            k = i1 / 2
            l = i1 / 2
        }

        val vec3d1 = WanderAI.findGroundTargetTowards(bee, k, l, i, vec3d, (Math.PI.toFloat() / 10f).toDouble())

        if (vec3d1 != null) {
            bee.navigator.tryMoveToXYZ(vec3d1.x, vec3d1.y, vec3d1.z, 1.0)
        }
    }

    override fun startExecuting() {
        searchingTicks = 0
    }

    override fun canBeeContinue(): Boolean {
        return canBeeStart()
    }

    override fun resetTask() {
        searchingTicks = 0
        bee.navigator.clearPath()
    }
}