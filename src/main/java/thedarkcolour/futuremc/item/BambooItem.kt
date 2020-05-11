package thedarkcolour.futuremc.item

import net.minecraft.item.ItemStack
import thedarkcolour.core.item.ItemModeledBlock
import thedarkcolour.futuremc.registry.FBlocks

class BambooItem : ItemModeledBlock(FBlocks.BAMBOO) {
    override fun getItemBurnTime(itemStack: ItemStack): Int {
        return 50
    }
}