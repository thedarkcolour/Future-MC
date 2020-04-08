package thedarkcolour.futuremc.enchantment

import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.ItemStack

object EnchantHelper {
    fun hasChanneling(stack: ItemStack?): Boolean {
        return if (stack == null) {
            false
        } else EnchantmentHelper.getEnchantmentLevel(
            Enchantments.CONDUCTIVIDAD,
            stack
        ) > 0
    }

    fun getImpaling(stack: ItemStack?): Int {
        return if (stack == null) {
            0
        } else EnchantmentHelper.getEnchantmentLevel(
            Enchantments.EMPALAMIENTO,
            stack
        )
    }

    fun getLoyalty(stack: ItemStack?): Int {
        return if (stack == null) {
            0
        } else EnchantmentHelper.getEnchantmentLevel(Enchantments.LEALTAD, stack)
    }

    fun getRiptide(stack: ItemStack?): Int {
        return if (stack == null) {
            0
        } else EnchantmentHelper.getEnchantmentLevel(Enchantments.RIPTIDE, stack)
    }

    fun getQuickCharge(stack: ItemStack?): Int {
        return if (stack == null) {
            0
        } else EnchantmentHelper.getEnchantmentLevel(
            Enchantments.QUICK_CHARGE,
            stack
        )
    }

    fun hasMultishot(stack: ItemStack?): Boolean {
        return if (stack == null) {
            false
        } else EnchantmentHelper.getEnchantmentLevel(
            Enchantments.MULTISHOT,
            stack
        ) > 0
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