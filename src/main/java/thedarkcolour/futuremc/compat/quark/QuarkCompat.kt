package thedarkcolour.futuremc.compat.quark

import net.minecraft.block.state.IBlockState
import thedarkcolour.core.util.isQuarkLoaded
import vazkii.quark.automation.block.BlockColorSlime

object QuarkCompat {
    fun isNotColoredSlime(state: IBlockState): Boolean {
        return if (isQuarkLoaded) {
            state.block !is BlockColorSlime
        } else {
            false
        }
    }
}