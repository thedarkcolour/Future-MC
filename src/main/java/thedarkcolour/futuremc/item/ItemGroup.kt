package thedarkcolour.futuremc.item

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.init.FBlocks
import thedarkcolour.futuremc.init.FItems

class ItemGroup : CreativeTabs("futuremc") {
    override fun createIcon(): ItemStack {
        return if (FConfig.villageAndPillage.lantern) ItemStack(FBlocks.LANTERN) else if (FConfig.villageAndPillage.bamboo.enabled) ItemStack(FItems.BAMBOO) else ItemStack(Blocks.GRASS)
    }
}