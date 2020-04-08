package thedarkcolour.futuremc.entity.bee.ai

import net.minecraft.util.math.BlockPos
import thedarkcolour.futuremc.entity.bee.BeeEntity
import thedarkcolour.futuremc.registry.FSounds

// TODO Finish
class PollinateAI(bee: BeeEntity) : PassiveAI(bee) {
    private var lastPollinationTicks = 0
    private var pollinationTicks = 0
    private var field_226495_f_ = false

    init {
        mutexBits = 0x1
    }

    override fun canBeeStart(): Boolean {
        when {
            bee.findFlowerCooldown > 0 -> return false
            bee.hasPollen() -> return false
            bee.world.isRaining -> return false
            bee.rng.nextFloat() < 0.7f -> return false

            else -> {
                val flower = getFlower()

                return if (flower != null) {
                    bee.navigator.tryMoveToXYZ(flower.x + 0.5, flower.y + 0.5, flower.z + 0.5, 1.2)
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun getFlower(): BlockPos? {
        return bee.getBlockInRange(2) { pos ->
            bee.isFlowerValid(bee.world.getBlockState(pos))
        }
    }

    override fun canBeeContinue(): Boolean {
        return if (!field_226495_f_) {
            false
        } else if (bee.hasFlower()) {
            false
        } else if (bee.world.isRaining) {
            false
        } else if (completedPollination()) {
            bee.rng.nextFloat() < 0.2f
        } else if (bee.ticksExisted % 20 == 0 && !bee.doesFlowerExist(bee.flowerPos!!)) {
            bee.flowerPos = null
            false
        } else {
            true
        }
    }

    private fun completedPollination(): Boolean {
        return pollinationTicks > 400
    }

    override fun startExecuting() {
        bee.setPollinating(true)
        pollinationTicks = 0
        lastPollinationTicks = 0
    }

    override fun resetTask() {
        bee.setPollinating(false)
        if (completedPollination()) {
            bee.setHasNectar(true)
        }
    }

    override fun updateTask() {
        ++pollinationTicks
        if (bee.rng.nextFloat() < 0.05f && pollinationTicks > lastPollinationTicks + 60) {
            lastPollinationTicks = pollinationTicks
            bee.playSound(FSounds.BEE_POLLINATE, 1.0f, 1.0f)
        }
    }
}