package thedarkcolour.futuremc.data

import net.minecraft.block.Block
import net.minecraft.data.DataGenerator
import net.minecraft.item.BlockItem
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.data.LanguageProvider
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.registry.FBlocks

class Lang(gen: DataGenerator) : LanguageProvider(gen, FutureMC.ID, "en_us") {
    private val blocks = ForgeRegistries.BLOCKS.values.filter { b -> b.registryName!!.namespace == FutureMC.ID }
    private val items = ForgeRegistries.ITEMS.values.filter { b -> b.registryName!!.namespace == FutureMC.ID && b !is BlockItem }

    override fun addTranslations() {
        add("container.jei.futuremc.smithing", "Smithing")

        // enforce translations to make sure we don't miss anything
        addBlockTranslations()
        addItemTranslations()

        testForMissingTranslations()
    }

    private fun addBlockTranslations() {
        add(FBlocks.NETHER_GOLD_ORE, "Nether Gold Ore")
        add(FBlocks.SOUL_FIRE, "Soul Fire")
        add(FBlocks.SOUL_SOIL, "Soul Soil")
        add(FBlocks.BASALT, "Basalt")
        add(FBlocks.POLISHED_BASALT, "Polished Basalt")
        add(FBlocks.SOUL_TORCH, "Soul Torch")
        add(FBlocks.SOUL_WALL_TORCH, "Soul Wall Torch")
        add(FBlocks.SOUL_FIRE_LANTERN, "Soul Fire Lantern")
        add(FBlocks.WARPED_STEM, "Warped Stem")
        add(FBlocks.STRIPPED_WARPED_STEM, "Stripped Warped Stem")
        add(FBlocks.WARPED_HYPHAE, "Warped Hyphae")
        add(FBlocks.STRIPPED_WARPED_HYPHAE, "Stripped Warped Hyphae")
        add(FBlocks.WARPED_NYLIUM, "Warped Nylium")
        add(FBlocks.WARPED_FUNGUS, "Warped Fungus")
    }

    private fun addItemTranslations() {
    }

    private fun testForMissingTranslations() {
        val missingBlocks = arrayListOf<ResourceLocation>()
        val missingItems = arrayListOf<ResourceLocation>()
        var passed = true

        if (blocks.isNotEmpty()) {
            for (b in blocks) {
                missingBlocks.add(b.registryName!!)
            }

            passed = false
        }

        if (items.isNotEmpty()) {
            for (i in items) {
                missingItems.add(i.registryName!!)
            }

            passed = false
        }

        if (!passed) {
            if (missingBlocks.isNotEmpty()) {
                FutureMC.LOGGER.error("FUTURE MC IS MISSING BLOCK TRANSLATIONS")

                for (b in missingBlocks) {
                    FutureMC.LOGGER.error(b)
                }
            }
            if (missingItems.isNotEmpty()) {
                FutureMC.LOGGER.error("FUTURE MC IS MISSING ITEM TRANSLATIONS")

                for (b in missingItems) {
                    FutureMC.LOGGER.error(b)
                }
            }

            throw IllegalStateException("FutureMC is missing block/item translations")
        }
    }

    override fun add(key: Block, name: String) {
        super.add(key, name)
    }
}