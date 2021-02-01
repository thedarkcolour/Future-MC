package thedarkcolour.futuremc.enchantment

import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.ItemStack
import thedarkcolour.futuremc.registry.FEnchantments

object EnchantHelper {
    fun getChanneling(stack: ItemStack): Boolean {
        return EnchantmentHelper.getEnchantmentLevel(FEnchantments.CONDUCTIVIDAD, stack) > 0
    }

    fun getImpaling(stack: ItemStack): Int {
        return EnchantmentHelper.getEnchantmentLevel(FEnchantments.EMPALAMIENTO, stack)
    }

    fun getLoyalty(stack: ItemStack): Int {
        return EnchantmentHelper.getEnchantmentLevel(FEnchantments.LEALTAD, stack)
    }

    fun getRiptide(stack: ItemStack): Int {
        return EnchantmentHelper.getEnchantmentLevel(FEnchantments.RIPTIDE, stack)
    }

    fun getQuickCharge(stack: ItemStack): Int {
        return EnchantmentHelper.getEnchantmentLevel(FEnchantments.QUICK_CHARGE, stack)
    }

    fun getMultishot(stack: ItemStack): Boolean {
        return EnchantmentHelper.getEnchantmentLevel(FEnchantments.MULTISHOT, stack) > 0
    }

    fun isCursed(stack: ItemStack): Boolean {
        if (stack.isEmpty) return false
        val map = EnchantmentHelper.getEnchantments(stack)
        for (e in map.keys) {
            if (e.isCurse) {
                return true
            }
        }
        return false
    }
}