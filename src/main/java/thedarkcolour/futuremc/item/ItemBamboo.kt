package thedarkcolour.futuremc.item

import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import thedarkcolour.core.item.Modeled
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.init.FBlocks

class ItemBamboo : ItemBlock(FBlocks.BAMBOO), Modeled {
    init {
        translationKey = FutureMC.ID + ".bamboo"
        setRegistryName("bamboo")
        addModel()
    }

    override fun getItemBurnTime(itemStack: ItemStack): Int {
        return 50
    }
}