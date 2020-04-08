package thedarkcolour.core.item

import net.minecraft.item.Item
import thedarkcolour.core.util.setItemModel
import thedarkcolour.core.util.setItemName

open class ItemModeled(regName: String) : Item() {
    init {
        setItemName(this, regName)
        setItemModel(this, 0)
    }
}