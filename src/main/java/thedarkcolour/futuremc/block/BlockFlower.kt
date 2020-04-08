package thedarkcolour.futuremc.block

import net.minecraft.block.BlockBush
import net.minecraft.block.SoundType
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.world.biome.Biome
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.FConfig

abstract class BlockFlower(regName: String) : BlockBush() {
    init {
        translationKey = FutureMC.ID + "." + regName
        setRegistryName(regName)
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.DECORATIONS else FutureMC.TAB
        soundType = SoundType.PLANT
    }

    abstract fun isBiomeValid(biome: Biome): Boolean

    abstract val flowerChance: Double
}