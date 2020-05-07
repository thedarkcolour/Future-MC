package thedarkcolour.futuremc.block

import net.minecraft.block.BlockRotatedPillar
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.FConfig

// todo just like BlockStrippedLog
class BlockWood(regName: String) : BlockRotatedPillar(Material.WOOD) {
    init {
        blockHardness = 2.0f
        soundType = SoundType.WOOD
        setRegistryName(regName)
        translationKey = "${FutureMC.ID}.$regName"
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.BUILDING_BLOCKS else FutureMC.GROUP
    }
}