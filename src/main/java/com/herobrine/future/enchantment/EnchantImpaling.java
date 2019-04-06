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
        return this.getMinEnchantability(enchantmentLevel) + 20;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType) {
        return 1.25F * level;
    }

    public static float getDamageForTrident(int level) {
        if(level > 0) {
            return Enchantments.IMPALING.calcDamageByCreature(level, null);
        }
        else {
            return 3.25F;
        }
    }
}
