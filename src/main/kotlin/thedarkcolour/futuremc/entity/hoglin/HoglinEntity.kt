package thedarkcolour.futuremc.entity.hoglin

import net.minecraft.entity.AgeableEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.EntitySenses
import net.minecraft.entity.ai.brain.sensor.SensorType
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.passive.AnimalEntity
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.world.World

class HoglinEntity(type: EntityType<HoglinEntity>, worldIn: World) : AnimalEntity(type, worldIn), IMob {
    private var movementCooldownTicks = 0
    private var timeInOverworld = 0
    private var cannotBeHunted = false

    override fun createChild(ageable: AgeableEntity): AgeableEntity? {
        TODO("not implemented")
    }

    companion object {
        private val BABY = EntityDataManager.createKey(AgeableEntity::class.java, DataSerializers.BOOLEAN)
        private val SENSOR_TYPES = listOf(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS)
    }
}