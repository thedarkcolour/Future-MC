package thedarkcolour.futuremc.enchantment

import net.minecraft.enchantment.Enchantment
import thedarkcolour.futuremc.registry.FEnchantments

class EnchantmentPiercing : EnchantmentCrossbowBase(Rarity.COMMON, "piercing") {
    override fun getMaxLevel(): Int {
        return 4
    }

    override fun getMinEnchantability(enchantmentLevel: Int): Int {
        return 1 + (enchantmentLevel - 1) * 10
    }

    override fun canApplyTogether(ench: Enchantment): Boolean {
        return super.canApplyTogether(ench) && ench !== FEnchantments.MULTISHOT
    }
}