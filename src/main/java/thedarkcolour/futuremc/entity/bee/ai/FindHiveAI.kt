package thedarkcolour.futuremc.entity.bee.ai

import net.minecraft.pathfinding.Path
import net.minecraft.util.math.BlockPos
import thedarkcolour.futuremc.block.BeeHiveBlock
import thedarkcolour.futuremc.entity.bee.BeeEntity
import java.util.*

class FindHiveAI(bee: BeeEntity) : FindBlockAI(bee) {
    val possibleHives = ArrayDeque<BlockPos>(3)
    var path: Path? = null

    override fun canBeeStart(): Boolean {
        val hivePos = bee.hivePos ?: return false
        return (bee.maximumHomeDistance != -1.0f) && bee.canEnterHive()
                && !isCloseEnough(hivePos) && bee.world.getBlockState(hivePos).block is BeeHiveBlock
    }

    override fun updateTask() {
        val hivePos = bee.hivePos

        if (hivePos != null) {
            ++searchingTicks
            if (searchingTicks > 600) {
                addPossibleHive(hivePos)
            } else if(bee.navigator.noPath()) {
                if (bee.isWithinDistance(hivePos, 16)) {
                    if (bee.isTooFar(hivePos)) {
                        reset()
                    } else {
                        startMovingTo(hivePos)
                    }
                } else {
                    val flag = startMovingToFar(hivePos)

                    if (!flag) {
                        makeChosenHivePossibleHive()
                    } else if (path != null && bee.navigator.path?.isSamePath(path!!) == true) {
                        reset()
                    } else {
                        path = bee.navigator.path
                    }
                }
            }
        }
    }

    private fun startMovingToFar(pos: BlockPos): Boolean {
        bee.navigator.tryMoveToXYZ(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 1.0)
        return bee.navigator.path != null // if all goes wrong, coremod the reachesTarget() method
    }

    private fun makeChosenHivePossibleHive() {
        val hivePos = bee.hivePos

        if (hivePos != null) {
            addPossibleHive(hivePos)
        }

        reset()
    }

    private fun reset() {
        bee.hivePos = null
        bee.findHiveCooldown = 200
    }

    private fun isCloseEnough(pos: BlockPos): Boolean {
        return if (bee.isWithinDistance(pos, 2)) {
            true
        } else {
            val path = bee.navigator.path
            path != null && path.target == pos && path.isFinished /* reachesTarget() */
        }
    }

    private fun addPossibleHive(pos: BlockPos) {
        // remove before adding to avoid illegal state exception
        while (possibleHives.size > 3) possibleHives.remove()
        possibleHives.add(pos)
    }
}