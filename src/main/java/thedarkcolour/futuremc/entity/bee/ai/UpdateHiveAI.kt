package thedarkcolour.futuremc.entity.bee.ai

import net.minecraft.util.math.BlockPos
import thedarkcolour.futuremc.entity.bee.BeeEntity
import thedarkcolour.futuremc.tile.BeeHiveTile

class UpdateHiveAI(bee: BeeEntity) : PassiveAI(bee) {
    override fun canBeeStart(): Boolean {
        return bee.findHiveCooldown == 0 && !bee.hasHive() && bee.canEnterHive()
    }

    override fun canBeeContinue() = false

    override fun startExecuting() {
        bee.findFlowerCooldown = 200
        val list = getNearbyFreeHives()

        if (list.isNotEmpty()) {
            for (pos in list) {
                if (!bee.findHiveAI.possibleHives.contains(pos)) {
                    bee.hivePos = pos
                    return
                }
            }

            bee.findHiveAI.possibleHives.clear()
            bee.hivePos = list[0]
        }
    }

    private fun getNearbyFreeHives(): List<BlockPos> {
        val pos = BlockPos(bee)
        val worldIn = bee.world
        val list = arrayListOf<BlockPos>()

        for (pos1 in BlockPos.getAllInBoxMutable(pos.add(-20, -20, -20), pos.add(20, 20, 20))) {
            val hive = worldIn.getTileEntity(pos1)
            if (hive is BeeHiveTile && !hive.isFullOfBees())
                list.add(pos1.toImmutable())
        }

        return if (list.isEmpty()) emptyList() else list
    }
}