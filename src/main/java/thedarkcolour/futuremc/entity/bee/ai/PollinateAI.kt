package thedarkcolour.futuremc.entity.bee.ai

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import thedarkcolour.futuremc.entity.bee.BeeEntity
import thedarkcolour.futuremc.registry.FSounds

class PollinateAI(bee: BeeEntity) : PassiveAI(bee) {
    private var lastPollinationTicks = 0
    private var pollinationTicks = 0
    private var nextTarget: Vec3d? = null
    private var ticks = 0
    var isRunning = false

    init {
        mutexBits = 0x1
    }

    override fun canBeeStart(): Boolean {
        when {
            bee.findFlowerCooldown > 0 -> return false
            bee.hasNectar() -> return false
            bee.world.isRaining -> return false
            bee.rng.nextFloat() < 0.7f -> return false

            else -> {
                val flower = getFlower()

                return if (flower != null) {
                    bee.flowerPos = flower
                    bee.navigator.tryMoveToXYZ(flower.x + 0.5, flower.y + 0.5, flower.z + 0.5, 1.2)
                    true
                } else {
                    false
                }
            }
        }
    }

    override fun canBeeContinue(): Boolean {
        return if (!isRunning) {
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
        bee.resetPollinationTicks()
        pollinationTicks = 0
        lastPollinationTicks = 0
        ticks = 0
        isRunning = true
    }

    override fun resetTask() {
        if (completedPollination()) {
            bee.setHasNectar(true)
        }

        isRunning = false
        bee.navigator.clearPath()
    }

    override fun updateTask() {
        if (++ticks > 600) {
            bee.flowerPos = null
        } else {
            val v3c = Vec3d(bee.flowerPos!!).add(0.5, 0.6, 0.5)

            if (v3c.distanceTo(bee.positionVector) > 1.0) {
                nextTarget = v3c
                moveToNextTarget()
            } else {
                if (nextTarget == null) {
                    nextTarget = v3c
                }

                val flag = bee.positionVector.distanceTo(v3c) <= 0.1
                var flag1 = true
                if (!flag && ticks > 600) {
                    bee.flowerPos = null
                } else {
                    if (flag) {
                        if (bee.rng.nextInt(100) == 0) {
                            nextTarget = v3c.add(getRandomOffset(), 0.0, getRandomOffset())
                            bee.navigator.clearPath()
                        } else {
                            flag1 = false
                        }

                        bee.lookHelper.setLookPosition(v3c.x, v3c.y, v3c.z, 10.0f, 40.0f)
                    }

                    if (flag1) {
                        moveToNextTarget()
                    }

                    ++pollinationTicks
                    if (bee.rng.nextFloat() < 0.05f && pollinationTicks > lastPollinationTicks + 60) {
                        lastPollinationTicks = pollinationTicks
                        bee.playSound(FSounds.BEE_POLLINATE, 1.0f, 1.0f)
                    }
                }
            }
        }
    }

    private fun moveToNextTarget() {
        val target = nextTarget ?: return // better safe than sorry
        bee.moveHelper.setMoveTo(target.x, target.y, target.z, 0.3499999940395355)
    }

    private fun getRandomOffset(): Double {
        return ((bee.rng.nextFloat() * 2.0f - 1.0f) * 0.33333334f).toDouble()
    }

    private fun getFlower(): BlockPos? {
        return bee.getBlockInRange(5) { pos ->
            bee.isFlowerValid(bee.world.getBlockState(pos))
        }
    }
}