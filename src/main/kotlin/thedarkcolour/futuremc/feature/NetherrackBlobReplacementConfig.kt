package thedarkcolour.futuremc.feature

import com.google.common.collect.ImmutableMap
import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.types.DynamicOps
import net.minecraft.block.BlockState
import net.minecraft.world.gen.feature.IFeatureConfig
import thedarkcolour.futuremc.util.deserializeBlockState
import java.util.*

class NetherrackBlobReplacementConfig(
    val base: Int,
    val spread: Int,
    val target: BlockState,
    val state: BlockState,
) : IFeatureConfig {
    constructor(serialized: Dynamic<*>) : this(
        serialized["base"].asInt(0),
        serialized["spread"].asInt(0),
        serialized["state"].deserializeBlockState(),
        serialized["target"].deserializeBlockState(),
    )

    override fun <T : Any?> serialize(ops: DynamicOps<T>): Dynamic<T> {
        return Dynamic(ops, ops.createMap(ImmutableMap.of(
            ops.createString("base"), ops.createInt(base),
            ops.createString("spread"), ops.createInt(spread),
            ops.createString("state"), BlockState.serialize(ops, state).value,
            ops.createString("target"), BlockState.serialize(ops, target).value,
        )))
    }

    fun getRandomSpread(rand: Random): Int {
        return if (spread == 0) base else base + rand.nextInt(spread + 1)
    }

}