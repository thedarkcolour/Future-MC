package thedarkcolour.futuremc.entity.bee.ai

import net.minecraft.entity.ai.RandomPositionGenerator
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import thedarkcolour.futuremc.entity.bee.BeeEntity
import kotlin.math.abs

abstract class GoToBlockAI(bee: BeeEntity) : PassiveAI(bee) {
    protected var searchingTicks = bee.rng.nextInt(10)

    init {
        mutexBits = 0x1
    }

    fun isTooFar(pos: BlockPos): Boolean {
        return !bee.isWithinDistance(pos, 48)
    }

    fun startMovingTo(pos: BlockPos) {
        var targetPos = Vec3d(pos)
        val y: Int
        val beePos = BlockPos(bee)
        val verticalDistance = targetPos.y - beePos.y
        y = if (verticalDistance > 2) {
            4
        } else {
            -4
        }

        var xz = 6
        val targetDistance = manhattanDistance(beePos, pos)
        if (targetDistance < 15) {
            xz = targetDistance / 2
        }

        targetPos = findGroundTargetTowards(bee, xz, y, targetPos) ?: return
        bee.navigator.tryMoveToXYZ(targetPos.x, targetPos.y, targetPos.z, 1.0)
    }

    private fun manhattanDistance(pos: BlockPos, other: BlockPos): Int {
        val f = abs(other.x - pos.x)
        val f1 = abs(other.y - pos.y)
        val f2 = abs(other.z - pos.z)

        return f + f1 + f2
    }

    private fun findGroundTargetTowards(bee: BeeEntity, xz: Int, y: Int, vector: Vec3d): Vec3d? {
        val vec3d = vector.subtract(bee.posX, bee.posY, bee.posZ)
        return RandomPositionGenerator.generateRandomPos(bee, xz, y, vec3d, false)
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