package thedarkcolour.core.item

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import thedarkcolour.core.util.setBuiltinRenderer
import thedarkcolour.core.util.setItemModel
import thedarkcolour.core.util.setItemName
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.entity.trident.ModelTrident

open class ModeledItem(regName: String) : Item() {

    init {
        setItemName(this, regName)
        setItemModel(this, 0)

        // the redundant `this is Item` check
        // is needed because Kotlin's type
        // inference is a bit stupid at the moment
        @Suppress("USELESS_IS_CHECK")
        if (this is Builtin && this is Item) {
            setBuiltinRenderer(this)
        }
    }

    override fun setCreativeTab(tab: CreativeTabs): ModeledItem {
        super.setCreativeTab(if (FConfig.useVanillaCreativeTabs) tab else FutureMC.GROUP)
        return this
    }

    protected companion object {
        val TRIDENT_MODEL = ModelTrident()
    }

    /**
     * Specifies that this [ModeledItem] has
     * a `builtin/entity` renderer entry for use
     * in a custom `TileEntityItemStackRenderer`.
     */
    interface Builtin {

        /**
        * Renders this item from a custom `TileEntityItemStackRenderer`.
         */
        fun render(stack: ItemStack, partialTicks: Float)
    }
}