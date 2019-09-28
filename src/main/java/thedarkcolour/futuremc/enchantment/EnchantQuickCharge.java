package thedarkcolour.futuremc.enchantment;

public class EnchantQuickCharge extends EnchantmentCrossbowBase {
    public EnchantQuickCharge() {
        super(Rarity.COMMON, "quick_charge");
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