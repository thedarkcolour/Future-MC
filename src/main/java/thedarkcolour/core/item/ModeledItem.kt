package thedarkcolour.core.item

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import thedarkcolour.core.util.setBuiltinRenderer
import thedarkcolour.core.util.setItemModel
import thedarkcolour.core.util.setItemName
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.FConfig

open class ModeledItem(regName: String) : Item() {
    init {
        setItemName(this, regName)
        setItemModel(this, 0)

        if (this is Builtin) {
            setBuiltinRenderer(this)
        }
    }

    override fun setCreativeTab(tab: CreativeTabs): ModeledItem {
        super.setCreativeTab(if (FConfig.useVanillaCreativeTabs) tab else FutureMC.GROUP)
        return this
    }

    /**
     * Specifies that this item has a builtin TEISR
     */
    interface Builtin {
        /**
         * Renders this item from `TileEntityItemStackRenderer`
         */
        fun render(stack: ItemStack, partialTicks: Float)
    }
}