package thedarkcolour.futuremc.registry

import net.minecraft.enchantment.Enchantment
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.event.RegistryEvent
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.enchantment.*
import thedarkcolour.futuremc.item.CrossbowItem
import thedarkcolour.futuremc.item.TridentItem

object FEnchantments {
    val TRIDENT = EnumHelper.addEnchantmentType("weapons") { item -> item is TridentItem }!!

    val LEALTAD: Enchantment = EnchantmentLoyalty()
    val CONDUCTIVIDAD: Enchantment = EnchantmentChanneling()
    val RIPTIDE: Enchantment = EnchantmentRiptide()
    val EMPALAMIENTO: Enchantment = EnchantmentImpaling()

    val CROSSBOW = EnumHelper.addEnchantmentType("weapons") { item -> item is CrossbowItem }!!

    val QUICK_CHARGE: Enchantment = EnchantmentQuickCharge()
    val MULTISHOT: Enchantment = EnchantmentMultishot()
    val PIERCING: Enchantment = EnchantmentPiercing()

    fun registerEnchantments(event: RegistryEvent.Register<Enchantment>) {
        val r = event.registry

        if (FConfig.updateAquatic.trident) r.registerAll(LEALTAD, CONDUCTIVIDAD, RIPTIDE, EMPALAMIENTO)
        if (FConfig.villageAndPillage.crossbow) r.registerAll(QUICK_CHARGE, MULTISHOT, PIERCING)
    }
}