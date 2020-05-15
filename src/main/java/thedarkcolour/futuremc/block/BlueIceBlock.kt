package thedarkcolour.futuremc.block

import net.minecraft.block.state.IBlockState
import thedarkcolour.core.block.FBlock
import java.util.*

class BlueIceBlock(properties: Properties) : FBlock(properties) {
    override fun quantityDropped(random: Random) = 0

    override fun getHarvestTool(state: IBlockState) = "pickaxe"
}