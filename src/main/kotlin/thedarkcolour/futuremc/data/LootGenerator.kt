package thedarkcolour.futuremc.data

import com.google.gson.GsonBuilder
import net.minecraft.advancements.criterion.StatePropertiesPredicate
import net.minecraft.block.Block
import net.minecraft.block.SlabBlock
import net.minecraft.data.DataGenerator
import net.minecraft.data.DirectoryCache
import net.minecraft.data.IDataProvider
import net.minecraft.data.LootTableProvider
import net.minecraft.state.properties.SlabType
import net.minecraft.util.ResourceLocation
import net.minecraft.world.storage.loot.*
import net.minecraft.world.storage.loot.conditions.BlockStateProperty
import net.minecraft.world.storage.loot.conditions.SurvivesExplosion
import net.minecraft.world.storage.loot.functions.SetCount
import org.apache.logging.log4j.LogManager
import thedarkcolour.futuremc.registry.FBlocks

class LootGenerator(private val gen: DataGenerator) : LootTableProvider(gen) {
    private val tables = hashMapOf<Block, LootTable.Builder>()

    private fun addLoot() {
        // todo crimson fence
        dropSelf(FBlocks.NETHER_GOLD_ORE)
        dropSelf(FBlocks.SOUL_FIRE)
        dropSelf(FBlocks.SOUL_SOIL)
        dropSelf(FBlocks.BASALT)
        dropSelf(FBlocks.POLISHED_BASALT)
        dropSelf(FBlocks.SOUL_TORCH)
        dropSelf(FBlocks.SOUL_WALL_TORCH)
        dropSelf(FBlocks.SOUL_FIRE_LANTERN)
        dropSelf(FBlocks.WARPED_STEM)
        dropSelf(FBlocks.STRIPPED_WARPED_STEM)
        dropSelf(FBlocks.WARPED_HYPHAE)
        dropSelf(FBlocks.STRIPPED_WARPED_HYPHAE)
        dropSelf(FBlocks.WARPED_NYLIUM)
        dropSelf(FBlocks.WARPED_FUNGUS)
        dropSelf(FBlocks.WARPED_WART_BLOCK)
        dropSelf(FBlocks.WARPED_ROOTS)
        dropSelf(FBlocks.NETHER_SPROUTS)
        dropSelf(FBlocks.CRIMSON_STEM)
        dropSelf(FBlocks.STRIPPED_CRIMSON_STEM)
        dropSelf(FBlocks.CRIMSON_HYPHAE)
        dropSelf(FBlocks.STRIPPED_CRIMSON_HYPHAE)
        dropSelf(FBlocks.CRIMSON_NYLIUM)
        dropSelf(FBlocks.CRIMSON_FUNGUS)
        dropSelf(FBlocks.SHROOMLIGHT)
        dropSelf(FBlocks.WEEPING_VINES)
        dropSelf(FBlocks.WEEPING_VINES_PLANT)
        dropSelf(FBlocks.TWISTING_VINES)
        dropSelf(FBlocks.TWISTING_VINES_PLANT)
        dropSelf(FBlocks.CRIMSON_ROOTS)
        dropSelf(FBlocks.CRIMSON_PLANKS)
        dropSelf(FBlocks.WARPED_PLANKS)
        dropSelf(FBlocks.CRIMSON_SLAB)
        dropSelf(FBlocks.WARPED_SLAB)
        dropSelf(FBlocks.CRIMSON_PRESSURE_PLATE)
        dropSelf(FBlocks.WARPED_PRESSURE_PLATE)
        dropSelf(FBlocks.NETHERITE_BLOCK)
        dropSelf(FBlocks.ANCIENT_DEBRIS)
        dropSelf(FBlocks.BLACKSTONE)
        dropSelf(FBlocks.BLACKSTONE_STAIRS)
        dropSelf(FBlocks.BLACKSTONE_WALL)
        dropSelf(FBlocks.BLACKSTONE_SLAB)
        dropSelf(FBlocks.POLISHED_BLACKSTONE)
        dropSelf(FBlocks.POLISHED_BLACKSTONE_BRICKS)
        dropSelf(FBlocks.CRACKED_POLISHED_BLACKSTONE_BRICKS)
        dropSelf(FBlocks.CHISELED_POLISHED_BLACKSTONE)
        dropSelf(FBlocks.POLISHED_BLACKSTONE_BRICK_SLAB)
        dropSelf(FBlocks.POLISHED_BLACKSTONE_BRICK_STAIRS)
        dropSelf(FBlocks.POLISHED_BLACKSTONE_BRICK_WALL)
        dropSelf(FBlocks.GILDED_BLACKSTONE)
        dropSelf(FBlocks.POLISHED_BLACKSTONE_STAIRS)
        dropSelf(FBlocks.POLISHED_BLACKSTONE_SLAB)
        dropSelf(FBlocks.POLISHED_BLACKSTONE_PRESSURE_PLATE)
        dropSelf(FBlocks.POLISHED_BLACKSTONE_BUTTON)
        dropSelf(FBlocks.POLISHED_BLACKSTONE_WALL)
        dropSelf(FBlocks.CHISELED_NETHER_BRICKS)
        dropSelf(FBlocks.CRACKED_NETHER_BRICKS)
        dropSelf(FBlocks.QUARTZ_BRICKS)
        dropSelf(FBlocks.CRYING_OBSIDIAN)
        dropSelf(FBlocks.RESPAWN_ANCHOR)
    }

    // "default" loot table
    private fun dropSelf(block: Block) {
        val pool = LootPool.builder()
            .rolls(ConstantRange.of(1))
            .addEntry(ItemLootEntry.builder(block))
            .acceptCondition(SurvivesExplosion.builder())
        tables[block] = LootTable.builder().addLootPool(pool)
    }

    // loot for slabs
    private fun dropSlabs(block: Block) {
        val pool = LootPool.builder()
            .rolls(ConstantRange.of(1))
            .addEntry(ItemLootEntry.builder(block))
            .acceptCondition(SurvivesExplosion.builder())
            .acceptFunction(
                SetCount.builder(ConstantRange.of(2))
                    .acceptCondition(
                        BlockStateProperty.builder(block)
                            .func_227567_a_(
                                StatePropertiesPredicate.Builder.create()
                                    .exactMatch(SlabBlock.TYPE, SlabType.DOUBLE)
                            )
                    )
            )
        tables[block] = LootTable.builder().addLootPool(pool)
    }

    override fun act(cache: DirectoryCache) {
        addLoot()

        val namespacedTables = hashMapOf<ResourceLocation, LootTable>()

        for (entry in tables) {
            namespacedTables[entry.key.lootTable] = entry.value.setParameterSet(LootParameterSets.BLOCK).build()
        }

        writeLootTables(namespacedTables, cache)
    }

    private fun writeLootTables(tables: Map<ResourceLocation, LootTable>, cache: DirectoryCache) {
        val output = gen.outputFolder

        tables.forEach { (key, table) ->
            val path = output.resolve("data/" + key.namespace + "/loot_tables/" + key.path + ".json")
            try {
                IDataProvider.save(GSON, cache, LootTableManager.toJson(table), path)
            } catch (e: Exception) {
                LOGGER.error("Couldn't write loot table $path", e)
            }
        }
    }

    companion object {
        // internal stuff
        private val GSON = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
        private val LOGGER = LogManager.getLogger()
    }
}