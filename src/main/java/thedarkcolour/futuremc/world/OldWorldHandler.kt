package thedarkcolour.futuremc.world

import net.minecraft.block.Block
import net.minecraft.enchantment.Enchantment
import net.minecraft.item.Item
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraftforge.event.RegistryEvent.MissingMappings
import net.minecraftforge.event.world.ChunkDataEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.EntityEntry
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.IForgeRegistryEntry
import thedarkcolour.core.util.immutableMapOf

@Suppress("SpellCheckingInspection")
object OldWorldHandler {
    private var BLOCK_MAPPINGS = immutableMapOf<String, String> { map ->
        map.put("minecraftfuture:nether_brick_red_wall", "futuremc:red_nether_brick_wall")
        map.put("minecraftfuture:granite_wall", "futuremc:granite_wall")
        map.put("minecraftfuture:sandstone_wall", "futuremc:sandstone_wall")
        map.put("minecraftfuture:stripped_oak_log", "futuremc:stripped_oak_log")
        map.put("minecraftfuture:stripped_dark_oak_log", "futuremc:stripped_dark_oak_log")
        map.put("minecraftfuture:smoothquartz", "futuremc:smooth_quartz")
        map.put("minecraftfuture:fletchingtable", "futuremc:fletching_table")
        map.put("minecraftfuture:prismarine_wall", "futuremc:prismarine_wall")
        map.put("minecraftfuture:honey_block", "futuremc:honey_block")
        map.put("minecraftfuture:smithingtable", "futuremc:smithing_table")
        map.put("minecraftfuture:red_sandstone_wall", "futuremc:red_sandstone_wall")
        map.put("minecraftfuture:bee_nest", "futuremc:bee_nest")
        map.put("minecraftfuture:loom", "futuremc:loom")
        map.put("minecraftfuture:stonecutter", "futuremc:stonecutter")
        map.put("minecraftfuture:stripped_birch_log", "futuremc:stripped_birch_log")
        map.put("minecraftfuture:grindstone", "futuremc:grindstone")
        map.put("minecraftfuture:diorite_wall", "futuremc:diorite_wall")
        map.put("minecraftfuture:lantern", "futuremc:lantern")
        map.put("minecraftfuture:brick_wall", "futuremc:brick_wall")
        map.put("minecraftfuture:smoothstone", "futuremc:smooth_stone")
        map.put("minecraftfuture:end_stone_wall", "futuremc:end_stone_brick_wall")
        map.put("minecraftfuture:honeycomb_block", "futuremc:honeycomb_block")
        map.put("minecraftfuture:bee_hive", "futuremc:beehive")
        map.put("minecraftfuture:stripped_acacia_log", "futuremc:stripped_acacia_log")
        map.put("minecraftfuture:banner_pattern", "futuremc:banner_pattern")
        map.put("minecraftfuture:composter", "futuremc:composter")
        map.put("minecraftfuture:mossy_stone_wall", "futuremc:mossy_stone_brick_wall")
        map.put("minecraftfuture:andesite_wall", "futuremc:andesite_wall")
        map.put("minecraftfuture:stripped_spruce_log", "futuremc:stripped_spruce_log")
        map.put("minecraftfuture:stripped_jungle_log", "futuremc:stripped_jungle_log")
        map.put("minecraftfuture:nether_brick_wall", "futuremc:nether_brick_wall")
        map.put("minecraftfuture:flowerblack", "futuremc:wither_rose")
        map.put("minecraftfuture:blue_ice", "futuremc:blue_ice")
        map.put("minecraftfuture:smoker", "futuremc:smoker")
        map.put("minecraftfuture:stone_brick_wall", "futuremc:stone_brick_wall")
        map.put("minecraftfuture:barrel", "futuremc:barrel")
        map.put("minecraftfuture:blast_furnace", "futuremc:blast_furnace")
        map.put("minecraftfuture:scaffolding", "futuremc:scaffolding")
        map.put("minecraftfuture:flowerwhite", "futuremc:lily_of_the_valley")
        map.put("minecraftfuture:bamboo", "futuremc:bamboo")
        map.put("minecraftfuture:flowerblue", "futuremc:cornflower")
        map.put("minecraftfuture:campfire", "futuremc:campfire")
        map.put("futuremc:end_stone_wall", "futuremc:end_stone_brick_wall")
        map.put("futuremc:mossy_stone_wall", "futuremc:mossy_stone_brick_wall")
        map.put("minecraftfuture:berrybush", "futuremc:sweet_berry_bush")
    }

