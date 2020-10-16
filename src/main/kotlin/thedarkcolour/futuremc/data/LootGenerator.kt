package thedarkcolour.futuremc.data

import net.minecraft.advancements.criterion.StatePropertiesPredicate
import net.minecraft.block.Block
import net.minecraft.block.SlabBlock
import net.minecraft.data.DataGenerator
import net.minecraft.data.LootTableProvider
import net.minecraft.state.properties.SlabType
import net.minecraft.world.storage.loot.ConstantRange
import net.minecraft.world.storage.loot.ItemLootEntry
import net.minecraft.world.storage.loot.LootPool
import net.minecraft.world.storage.loot.LootTable
import net.minecraft.world.storage.loot.conditions.BlockStateProperty
import net.minecraft.world.storage.loot.conditions.SurvivesExplosion
import net.minecraft.world.storage.loot.functions.SetCount

class LootGenerator(private val gen: DataGenerator) : LootTableProvider(gen) {
    private val tables = hashMapOf<Block, LootTable.Builder>()

    private fun addLoot() {

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
}