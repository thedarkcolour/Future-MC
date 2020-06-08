package thedarkcolour.futuremc.item

import net.minecraft.item.ItemArmor.ArmorMaterial
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.EnumHelper
import thedarkcolour.futuremc.registry.FItems
import thedarkcolour.futuremc.registry.FSounds

/**
 * Since we cannot extend an enum class, I use an object
 * with values created by EnumHelper.
 */
object FArmorMaterial {
    val TURTLE: ArmorMaterial = EnumHelper.addArmorMaterial(
        "TURTLE",
        "futuremc:turtle",
        25,
        intArrayOf(2, 5, 6, 2),
        9,
        FSounds.ITEM_ARMOR_EQUIP_TURTLE,
        0.0f
    )!!.setRepairItem(ItemStack(FItems.SCUTE))
    val NETHERITE: ArmorMaterial = EnumHelper.addArmorMaterial(
        "NETHERITE",
        "futuremc:netherite",
        37,
        intArrayOf(3, 6, 8, 3),
        15,
        FSounds.ITEM_ARMOR_EQUIP_NETHERITE,
        3.0f
    )!!.setRepairItem(ItemStack(FItems.NETHERITE_INGOT))
}