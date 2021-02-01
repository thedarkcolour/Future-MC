package thedarkcolour.futuremc.enchantment

import net.minecraft.enchantment.Enchantment
import net.minecraft.inventory.EntityEquipmentSlot
import thedarkcolour.futuremc.registry.FEnchantments

open class EnchantmentTridentBase(rarity: Rarity, name: String) : Enchantment(
    rarity,
    FEnchantments.TRIDENT,
    arrayOf(EntityEquipmentSlot.MAINHAND)
) {
    init {
        setRegistryName(name)
        setName(name)
    }
}