    private var ENCHANTMENT_MAPPINGS = immutableMapOf<String, String> { map ->
        map.put("minecraftfuture:riptide", "futuremc:riptide")
        map.put("minecraftfuture:impaling", "futuremc:impaling")
        map.put("minecraftfuture:channeling", "futuremc:channeling")
        map.put("minecraftfuture:loyalty", "futuremc:loyalty")
    }

    private val ENTITY_MAPPINGS = immutableMapOf<String, String> { map ->
        map.put("minecraftfuture:bee", "futuremc:bee")
        map.put("minecraftfuture:trident", "futuremc:trident")
        map.put("minecraftfuture:panda", "futuremc:panda")
    }

    private var ITEM_MAPPINGS = immutableMapOf<String, String> { map ->
        map.putAll(BLOCK_MAPPINGS)
        map.put("minecraftfuture:dye", "futuremc:dye")
        map.put("minecraftfuture:honeycomb", "futuremc:honeycomb")
        map.put("minecraftfuture:sweetberry", "futuremc:sweet_berries")
        map.put("minecraftfuture:trident", "futuremc:trident")
        map.put("minecraftfuture:honey_bottle", "futuremc:honey_bottle")
        map.put("minecraftfuture:suspiciousstew", "futuremc:suspicious_stew")
        map.put("minecraftfuture:debugger", "futuremc:debugger")
    }

    private val TILE_MAPPINGS = immutableMapOf<String, String> { map ->
        map.put("minecraftfuture:containerbarrel", "futuremc:barrel")
        map.put("minecraftfuture:containerblastfurnace", "futuremc:blast_furnace")
        map.put("minecraftfuture:containersmoker", "futuremc:smoker")
        map.put("minecraftfuture:composter", "futuremc:composter")
        map.put("minecraftfuture:beehive", "futuremc:beehive")
        map.put("minecraftfuture:campfire", "futuremc:campfire")
    }

    // Use getAllMappings because of new modid
    @SubscribeEvent
    fun onOldBlocksLoad(event: MissingMappings<Block>) {
        remap(event, BLOCK_MAPPINGS, ForgeRegistries.BLOCKS)
    }

    @SubscribeEvent
    fun onOldEnchantmentsLoad(event: MissingMappings<Enchantment>) {
        remap(event, ENCHANTMENT_MAPPINGS, ForgeRegistries.ENCHANTMENTS)
    }

    @SubscribeEvent
    fun onOldEntityLoad(event: MissingMappings<EntityEntry>) {
        remap(event, ENTITY_MAPPINGS, ForgeRegistries.ENTITIES)
    }

    @SubscribeEvent
    fun onOldItemsLoad(event: MissingMappings<Item>) {
        remap(event, ITEM_MAPPINGS, ForgeRegistries.ITEMS)
    }

    @SubscribeEvent
    fun onOldSoundLoad(event: MissingMappings<SoundEvent>) {
        for (mapping in event.allMappings) {
            if (mapping.key.namespace == "minecraftfuture") {
                mapping.ignore()
            }
        }
    }

    private fun <T : IForgeRegistryEntry<T>> remap(event: MissingMappings<T>, mappings: Map<String, String>, registry: IForgeRegistry<T>) {
        for (mapping in event.allMappings) {
            val key = mapping.key
            val path = key.path

            if (path == "scaffolding" || path == "debugger") mapping.ignore()

            if (key.namespace == "minecraftfuture") {
                val r = registry.getValue(ResourceLocation(mappings[key.toString()] ?: continue))

                mapping.remap(r)
            }
        }
    }

    @SubscribeEvent
    fun onChunkEntityLoad(event: ChunkDataEvent) {
        val tileEntities = event.data.getCompoundTag("Level").getTagList("TileEntities", 10)
        val chunk = event.chunk
        for (i in 0 until tileEntities.tagCount()) {
            val data = tileEntities.getCompoundTagAt(i)
            val id = data.getString("id")
            if (id.startsWith("minecraftfuture")) {
                val pos = BlockPos(data.getInteger("x"), data.getInteger("y"), data.getInteger("z"))
                remapTile(data, id)
                chunk.removeTileEntity(pos)
                chunk.addTileEntity(pos, TileEntity.create(chunk.world, data)!!)
            }
        }
    }

    private fun remapTile(data: NBTTagCompound, oldID: String) {
        data.setString("id", TILE_MAPPINGS[oldID]!!)
    }
}