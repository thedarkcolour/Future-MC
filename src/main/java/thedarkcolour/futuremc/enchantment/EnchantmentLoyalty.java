package thedarkcolour.futuremc.enchantment;

public class EnchantmentLoyalty extends EnchantmentTridentBase {
    public EnchantmentLoyalty() {
        super(Rarity.UNCOMMON, "loyalty");
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 5 + (enchantmentLevel - 1) * 7;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
