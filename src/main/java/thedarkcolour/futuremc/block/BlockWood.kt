package thedarkcolour.futuremc.block

import net.minecraft.block.BlockRotatedPillar
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material

class BlockWood : BlockRotatedPillar(Material.WOOD) {
    init {
        blockHardness = 2.0f
        soundType = SoundType.WOOD
    }
}