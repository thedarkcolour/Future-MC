package com.herobrine.future.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

import java.util.Iterator;
import java.util.Map;

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

    public static boolean hasMultishot(ItemStack stack) {
        if(stack == null) {
            return false;
        }
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.MULTISHOT, stack) > 0;
    }

    public static boolean isCursed(ItemStack stack) {
        if(stack.isEmpty()) return false;
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
        for(Enchantment e : map.keySet()) {
            if(e.isCurse()) {
                return true;
            }
        }
        return false;
    }
}