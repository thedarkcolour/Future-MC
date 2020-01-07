package thedarkcolour.futuremc.block

import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.FutureMC.TAB
import thedarkcolour.futuremc.config.FConfig
import java.util.*

class BlockBlueIce : Block(Material.PACKED_ICE) {
    override fun quantityDropped(random: Random): Int {
        return 0
    }

    override fun getHarvestTool(state: IBlockState): String? {
        return "pickaxe"
    }

    init {
        translationKey = FutureMC.ID + ".blue_ice"
        setRegistryName("blue_ice")
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.BUILDING_BLOCKS else TAB
        setDefaultSlipperiness(0.989f)
        setHardness(2.8f)
        soundType = SoundType.GLASS
    }
}