package thedarkcolour.futuremc.feature

import com.google.common.collect.ImmutableMap
import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.types.DynamicOps
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.world.gen.feature.IFeatureConfig
import thedarkcolour.futuremc.registry.FBlocks
import thedarkcolour.futuremc.util.deserializeBlockState

class HugeFungusFeatureConfig(val base: BlockState, val stem: BlockState, val hat: BlockState, val decoration: BlockState, val planted: Boolean) : IFeatureConfig {
    // see Util.kt
    constructor(serialized: Dynamic<*>) : this(
        base = serialized["valid_base_state"].deserializeBlockState(),
        stem = serialized["stem_state"].deserializeBlockState(),
        hat = serialized["hat_state"].deserializeBlockState(),
        decoration = serialized["decor_state"].deserializeBlockState(),
        planted = serialized["planted"].asBoolean(false)
    )

    override fun <T> serialize(ops: DynamicOps<T>): Dynamic<T> {
        return Dynamic(ops, ops.createMap(ImmutableMap.of(
                ops.createString("valid_base_block"), BlockState.serialize(ops, base).value,
                ops.createString("stem_state"), BlockState.serialize(ops, stem).value,
                ops.createString("hat_state"), BlockState.serialize(ops, hat).value,
                ops.createString("decor_state"), BlockState.serialize(ops, decoration).value,
                ops.createString("planted"), ops.createBoolean(planted),
        )))
    }

    companion object {
        val CRIMSON_PLANTED = HugeFungusFeatureConfig(FBlocks.CRIMSON_NYLIUM.defaultState, FBlocks.CRIMSON_STEM.defaultState, Blocks.NETHER_WART_BLOCK.defaultState, FBlocks.SHROOMLIGHT.defaultState, planted = true)
        val CRIMSON = HugeFungusFeatureConfig(CRIMSON_PLANTED.base, CRIMSON_PLANTED.stem, CRIMSON_PLANTED.hat, CRIMSON_PLANTED.decoration, planted = false)
        val WARPED_PLANTED = HugeFungusFeatureConfig(FBlocks.WARPED_NYLIUM.defaultState, FBlocks.WARPED_STEM.defaultState, FBlocks.WARPED_WART_BLOCK.defaultState, FBlocks.SHROOMLIGHT.defaultState, planted = true)
        val WARPED = HugeFungusFeatureConfig(WARPED_PLANTED.base, WARPED_PLANTED.stem, WARPED_PLANTED.hat, WARPED_PLANTED.decoration, planted = false)
    }
}