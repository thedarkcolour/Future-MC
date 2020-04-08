package thedarkcolour.core.block

import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.item.EnumDyeColor
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.AxisAlignedBB
import thedarkcolour.futuremc.FutureMC

open class BlockBase : Block {
    @JvmOverloads
    @Deprecated("Use props constructor", ReplaceWith("()as"))
    constructor(regName: String, material: Material = Material.ROCK, soundType: SoundType = SoundType.STONE)
            : super(material) {
        translationKey = "${FutureMC.ID}.$regName"
        setRegistryName(regName)
        blockSoundType = soundType
        blockHardness = 3.0f
    }

    @Deprecated("Use props constructor", ReplaceWith("()"))
    constructor(regName: String, material: Material, color: MapColor, soundType: SoundType) : super(material, color) {
        translationKey = "${FutureMC.ID}.$regName"
        setRegistryName(regName)
        blockSoundType = soundType
        blockHardness = 3.0f
    }

    constructor(properties: Props) : super(
        properties.material,
        properties.color ?: properties.material.materialMapColor
    ) {
        translationKey = FutureMC.ID + "." + properties.registryName
        registryName = ResourceLocation(FutureMC.ID, properties.registryName)
        blockSoundType = properties.sound
        blockHardness = properties.hardness
    }

    companion object {
        fun makeAABB(
            startX: Double,
            startY: Double,
            startZ: Double,
            endX: Double,
            endY: Double,
            endZ: Double
        ): AxisAlignedBB {
            return AxisAlignedBB(startX / 16.0, startY / 16.0, startZ / 16.0, endX / 16.0, endY / 16.0, endZ / 16.0)
        }
    }

    class Props(val registryName: String) {
        var material = Material.ROCK
        var sound = SoundType.STONE
        var color: MapColor? = null
        var hardness = 3f

        fun material(material: Material) {
            this.material = material
        }

        fun color(color: MapColor) {
            this.color = color
        }

        fun color(color: EnumDyeColor) {
            this.color = MapColor.getBlockColor(color)
        }

        fun sound(sound: SoundType) {
            this.sound = sound
        }

        fun hardness(hardness: Float) {
            this.hardness = hardness
        }
    }
}