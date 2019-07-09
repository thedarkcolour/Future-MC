package com.herobrine.future.enchantment;

import net.minecraft.entity.EnumCreatureAttribute;

public class EnchantImpaling extends EnchantmentTridentBase {
    public EnchantImpaling() {
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

    @Override
    public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType) {
        return level * 2.5F;
    }
}