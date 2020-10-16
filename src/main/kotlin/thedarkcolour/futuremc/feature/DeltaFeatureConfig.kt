package thedarkcolour.futuremc.feature

import com.google.common.collect.ImmutableMap
import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.types.DynamicOps
import net.minecraft.block.BlockState
import net.minecraft.world.gen.feature.IFeatureConfig
import thedarkcolour.futuremc.util.deserializeBlockState

class DeltaFeatureConfig(
    val contents: BlockState,
    val rim: BlockState,
    val minRadius: Int = 0,
    val maxRadius: Int = 0,
    val maxRim: Int = 0,
) : IFeatureConfig {

    constructor(serialized: Dynamic<*>) : this(
        contents = serialized["contents"].deserializeBlockState(),
        rim = serialized["rim"].deserializeBlockState(),
        minRadius = serialized["minRadius"].asInt(0),
        maxRadius = serialized["maxRadius"].asInt(0),
        maxRim = serialized["maxRim"].asInt(0),
    )

    override fun <T : Any?> serialize(ops: DynamicOps<T>): Dynamic<T> {
        return Dynamic(ops, ops.createMap(ImmutableMap.of(
            ops.createString("contents"), BlockState.serialize(ops, contents).value,
            ops.createString("rim"), BlockState.serialize(ops, rim).value,
            ops.createString("minRadius"), ops.createInt(minRadius),
            ops.createString("maxRadius"), ops.createInt(maxRadius),
            ops.createString("maxRim"), ops.createInt(maxRim),
        )))
    }
}