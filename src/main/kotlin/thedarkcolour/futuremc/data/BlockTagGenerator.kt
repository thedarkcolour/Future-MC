package thedarkcolour.futuremc.data

import net.minecraft.data.BlockTagsProvider
import net.minecraft.data.DataGenerator
import net.minecraft.tags.Tag
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.Tags
import thedarkcolour.futuremc.registry.FBlocks

class BlockTagGenerator(gen: DataGenerator) : BlockTagsProvider(gen) {
    /**
     * A set of all non Future MC blocks and tags
     * that is used to avoid generating unused tags.
     */
    private var filter: Set<ResourceLocation>? = null

    /**
     * Register our block tags.
     */
    private fun registerBlockTags() {
        getBuilder(Tags.Blocks.ORES_GOLD).add(FBlocks.NETHER_GOLD_ORE)
    }

    override fun registerTags() {
        super.registerTags()

        filter = tagToBuilder.keys.map(Tag<*>::getId).toHashSet()

        registerBlockTags()
    }
}