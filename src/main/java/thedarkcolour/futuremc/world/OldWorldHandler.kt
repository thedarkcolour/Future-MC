package thedarkcolour.futuremc.world

import com.google.common.collect.ImmutableMap
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
import thedarkcolour.core.util.immutableMapOf

@Suppress("SpellCheckingInspection")
object OldWorldHandler {
    private val TILE_MAPPINGS = immutableMapOf<String, String> { map ->
        map.put("minecraftfuture:containerbarrel", "futuremc:barrel")
        map.put("minecraftfuture:containerblastfurnace", "futuremc:blast_furnace")
        map.put("minecraftfuture:containersmoker", "futuremc:smoker")
        map.put("minecraftfuture:composter", "futuremc:composter")
        map.put("minecraftfuture:beehive", "futuremc:beehive")
        map.put("minecraftfuture:campfire", "futuremc:campfire")
    }

    private val ENTITY_MAPPINGS = immutableMapOf<String, String> { map ->
        map.put("minecraftfuture:bee", "futuremc:bee")
        map.put("minecraftfuture:trident", "futuremc:trident")
        map.put("minecraftfuture:panda", "futuremc:panda")
    }

    private var blocks: ImmutableMap<String, String> = immutableMapOf { map ->
        map.put("minecraftfuture:nether_brick_red_wall", "futuremc:red_nether_brick_wall")
        map.put("minecraftfuture:debugger", "futuremc:debugger")
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
        map.put("minecraftfuture:sweetberry", "futuremc:sweet_berries")
        map.put("minecraftfuture:bee_nest", "futuremc:bee_nest")
        map.put("minecraftfuture:dye", "futuremc:dye")
        map.put("minecraftfuture:loom", "futuremc:loom")
        map.put("minecraftfuture:stonecutter", "futuremc:stonecutter")
        map.put("minecraftfuture:stripped_birch_log", "futuremc:stripped_birch_log")
        map.put("minecraftfuture:grindstone", "futuremc:grindstone")
        map.put("minecraftfuture:honeycomb", "futuremc:honeycomb")
        map.put("minecraftfuture:diorite_wall", "futuremc:diorite_wall")
        map.put("minecraftfuture:lantern", "futuremc:lantern")
        map.put("minecraftfuture:honey_bottle", "futuremc:honey_bottle")
        map.put("minecraftfuture:brick_wall", "futuremc:brick_wall")
        map.put("minecraftfuture:smoothstone", "futuremc:smooth_stone")
        map.put("minecraftfuture:end_stone_wall", "futuremc:end_stone_wall")
        map.put("minecraftfuture:honeycomb_block", "futuremc:honeycomb_block")
        map.put("minecraftfuture:bee_hive", "futuremc:beehive")
        map.put("minecraftfuture:stripped_acacia_log", "futuremc:stripped_acacia_log")
        map.put("minecraftfuture:banner_pattern", "futuremc:banner_pattern")
        map.put("minecraftfuture:composter", "futuremc:composter")
        map.put("minecraftfuture:mossy_stone_wall", "futuremc:mossy_stone_wall")
        map.put("minecraftfuture:trident", "futuremc:trident")
        map.put("minecraftfuture:andesite_wall", "futuremc:andesite_wall")
        map.put("minecraftfuture:stripped_spruce_log", "futuremc:stripped_spruce_log")
        map.put("minecraftfuture:stripped_jungle_log", "futuremc:stripped_jungle_log")
        map.put("minecraftfuture:nether_brick_wall", "futuremc:nether_brick_wall")
        map.put("minecraftfuture:flowerblack", "futuremc:wither_rose")
        map.put("minecraftfuture:blue_ice", "futuremc:blue_ice")
        map.put("minecraftfuture:smoker", "futuremc:smoker")
        map.put("minecraftfuture:suspiciousstew", "futuremc:suspicious_stew")
        map.put("minecraftfuture:stone_brick_wall", "futuremc:stone_brick_wall")
        map.put("minecraftfuture:barrel", "futuremc:barrel")
        map.put("minecraftfuture:blast_furnace", "futuremc:blast_furnace")
        map.put("minecraftfuture:scaffolding", "futuremc:scaffolding")
        map.put("minecraftfuture:flowerwhite", "futuremc:lily_of_the_valley")
        map.put("minecraftfuture:bamboo", "futuremc:bamboo")
        map.put("minecraftfuture:flowerblue", "futuremc:cornflower")
        map.put("minecraftfuture:campfire", "futuremc:campfire")
    }

    private var enchantments: ImmutableMap<String, String> = immutableMapOf { map ->
        map.put("minecraftfuture:riptide", "futuremc:riptide")
        map.put("minecraftfuture:impaling", "futuremc:impaling")
        map.put("minecraftfuture:channeling", "futuremc:channeling")
        map.put("minecraftfuture:loyalty", "futuremc:loyalty")
    }

