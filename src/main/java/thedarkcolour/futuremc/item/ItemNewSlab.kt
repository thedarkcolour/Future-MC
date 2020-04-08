package thedarkcolour.futuremc.item

import net.minecraft.item.ItemSlab
import net.minecraft.item.ItemStack
import thedarkcolour.core.util.setItemModel
import thedarkcolour.futuremc.block.BlockNewSlab
import java.util.*

class ItemNewSlab(private val singleSlab: BlockNewSlab.Half, doubleSlab: BlockNewSlab.BlockDoubleSlab) : ItemSlab(singleSlab, singleSlab, doubleSlab) {
    init {
        registryName = singleSlab.registryName
        translationKey = singleSlab.translationKey
        setItemModel(this, 0)
    }

    override fun getTranslationKey(stack: ItemStack): String {
        return singleSlab.translationKey
    }

    object Slabs {
        private val VARIANTS = arrayOf(
            "granite", "andesite", "diorite",  // DONE
            "polished_granite", "polished_andesite", "polished_diorite",  // DONE
            "stone", "prismarine", "dark_prismarine", "prismarine_brick",  // DONE
            "smooth_red_sandstone", "smooth_sandstone", "mossy_stone_brick",
            "mossy_stone", "smooth_quartz", "red_nether_brick", "end_stone_brick"
        )
        var slabHalves = ArrayList<BlockNewSlab.Half>()
        var slabDoubles = ArrayList<BlockNewSlab.BlockDoubleSlab>()
        var slabItems = ArrayList<ItemNewSlab>()

        fun initSlab() {
            for (variant in VARIANTS) {
                val half = BlockNewSlab.Half(variant)
                val full = BlockNewSlab.BlockDoubleSlab(variant)
                slabHalves.add(half)
                slabDoubles.add(full)
                slabItems.add(ItemNewSlab(half, full))
            }
        }

        fun clean() {
            slabDoubles.clear()
            slabHalves.clear()
            slabItems.clear()
        }
    }
}