package thedarkcolour.core.item

import net.minecraft.block.Block
import net.minecraft.item.ItemBlock

class ItemModeledBlock(block: Block) : ItemBlock(block), Modeled {
    init {
        addModel()
        registryName = block.registryName
    }
}