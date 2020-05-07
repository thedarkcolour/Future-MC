package thedarkcolour.core.block

import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.EnumDyeColor
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.AxisAlignedBB
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.FConfig

/**
 * Uses a properties class similar to the one from 1.13+
 */
@Suppress("DEPRECATION")
open class FBlock(properties: Properties) : Block(properties.material, properties.color) {
    init {
        translationKey = FutureMC.ID + "." + properties.registryName
        registryName = ResourceLocation(FutureMC.ID, properties.registryName)
        blockSoundType = properties.sound
        blockHardness = properties.hardness
        blockResistance = properties.resistance
        lightValue = properties.light
        needsRandomTick = properties.randomTick
        slipperiness = properties.slipperiness
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        creativeTab = if (FConfig.useVanillaCreativeTabs) {
            val group = properties.group
            if (group == null && FutureMC.DEBUG)
                FutureMC.LOGGER.warn("No creative tab for block $this")
            group
        } else FutureMC.GROUP
    }

    override fun toString(): String {
        return javaClass.canonicalName + "{${registryName}}"
    }

    companion object {
        fun makeAABB(
            startX: Double, startY: Double, startZ: Double,
            endX: Double, endY: Double, endZ: Double
        ): AxisAlignedBB {
            return AxisAlignedBB(startX / 16.0, startY / 16.0, startZ / 16.0, endX / 16.0, endY / 16.0, endZ / 16.0)
        }
    }

    @Suppress("HasPlatformType")
    class Properties(var material: Material, val registryName: String) {
        var color = material.materialMapColor
        var sound = SoundType.STONE
        var hardness = 3f
        var resistance = 3f
        var light = 0
        var randomTick = false
        var slipperiness = 0.6f
        var group: CreativeTabs? = null

        fun color(color: MapColor): Properties {
            this.color = color
            return this
        }

        fun color(color: EnumDyeColor): Properties {
            this.color = MapColor.getBlockColor(color)
            return this
        }

        fun group(group: CreativeTabs): Properties {
            this.group = group
            return this
        }

        fun hardnessAndResistance(hardness: Float, resistance: Float = hardness): Properties {
            this.hardness = hardness
            this.resistance = resistance
            return this
        }

        fun light(light: Int): Properties {
            if (light !in 0..15) throw IllegalArgumentException("Light value must be in 0..15")
            this.light = light
            return this
        }

        fun sound(sound: SoundType): Properties {
            this.sound = sound
            return this
        }

        fun tickRandomly(): Properties {
            this.randomTick = true
            return this
        }

        fun breakInstantly(): Properties {
            hardnessAndResistance(0.0f, 0.0f)
            return this
        }

        fun slipperiness(slipperiness: Float): Properties {
            this.slipperiness = slipperiness
            return this
        }
    }
}