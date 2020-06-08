package thedarkcolour.futuremc.item

import net.minecraft.item.Item.ToolMaterial
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.EnumHelper
import thedarkcolour.futuremc.registry.FItems

object FToolMaterial {
    val NETHERITE: ToolMaterial = EnumHelper.addToolMaterial(
        "NETHERITE",
        4,
        2031,
        9.0f,
        4.0f,
        9
    )!!.setRepairItem(ItemStack(FItems.NETHERITE_INGOT))
}