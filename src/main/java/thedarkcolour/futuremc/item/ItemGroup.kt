package thedarkcolour.futuremc.item

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.registry.FBlocks
import thedarkcolour.futuremc.registry.FItems

class ItemGroup : CreativeTabs("futuremc") {
    override fun createIcon(): ItemStack {
        return when {
            FConfig.villageAndPillage.lantern -> ItemStack(FBlocks.LANTERN)
            FConfig.villageAndPillage.bamboo.enabled -> ItemStack(FItems.BAMBOO)
            else -> ItemStack(Blocks.GRASS)
        }
    }
}