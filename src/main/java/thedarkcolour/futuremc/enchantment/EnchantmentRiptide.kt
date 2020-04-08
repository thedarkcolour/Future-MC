package thedarkcolour.futuremc.enchantment

import net.minecraft.enchantment.Enchantment

class EnchantmentRiptide : EnchantmentTridentBase(Rarity.RARE, "riptide") {
    override fun getMinEnchantability(enchantmentLevel: Int): Int {
        return 10 + enchantmentLevel * 7
    }

    override fun getMaxEnchantability(enchantmentLevel: Int): Int {
        return 50
    }

    override fun getMaxLevel(): Int {
        return 3
    }

    override fun canApplyTogether(ench: Enchantment): Boolean {
        return super.canApplyTogether(ench) && ench !== Enchantments.LEALTAD && ench !== Enchantments.CONDUCTIVIDAD
    }
}