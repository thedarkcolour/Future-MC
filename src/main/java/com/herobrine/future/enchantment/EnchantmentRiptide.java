package com.herobrine.future.enchantment;

public class EnchantmentRiptide extends EnchantmentTridentBase {
    public EnchantmentRiptide() {
        super(Rarity.RARE, "riptide");
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
