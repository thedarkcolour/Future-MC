package thedarkcolour.futuremc.item

import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.IArmorMaterial
import net.minecraft.item.crafting.Ingredient
import thedarkcolour.futuremc.registry.FItems
import thedarkcolour.futuremc.registry.FSounds

object NetheriteArmorMaterial : IArmorMaterial {
    override fun getSoundEvent() = FSounds.ITEM_ARMOR_EQUIP_NETHERITE

    override fun getRepairMaterial(): Ingredient {
        return Ingredient.fromItems(FItems.NETHERITE_INGOT)
    }

    override fun getToughness() = 3.0f

    override fun getName() = "futuremc:netherite"

    override fun getDurability(slotType: EquipmentSlotType) = when (slotType) {
        EquipmentSlotType.FEET -> 481
        EquipmentSlotType.LEGS -> 555
        EquipmentSlotType.CHEST -> 592
        EquipmentSlotType.HEAD -> 407
        else -> 0
    }

    override fun getEnchantability() = 15

    override fun getDamageReductionAmount(slotType: EquipmentSlotType) = when (slotType) {
        EquipmentSlotType.FEET -> 3
        EquipmentSlotType.LEGS -> 6
        EquipmentSlotType.CHEST -> 8
        EquipmentSlotType.HEAD -> 3
        else -> 0
    }
}