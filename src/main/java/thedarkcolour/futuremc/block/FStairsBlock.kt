package thedarkcolour.futuremc.block

import net.minecraft.block.BlockStairs
import net.minecraft.block.state.IBlockState
import net.minecraft.util.ResourceLocation
import thedarkcolour.core.block.FBlock
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.FConfig

class FStairsBlock(modelState: IBlockState, properties: FBlock.Properties) : BlockStairs(modelState) {
    init {
        translationKey = FutureMC.ID + "." + properties.registryName
        registryName = ResourceLocation(FutureMC.ID, properties.registryName)
        blockSoundType = properties.sound
        blockHardness = properties.hardness
        blockResistance = properties.resistance
        lightValue = properties.light
        needsRandomTick = properties.randomTick
        slipperiness = properties.slipperiness

        creativeTab = if (FConfig.useVanillaCreativeTabs) {
            val group = properties.group
            if (group == null && FutureMC.DEBUG)
                println(("No creative tab for block ${toString()}"))
            group
        } else FutureMC.GROUP
    }
}