package thedarkcolour.futuremc.enchantment;

public class EnchantmentQuickCharge extends EnchantmentCrossbowBase {
    public EnchantmentQuickCharge() {
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