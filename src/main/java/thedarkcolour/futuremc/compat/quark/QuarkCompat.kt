package thedarkcolour.futuremc.compat.quark

import net.minecraft.block.state.IBlockState
import vazkii.quark.automation.block.BlockColorSlime

object QuarkCompat {
    fun isColoredSlime(state: IBlockState): Boolean {
        return state.block is BlockColorSlime
    }
}