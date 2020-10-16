package thedarkcolour.futuremc.registry

import net.minecraft.entity.ai.brain.sensor.SensorType
import net.minecraftforge.registries.IForgeRegistry
import thedarkcolour.futuremc.entity.sensor.HoglinSpecificSensor

object FSensors {
    val HOGLIN_SPECIFIC_SENSOR = SensorType(::HoglinSpecificSensor)

    fun registerSensors(sensors: IForgeRegistry<SensorType<*>>) {

    }
}