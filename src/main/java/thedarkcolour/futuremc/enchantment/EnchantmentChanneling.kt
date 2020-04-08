package thedarkcolour.futuremc.enchantment

class EnchantmentChanneling : EnchantmentTridentBase(Rarity.VERY_RARE, "channeling") {
    override fun getMinEnchantability(enchantmentLevel: Int): Int {
        return 25
    }

    override fun getMaxEnchantability(enchantmentLevel: Int): Int {
        return 50
    }

    override fun getMaxLevel(): Int {
        return 1
    }
}