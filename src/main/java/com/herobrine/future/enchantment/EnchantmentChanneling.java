package com.herobrine.future.enchantment;

public class EnchantmentChanneling extends EnchantmentTridentBase {
    public EnchantmentChanneling() {
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
