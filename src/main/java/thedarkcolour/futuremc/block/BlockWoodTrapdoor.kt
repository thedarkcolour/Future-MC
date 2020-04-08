package thedarkcolour.futuremc.block

import net.minecraft.block.BlockTrapDoor
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import thedarkcolour.futuremc.FutureMC

class BlockWoodTrapdoor(string: String) : BlockTrapDoor(Material.WOOD) {
    init {
        setRegistryName(string)
        translationKey = "${FutureMC.ID}.$string"
        soundType = SoundType.WOOD
        enableStats = false
        setHardness(3f)
    }
}