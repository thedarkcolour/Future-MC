package thedarkcolour.futuremc.entity.bee.ai

import net.minecraft.util.math.BlockPos
import thedarkcolour.futuremc.block.BeeHiveBlock
import thedarkcolour.futuremc.entity.bee.BeeEntity
import thedarkcolour.futuremc.tile.BeeHiveTile
import java.util.*

class FindHiveAI(bee: BeeEntity) : FindBlockAI(bee) {
    private val possibleHives = ArrayDeque<BlockPos>(3)

    override fun canBeeStart(): Boolean {
        val hivePos = bee.hivePos ?: return false
        return (bee.maximumHomeDistance != -1.0f) && shouldReturnToHive()
                && !isCloseEnough(hivePos) && bee.world.getBlockState(hivePos).block is BeeHiveBlock
    }

    private fun shouldReturnToHive(): Boolean {
        return if (bee.cannotEnterHiveTicks <= 0 && !bee.hasStung()) {
            val flag = bee.ticksSincePollination > 3600 || bee.world.isRaining || !bee.world.isDaytime || bee.hasPollen()
            return flag && !isHiveNearFire()
        } else {
            false
        }
    }

    private fun isHiveNearFire(): Boolean {
        return if (bee.hivePos == null) {
            false
        } else {
            val te = bee.world.getTileEntity(bee.hivePos!!)
            te is BeeHiveTile && te.isNearFire()
        }
    }

    private fun isCloseEnough(pos: BlockPos): Boolean {
        return if (bee.isWithinDistance(pos, 2)) {
            true
        } else {
            val path = bee.navigator.path

            if (path != null) {
                val target = path.target
                val targetPos = BlockPos(target.x, target.y, target.z)

                targetPos == pos && path.isFinished
            } else {
                false
            }
        }
    }

    override fun updateTask() {
        if (bee.hasHive()) {
            ++searchingTicks
            if (searchingTicks > 600) {
                addPossibleHive(bee.hivePos!!)
            }
        }
    }

    private fun addPossibleHive(pos: BlockPos) {
        possibleHives.add(pos)

        while (possibleHives.size > 3) {
            possibleHives.remove()
        }
    }
}