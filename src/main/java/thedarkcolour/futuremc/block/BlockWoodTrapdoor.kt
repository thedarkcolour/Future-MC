package thedarkcolour.futuremc.block

import net.minecraft.block.BlockTrapDoor
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.FConfig

class BlockWoodTrapdoor(string: String) : BlockTrapDoor(Material.WOOD) {
    init {
        setRegistryName(string)
        translationKey = "${FutureMC.ID}.$string"
        soundType = SoundType.WOOD
        enableStats = false
        setHardness(3f)
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.REDSTONE else FutureMC.GROUP
    }
}