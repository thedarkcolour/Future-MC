package thedarkcolour.futuremc.registry

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnumEnchantmentType
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.fml.common.registry.ForgeRegistries
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.enchantment.*
import thedarkcolour.futuremc.item.ItemCrossbow
import thedarkcolour.futuremc.item.ItemTrident

object FEnchantments {
    lateinit var CHANNELING: Enchantment private set
    lateinit var IMPALING: Enchantment private set
    lateinit var LOYALTY: Enchantment private set
    lateinit var RIPTIDE: Enchantment private set
    lateinit var MULTISHOT: Enchantment private set
    lateinit var PIERCING: Enchantment private set
    lateinit var QUICK_CHARGE: Enchantment private set

    lateinit var TRIDENT: EnumEnchantmentType private set
    lateinit var CROSSBOW: EnumEnchantmentType private set

    fun init() {
        val register = ForgeRegistries.ENCHANTMENTS

        CHANNELING = register(EnchantmentChanneling(), FConfig.updateAquatic.trident)
        IMPALING = register(EnchantmentImpaling(), FConfig.updateAquatic.trident)
        LOYALTY = register(EnchantmentLoyalty(), FConfig.updateAquatic.trident)
        RIPTIDE = register(EnchantmentRiptide(), FConfig.updateAquatic.trident)
        MULTISHOT = register(EnchantmentMultishot(), FConfig.villageAndPillage.crossbow)
        PIERCING = register(EnchantmentPiercing(), FConfig.villageAndPillage.crossbow)
        QUICK_CHARGE = register(EnchantmentQuickCharge(), FConfig.villageAndPillage.crossbow)

        TRIDENT = EnumHelper.addEnchantmentType("weapons") { item -> (item is ItemTrident) }!!
        CROSSBOW = EnumHelper.addEnchantmentType("weapons") { item -> (item is ItemCrossbow) }!!
    }
}