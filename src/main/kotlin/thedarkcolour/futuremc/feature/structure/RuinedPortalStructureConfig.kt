package thedarkcolour.futuremc.feature.structure

import com.google.common.collect.ImmutableMap
import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.types.DynamicOps
import net.minecraft.world.gen.feature.IFeatureConfig

class RuinedPortalStructureConfig(val type: RuinedPortalStructure.Type) : IFeatureConfig {
    constructor(serialized: Dynamic<*>) : this(
        RuinedPortalStructure.Type.valueOf(serialized["portal_type"].asString(""))
    )

    override fun <T : Any?> serialize(ops: DynamicOps<T>): Dynamic<T> {
        return Dynamic(ops, ops.createMap(ImmutableMap.of(
            ops.createString("portal_type"), ops.createString(type.name)
        )))
    }
}