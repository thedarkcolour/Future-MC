package thedarkcolour.futuremc.enchantment;

public class EnchantmentImpaling extends EnchantmentTridentBase {
    public EnchantmentImpaling() {
        super(Rarity.RARE, "impaling");
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + (enchantmentLevel - 1) * 8;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return getMinEnchantability(enchantmentLevel) + 20;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }
}