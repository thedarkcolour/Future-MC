package thedarkcolour.futuremc.enchantment

import net.minecraft.enchantment.Enchantment
import net.minecraft.inventory.EntityEquipmentSlot

open class EnchantmentCrossbowBase(rarity: Rarity, name: String) : Enchantment(
    rarity,
    Enchantments.CROSSBOW,
    arrayOf(EntityEquipmentSlot.MAINHAND)
) {
    init {
        setRegistryName(name)
        setName(name)
    }
}