package thedarkcolour.futuremc.enchantment

import net.minecraft.enchantment.Enchantment
import net.minecraftforge.common.util.EnumHelper
import thedarkcolour.futuremc.item.ItemCrossbow
import thedarkcolour.futuremc.item.ItemTrident

object Enchantments {
    val TRIDENT = EnumHelper.addEnchantmentType("weapons") { item -> item is ItemTrident }!!

    val LEALTAD: Enchantment = EnchantmentLoyalty()
    val CONDUCTIVIDAD: Enchantment = EnchantmentChanneling()
    val RIPTIDE: Enchantment = EnchantmentRiptide()
    val EMPALAMIENTO: Enchantment = EnchantmentImpaling()

    val CROSSBOW = EnumHelper.addEnchantmentType("weapons") { item -> item is ItemCrossbow }!!

    val QUICK_CHARGE: Enchantment = EnchantmentQuickCharge()
    val MULTISHOT: Enchantment = EnchantmentMultishot()
    val PIERCING: Enchantment = EnchantmentPiercing()
}