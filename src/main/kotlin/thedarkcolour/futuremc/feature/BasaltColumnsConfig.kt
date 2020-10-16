package thedarkcolour.futuremc.feature

import com.google.common.collect.ImmutableMap
import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.types.DynamicOps
import net.minecraft.world.gen.feature.IFeatureConfig

class BasaltColumnsConfig(
    val minimumReach: Int,
    val maximumReach: Int,
    val minimumHeight: Int,
    val maximumHeight: Int,
) : IFeatureConfig {
    constructor(serialized: Dynamic<*>) : this(
        serialized["minimum_reach"].asInt(0),
        serialized["maximum_reach"].asInt(0),
        serialized["minimum_height"].asInt(0),
        serialized["maximum_height"].asInt(0),
    )

    override fun <T : Any?> serialize(ops: DynamicOps<T>): Dynamic<T> {
        return Dynamic(ops, ops.createMap(ImmutableMap.of(
            ops.createString("minimum_reach"), ops.createInt(minimumReach),
            ops.createString("maximum_reach"), ops.createInt(maximumReach),
            ops.createString("minimum_height"), ops.createInt(minimumHeight),
            ops.createString("maximum_height"), ops.createInt(maximumHeight),
        )))
    }
}