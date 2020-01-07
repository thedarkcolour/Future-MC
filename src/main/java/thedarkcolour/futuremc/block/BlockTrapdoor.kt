package thedarkcolour.futuremc.block

import net.minecraft.block.material.Material
import thedarkcolour.futuremc.FutureMC

class BlockTrapdoor(string: String) : net.minecraft.block.BlockTrapDoor(Material.WOOD) {
    init {
        setRegistryName(string)
        translationKey = "${FutureMC.ID}.$string"
    }
}