package thedarkcolour.futuremc.entity.bee.ai

import thedarkcolour.futuremc.entity.bee.BeeEntity

class FindFlowerAI(bee: BeeEntity) : FindBlockAI(bee) {
    override fun canBeeStart(): Boolean {
        return bee.hasFlower() && (bee.maximumHomeDistance != -1.0f) && shouldMoveToFlower()
                && bee.doesFlowerExist(bee.flowerPos!!) && bee.isWithinDistance(bee.flowerPos!!, 2)
    }

    private fun shouldMoveToFlower(): Boolean {
        return bee.ticksSincePollination > 2400
    }

    override fun updateTask() {
        val flowerPos = bee.flowerPos

        if (flowerPos != null) {
            ++searchingTicks
            if (searchingTicks > 600) {
                bee.flowerPos = null
            } else if (bee.navigator.noPath()) {
                if (isTooFar(flowerPos)) {
                    bee.flowerPos = null
                } else {
                    startMovingTo(flowerPos)
                }
            }
        }
    }
}