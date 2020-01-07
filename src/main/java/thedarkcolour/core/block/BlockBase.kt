package thedarkcolour.core.block

import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.util.math.AxisAlignedBB
import thedarkcolour.futuremc.FutureMC

open class BlockBase : Block {
    @JvmOverloads
    constructor(regName: String, material: Material = Material.ROCK, soundType: SoundType = SoundType.STONE) : super(material) {
        translationKey = "${FutureMC.ID}.$regName"
        setRegistryName(regName)
        blockSoundType = soundType
        blockHardness = 3.0f
    }

    constructor(regName: String, material: Material, color: MapColor, soundType: SoundType) : super(material, color) {
        translationKey = "${FutureMC.ID}.$regName"
        setRegistryName(regName)
        blockSoundType = soundType
        blockHardness = 3.0f
    }

    companion object {
        fun makeAABB(startX: Double, startY: Double, startZ: Double, endX: Double, endY: Double, endZ: Double): AxisAlignedBB {
            return AxisAlignedBB(startX / 16.0, startY / 16.0, startZ / 16.0, endX / 16.0, endY / 16.0, endZ / 16.0)
        }
    }
}