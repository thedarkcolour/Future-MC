package thedarkcolour.futuremc.enchantment

import net.minecraft.enchantment.Enchantment

class EnchantmentMultishot : EnchantmentCrossbowBase(Rarity.RARE, "multishot") {
    override fun getMinEnchantability(enchantmentLevel: Int): Int {
        return 20
    }

    override fun getMaxLevel(): Int {
        return 1
    }

    override fun canApplyTogether(ench: Enchantment): Boolean {
        return super.canApplyTogether(ench) && ench !== Enchantments.PIERCING
    }
}