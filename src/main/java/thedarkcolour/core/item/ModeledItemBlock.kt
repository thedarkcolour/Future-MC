package thedarkcolour.core.item

import net.minecraft.block.Block
import net.minecraft.item.ItemBlock
import thedarkcolour.core.util.setItemModel

open class ModeledItemBlock(block: Block) : ItemBlock(block) {
    init {
        registryName = block.registryName

        setItemModel(this, 0)
    }
}