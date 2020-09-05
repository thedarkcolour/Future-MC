package thedarkcolour.futuremc.block.villagepillage

import net.minecraft.block.BlockBush
import net.minecraft.block.SoundType
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.world.biome.Biome
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.FConfig

// todo use FBlock.Properties
abstract class BlockFlower(regName: String) : BlockBush() {
    init {
        translationKey = FutureMC.ID + "." + regName
        setRegistryName(regName)
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.DECORATIONS else FutureMC.GROUP
        soundType = SoundType.PLANT
    }

    abstract val validBiomes: Set<Biome>
    abstract val flowerChance: Double
}