    private var items: ImmutableMap<String, String> = immutableMapOf { map ->
        map.put("minecraftfuture:nether_brick_red_wall", "futuremc:red_nether_brick_wall")
        map.put("minecraftfuture:granite_wall", "futuremc:granite_wall")
        map.put("minecraftfuture:sandstone_wall", "futuremc:sandstone_wall")
        map.put("minecraftfuture:stripped_oak_log", "futuremc:stripped_oak_log")
        map.put("minecraftfuture:stripped_dark_oak_log", "futuremc:stripped_dark_oak_log")
        map.put("minecraftfuture:smoothquartz", "futuremc:smooth_quartz") // a
        map.put("minecraftfuture:fletchingtable", "futuremc:fletching_table") // a
        map.put("minecraftfuture:prismarine_wall", "futuremc:prismarine_wall")
        map.put("minecraftfuture:honey_block", "futuremc:honey_block")
        map.put("minecraftfuture:smithingtable", "futuremc:smithing_table") // a
        map.put("minecraftfuture:red_sandstone_wall", "futuremc:red_sandstone_wall")
        map.put("minecraftfuture:bee_nest", "futuremc:bee_nest")
        map.put("minecraftfuture:loom", "futuremc:loom")
        map.put("minecraftfuture:stonecutter", "futuremc:stonecutter")
        map.put("minecraftfuture:stripped_birch_log", "futuremc:stripped_birch_log")
        map.put("minecraftfuture:grindstone", "futuremc:grindstone")
        map.put("minecraftfuture:diorite_wall", "futuremc:diorite_wall")
        map.put("minecraftfuture:lantern", "futuremc:lantern")
        map.put("minecraftfuture:brick_wall", "futuremc:brick_wall")
        map.put("minecraftfuture:smoothstone", "futuremc:smooth_stone") // a
        map.put("minecraftfuture:end_stone_wall", "futuremc:end_stone_wall")
        map.put("minecraftfuture:honeycomb_block", "futuremc:honeycomb_block")
        map.put("minecraftfuture:bee_hive", "futuremc:beehive") // a
        map.put("minecraftfuture:stripped_acacia_log", "futuremc:stripped_acacia_log")
        map.put("minecraftfuture:composter", "futuremc:composter")
        map.put("minecraftfuture:mossy_stone_wall", "futuremc:mossy_stone_wall")
        map.put("minecraftfuture:andesite_wall", "futuremc:andesite_wall")
        map.put("minecraftfuture:stripped_spruce_log", "futuremc:stripped_spruce_log")
        map.put("minecraftfuture:stripped_jungle_log", "futuremc:stripped_jungle_log")
        map.put("minecraftfuture:nether_brick_wall", "futuremc:nether_brick_wall")
        map.put("minecraftfuture:flowerblack", "futuremc:wither_rose") // a
        map.put("minecraftfuture:blue_ice", "futuremc:blue_ice")
        map.put("minecraftfuture:smoker", "futuremc:smoker")
        map.put("minecraftfuture:stone_brick_wall", "futuremc:stone_brick_wall")
        map.put("minecraftfuture:barrel", "futuremc:barrel")
        map.put("minecraftfuture:blast_furnace", "futuremc:blast_furnace")
        map.put("minecraftfuture:scaffolding", "futuremc:scaffolding")
        map.put("minecraftfuture:flowerwhite", "futuremc:lily_of_the_valley") // a
        map.put("minecraftfuture:bamboo", "futuremc:bamboo")
        map.put("minecraftfuture:flowerblue", "futuremc:cornflower") // a
        map.put("minecraftfuture:berrybush", "futuremc:sweet_berry_bush") // a
        map.put("minecraftfuture:campfire", "futuremc:campfire")
    }

    // Use getAllMappings because of new modid
    @SubscribeEvent
    fun onOldBlocksLoad(event: MissingMappings<Block>) {
        for (mapping in event.allMappings) {
            if (mapping.key.namespace == "minecraftfuture") {
                try {
                    mapping.remap(ForgeRegistries.BLOCKS.getValue(ResourceLocation(blocks[mapping.key.toString()]!!)))
                } catch (e: NullPointerException) {
                    println(mapping.key)
                }
            }
        }
    }

    @SubscribeEvent
    fun onOldEnchantmentsLoad(event: MissingMappings<Enchantment>) {
        for (mapping in event.allMappings) {
            if (mapping.key.namespace == "minecraftfuture") {
                try {
                    mapping.remap(ForgeRegistries.ENCHANTMENTS.getValue(ResourceLocation(enchantments[mapping.key.toString()]!!)))
                } catch (e: NullPointerException) {
                    println(mapping.key)
                }
            }
        }
    }

    @SubscribeEvent
    fun onOldItemsLoad(event: MissingMappings<Item>) {
        for (mapping in event.allMappings) {
            if (mapping.key.namespace == "minecraftfuture") {
                try {
                    mapping.remap(ForgeRegistries.ITEMS.getValue(ResourceLocation(items[mapping.key.toString()]!!)))
                } catch (e: NullPointerException) {
                    println(mapping.key)
                }
            }
        }
    }

    @SubscribeEvent
    fun onOldEntityLoad(event: MissingMappings<EntityEntry>) {
        for (mapping in event.allMappings) {
            if (mapping.key.namespace == "minecraftfuture") {
                try {
                    mapping.remap(ForgeRegistries.ENTITIES.getValue(ResourceLocation(ENTITY_MAPPINGS[mapping.key.toString()]!!)))
                } catch (e: NullPointerException) {
                    println(mapping.key)
                }
            }
        }
    }

    @SubscribeEvent
    fun onOldSoundLoad(event: MissingMappings<SoundEvent>) {
        for (mapping in event.allMappings) {
            if (mapping.key.namespace == "minecraftfuture") {
                mapping.ignore()
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

    fun remapTile(data: NBTTagCompound, oldID: String) {
        data.setString("id", TILE_MAPPINGS[oldID]!!)
    }
}