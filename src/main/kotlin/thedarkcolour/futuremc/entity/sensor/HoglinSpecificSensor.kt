package thedarkcolour.futuremc.entity.sensor

import com.google.common.collect.AbstractIterator
import net.minecraft.entity.ai.brain.memory.MemoryModuleType
import net.minecraft.entity.ai.brain.sensor.Sensor
import net.minecraft.util.math.BlockPos
import net.minecraft.world.server.ServerWorld
import thedarkcolour.futuremc.entity.hoglin.HoglinEntity
import thedarkcolour.futuremc.entity.piglin.PiglinEntity
import thedarkcolour.futuremc.registry.FMemoryModules.NEAREST_REPELLENT
import thedarkcolour.futuremc.registry.FMemoryModules.NEAREST_VISIBLE_ADULT_HOGLINS
import thedarkcolour.futuremc.registry.FMemoryModules.NEAREST_VISIBLE_ADULT_PIGLIN
import thedarkcolour.futuremc.registry.FMemoryModules.VISIBLE_ADULT_HOGLIN_COUNT
import thedarkcolour.futuremc.registry.FMemoryModules.VISIBLE_ADULT_PIGLIN_COUNT
import kotlin.math.abs

class HoglinSpecificSensor(senseInterval: Int = 20) : Sensor<HoglinEntity>(senseInterval) {
    override fun getUsedMemories(): Set<MemoryModuleType<*>> {
        return HoglinSpecificSensor.usedMemories
    }

    override fun update(worldIn: ServerWorld, entityIn: HoglinEntity) {
        val brain = entityIn.brain
        brain.setMemory(NEAREST_REPELLENT, findNearestWarpedFungus(worldIn, entityIn))
        var hoglin: PiglinEntity? = null
        var i = 0
        val list = arrayListOf<HoglinEntity>()
        val list1 = brain.getMemory(MemoryModuleType.VISIBLE_MOBS).orElse(arrayListOf())

        for (livingEntity in list1) {
            if (livingEntity is PiglinEntity && !livingEntity.isChild) {
                ++i
                if (hoglin == null) {
                    hoglin = livingEntity
                }
            }

            if (livingEntity is HoglinEntity && !livingEntity.isChild) {
                list.add(livingEntity)
            }
        }
    }

    private fun findNearestWarpedFungus(worldIn: ServerWorld, entityIn: HoglinEntity): BlockPos {
        TODO()
    }

    companion object {
        private val usedMemories = setOf(MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.NEAREST_HOSTILE, NEAREST_REPELLENT, NEAREST_VISIBLE_ADULT_PIGLIN, NEAREST_VISIBLE_ADULT_HOGLINS, VISIBLE_ADULT_PIGLIN_COUNT, VISIBLE_ADULT_HOGLIN_COUNT)
    }
}