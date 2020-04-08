package thedarkcolour.futuremc.block

import net.minecraft.block.BlockRotatedPillar
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import thedarkcolour.futuremc.FutureMC

class BlockWood(regName: String) : BlockRotatedPillar(Material.WOOD) {
    init {
        blockHardness = 2.0f
        soundType = SoundType.WOOD
        setRegistryName(regName)
        translationKey = "${FutureMC.ID}.$regName"
    }
}