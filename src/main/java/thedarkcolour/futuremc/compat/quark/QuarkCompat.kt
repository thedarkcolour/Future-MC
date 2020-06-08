package thedarkcolour.futuremc.compat.quark

import net.minecraft.block.state.IBlockState
import vazkii.quark.automation.block.BlockColorSlime
import vazkii.quark.base.module.ModuleLoader
import vazkii.quark.decoration.feature.VariedTrapdoors

object QuarkCompat {
    fun isColoredSlime(state: IBlockState): Boolean {
        return state.block is BlockColorSlime
    }

    fun hasVariedTrapdoors(): Boolean {
        return ModuleLoader.isFeatureEnabled(VariedTrapdoors::class.java)
    }
}