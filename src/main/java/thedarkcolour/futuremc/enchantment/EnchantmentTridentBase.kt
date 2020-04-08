package thedarkcolour.futuremc.enchantment

import net.minecraft.enchantment.Enchantment
import net.minecraft.inventory.EntityEquipmentSlot

open class EnchantmentTridentBase(rarity: Rarity, name: String) : Enchantment(
    rarity,
    Enchantments.TRIDENT,
    arrayOf(EntityEquipmentSlot.MAINHAND)
) {
    init {
        setRegistryName(name)
        setName(name)
    }
}