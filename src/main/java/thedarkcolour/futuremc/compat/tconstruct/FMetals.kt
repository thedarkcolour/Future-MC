package thedarkcolour.futuremc.compat.tconstruct

import net.minecraft.item.EnumRarity
import slimeknights.tconstruct.library.fluid.FluidMolten
import slimeknights.tconstruct.library.materials.Material

/**
 * Metals for Tinkers Construct added by Future MC
 */
object FMetals {
    val NETHERITE_MATERIAL = Material("netherite", 0x3b393b)
    val MOLTEN_ANCIENT_DEBRIS = FluidMolten("ancient_debris", 0x4f3028)
    val MOLTEN_NETHERITE = FluidMolten("netherite", 0x3b393b)

    // todo netherite alloy
    fun registerMetals() {
        NETHERITE_MATERIAL.fluid = MOLTEN_NETHERITE
        NETHERITE_MATERIAL.isCastable = true
        NETHERITE_MATERIAL.isCraftable = false
        NETHERITE_MATERIAL.setRenderInfo(0x3b393b)

        //TinkerRegistry.addMaterialStats(NETHERITE_MATERIAL, IMaterialStats)

        MOLTEN_ANCIENT_DEBRIS.temperature = 950
        MOLTEN_NETHERITE.temperature = 1250

        MOLTEN_NETHERITE.rarity = EnumRarity.EPIC
    }
}