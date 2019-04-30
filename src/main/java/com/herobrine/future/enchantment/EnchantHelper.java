package com.herobrine.future.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

public final class EnchantHelper {
    public static boolean hasChanneling(ItemStack stack) {
        if(stack == null) {
            return false;
        }
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.CHANNELING, stack) > 0;
    }

    public static int getImpaling(ItemStack stack) {
        if(stack == null) {
            return 0;
        }
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.IMPALING, stack);
    }

    public static int getLoyalty(ItemStack stack) {
        if(stack == null) {
            return 0;
        }
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.LOYALTY, stack);
    }

    public static int getQuickCharge(ItemStack stack) {
        if(stack == null) {
            return 0;
        }
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
    }
}