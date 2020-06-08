package thedarkcolour.futuremc.item

import net.minecraft.item.ItemStack
import thedarkcolour.core.item.ModeledItemBlock
import thedarkcolour.futuremc.registry.FBlocks

class BambooItem : ModeledItemBlock(FBlocks.BAMBOO) {
    override fun getItemBurnTime(itemStack: ItemStack): Int {
        return 50
    }
}