package thedarkcolour.futuremc.enchantment

class EnchantmentQuickCharge : EnchantmentCrossbowBase(Rarity.COMMON, "quick_charge") {
    override fun getMinEnchantability(enchantmentLevel: Int): Int {
        return 5 + (enchantmentLevel - 1) * 7
    }

    override fun getMaxEnchantability(enchantmentLevel: Int): Int {
        return 50
    }

    override fun getMaxLevel(): Int {
        return 3
    }
}