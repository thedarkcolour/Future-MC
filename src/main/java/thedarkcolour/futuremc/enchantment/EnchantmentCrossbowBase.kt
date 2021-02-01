package thedarkcolour.futuremc.enchantment

import net.minecraft.enchantment.Enchantment
import net.minecraft.inventory.EntityEquipmentSlot
import thedarkcolour.futuremc.registry.FEnchantments

open class EnchantmentCrossbowBase(rarity: Rarity, name: String) : Enchantment(
    rarity,
    FEnchantments.CROSSBOW,
    arrayOf(EntityEquipmentSlot.MAINHAND)
) {
    init {
        setRegistryName(name)
        setName(name)
    }
}