package thedarkcolour.futuremc.entity.bee.ai

import net.minecraft.util.math.BlockPos
import thedarkcolour.futuremc.entity.bee.BeeEntity
import thedarkcolour.futuremc.tile.BeeHiveTile

class LocateHiveAI(bee: BeeEntity) : PassiveAI(bee) {
    override fun canBeeStart(): Boolean {
        val a = bee.findHiveCooldown == 0
        val b = !bee.hasHive()
        val c = bee.canEnterHive()
        return a && b && c
    }

    override fun canBeeContinue() = false

    override fun startExecuting() {
        bee.findFlowerCooldown = 200
        val list = getNearbyFreeHives()

        if (list.isNotEmpty()) {
            for (pos in list) {
                if (!bee.goToHiveAI.possibleHives.contains(pos)) {
                    bee.hivePos = pos
                    return
                }
            }

            bee.goToHiveAI.possibleHives.clear()
            bee.hivePos = list[0]
        }
    }

    private fun getNearbyFreeHives(): List<BlockPos> {
        val worldIn = bee.world
        val list = arrayListOf<BlockPos>()

        bee.getBlockInRange(20) { pos ->
            val hive = worldIn.getTileEntity(pos)
            if (hive is BeeHiveTile && !hive.isFullOfBees()) {
                list.add(pos.toImmutable())
            }
            false
        }

        return if (list.isEmpty()) emptyList() else list
    }
}