package thedarkcolour.futuremc.enchantment;

public class EnchantChanneling extends EnchantmentTridentBase {
    public EnchantChanneling() {
        super(Rarity.VERY_RARE, "channeling");
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 25;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}
