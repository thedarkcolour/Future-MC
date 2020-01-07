package thedarkcolour.futuremc.item

import net.minecraft.block.BlockSlab
import net.minecraft.item.ItemSlab
import net.minecraft.item.ItemStack
import thedarkcolour.core.item.Modeled
import thedarkcolour.futuremc.block.BlockNewSlab
import java.util.*

class ItemNewSlab(singleSlab: BlockNewSlab.Half, doubleSlab: BlockNewSlab.Double?) : ItemSlab(singleSlab, singleSlab, doubleSlab), Modeled {
    private val singleSlab: BlockSlab
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
        var slabHalves: MutableList<BlockNewSlab.Half>? = ArrayList()
        var slabDoubles: MutableList<BlockNewSlab.Double>? = ArrayList()
        var slabItems: MutableList<ItemNewSlab>? = ArrayList()
        fun initSlab() {
            for (variant in VARIANTS) {
                val half = BlockNewSlab.Half(variant)
                val full = BlockNewSlab.Double(variant)
                slabHalves!!.add(half)
                slabDoubles!!.add(full)
                slabItems!!.add(ItemNewSlab(half, full))
            }
        }

        fun clean() {
            slabDoubles = null
            slabHalves = null
            slabItems = null
        }
    }

    init {
        registryName = singleSlab.registryName
        translationKey = singleSlab.translationKey
        //setCreativeTab(FConfig.useVanillaCreativeTabs ? CreativeTabs.BUILDING_BLOCKS : Init.CREATIVE_TAB);
        this.singleSlab = singleSlab
        addModel()
    }
}