package thedarkcolour.futuremc.enchantment

class EnchantmentImpaling : EnchantmentTridentBase(Rarity.RARE, "impaling") {
    override fun getMinEnchantability(enchantmentLevel: Int): Int {
        return 1 + (enchantmentLevel - 1) * 8
    }

    override fun getMaxEnchantability(enchantmentLevel: Int): Int {
        return getMinEnchantability(enchantmentLevel) + 20
    }

    override fun getMaxLevel(): Int {
        return 5
    }
}