package thedarkcolour.futuremc.item

import net.minecraft.item.IItemTier
import net.minecraft.item.crafting.Ingredient
import thedarkcolour.futuremc.registry.FItems

object NetheriteItemTier : IItemTier {
    override fun getRepairMaterial(): Ingredient {
        return Ingredient.fromItems(FItems.NETHERITE_INGOT)
    }

    override fun getHarvestLevel() = 4

    override fun getMaxUses() = 2031

    override fun getAttackDamage() = 4.0f

    override fun getEnchantability() = 15

    override fun getEfficiency() = 9.0f
}