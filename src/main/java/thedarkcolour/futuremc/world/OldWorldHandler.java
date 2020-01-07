package thedarkcolour.futuremc.world;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import static thedarkcolour.core.util.Util.immutableMapOf;

@SuppressWarnings("SpellCheckingInspection")
public final class OldWorldHandler {
    private static final ImmutableMap<String, String> TILE_MAPPINGS = immutableMapOf(map -> {
        map.put("minecraftfuture:containerbarrel", "futuremc:barrel");
        map.put("minecraftfuture:containerblastfurnace", "futuremc:blast_furnace");
        map.put("minecraftfuture:containersmoker", "futuremc:smoker");
        map.put("minecraftfuture:composter", "futuremc:composter");
        map.put("minecraftfuture:beehive", "futuremc:beehive");
        map.put("minecraftfuture:campfire", "futuremc:campfire");
    });
    private static final ImmutableMap<String, String> ENTITY_MAPPINGS = immutableMapOf(map -> {
        map.put("minecraftfuture:bee", "futuremc:bee");
        map.put("minecraftfuture:trident", "futuremc:trident");
        map.put("minecraftfuture:panda", "futuremc:panda");
    });

    private static ImmutableMap<String, String> blocks;
    private static ImmutableMap<String, String> enchantments;
    private static ImmutableMap<String, String> items;

    private static ImmutableMap<String, String> getBlockMappings() {
        if (blocks != null) {
            return blocks;
        }
        return blocks = immutableMapOf(map -> {
            map.put("minecraftfuture:nether_brick_red_wall", "futuremc:red_nether_brick_wall");
            map.put("minecraftfuture:granite_wall", "futuremc:granite_wall");
            map.put("minecraftfuture:sandstone_wall", "futuremc:sandstone_wall");
            map.put("minecraftfuture:stripped_oak_log", "futuremc:stripped_oak_log");
            map.put("minecraftfuture:stripped_dark_oak_log", "futuremc:stripped_dark_oak_log");
            map.put("minecraftfuture:smoothquartz", "futuremc:smooth_quartz"); // a
            map.put("minecraftfuture:fletchingtable", "futuremc:fletching_table"); // a
            map.put("minecraftfuture:prismarine_wall", "futuremc:prismarine_wall");
            map.put("minecraftfuture:honey_block", "futuremc:honey_block");
            map.put("minecraftfuture:smithingtable", "futuremc:smithing_table"); // a
            map.put("minecraftfuture:red_sandstone_wall", "futuremc:red_sandstone_wall");
            map.put("minecraftfuture:bee_nest", "futuremc:bee_nest");
            map.put("minecraftfuture:loom", "futuremc:loom");
            map.put("minecraftfuture:stonecutter", "futuremc:stonecutter");
            map.put("minecraftfuture:stripped_birch_log", "futuremc:stripped_birch_log");
            map.put("minecraftfuture:grindstone", "futuremc:grindstone");
            map.put("minecraftfuture:diorite_wall", "futuremc:diorite_wall");
            map.put("minecraftfuture:lantern", "futuremc:lantern");
            map.put("minecraftfuture:brick_wall", "futuremc:brick_wall");
            map.put("minecraftfuture:smoothstone", "futuremc:smooth_stone"); // a
            map.put("minecraftfuture:end_stone_wall", "futuremc:end_stone_wall");
            map.put("minecraftfuture:honeycomb_block", "futuremc:honeycomb_block");
            map.put("minecraftfuture:bee_hive", "futuremc:beehive"); // a
            map.put("minecraftfuture:stripped_acacia_log", "futuremc:stripped_acacia_log");
            map.put("minecraftfuture:composter", "futuremc:composter");
            map.put("minecraftfuture:mossy_stone_wall", "futuremc:mossy_stone_wall");
            map.put("minecraftfuture:andesite_wall", "futuremc:andesite_wall");
            map.put("minecraftfuture:stripped_spruce_log", "futuremc:stripped_spruce_log");
            map.put("minecraftfuture:stripped_jungle_log", "futuremc:stripped_jungle_log");
            map.put("minecraftfuture:nether_brick_wall", "futuremc:nether_brick_wall");
            map.put("minecraftfuture:flowerblack", "futuremc:wither_rose"); // a
            map.put("minecraftfuture:blue_ice", "futuremc:blue_ice");
            map.put("minecraftfuture:smoker", "futuremc:smoker");
            map.put("minecraftfuture:stone_brick_wall", "futuremc:stone_brick_wall");
            map.put("minecraftfuture:barrel", "futuremc:barrel");
            map.put("minecraftfuture:blast_furnace", "futuremc:blast_furnace");
            map.put("minecraftfuture:scaffolding", "futuremc:scaffolding");
            map.put("minecraftfuture:flowerwhite", "futuremc:lily_of_the_valley"); // a
            map.put("minecraftfuture:bamboo", "futuremc:bamboo");
            map.put("minecraftfuture:flowerblue", "futuremc:cornflower"); // a
            map.put("minecraftfuture:berrybush", "futuremc:sweet_berry_bush"); // a
            map.put("minecraftfuture:campfire", "futuremc:campfire");
        });
    }

    private static ImmutableMap<String, String> getEnchantmentMappings() {
        if (enchantments != null) {
            return enchantments;
        }
        return enchantments = immutableMapOf(map -> {
            map.put("minecraftfuture:riptide", "futuremc:riptide");
            map.put("minecraftfuture:impaling", "futuremc:impaling");
            map.put("minecraftfuture:channeling", "futuremc:channeling");
            map.put("minecraftfuture:loyalty", "futuremc:loyalty");
        });
    }

    private static ImmutableMap<String, String> getItemMappings() {
        if (items != null) {
            return items;
        }
        return items = immutableMapOf(map -> {
            map.put("minecraftfuture:nether_brick_red_wall", "futuremc:red_nether_brick_wall");
            map.put("minecraftfuture:debugger", "futuremc:debugger");
            map.put("minecraftfuture:granite_wall", "futuremc:granite_wall");
            map.put("minecraftfuture:sandstone_wall", "futuremc:sandstone_wall");
            map.put("minecraftfuture:stripped_oak_log", "futuremc:stripped_oak_log");
            map.put("minecraftfuture:stripped_dark_oak_log", "futuremc:stripped_dark_oak_log");
            map.put("minecraftfuture:smoothquartz", "futuremc:smooth_quartz");
            map.put("minecraftfuture:fletchingtable", "futuremc:fletching_table");
            map.put("minecraftfuture:prismarine_wall", "futuremc:prismarine_wall");
            map.put("minecraftfuture:honey_block", "futuremc:honey_block");
            map.put("minecraftfuture:smithingtable", "futuremc:smithing_table");
            map.put("minecraftfuture:red_sandstone_wall", "futuremc:red_sandstone_wall");
            map.put("minecraftfuture:sweetberry", "futuremc:sweet_berries");
            map.put("minecraftfuture:bee_nest", "futuremc:bee_nest");
            map.put("minecraftfuture:dye", "futuremc:dye");
            map.put("minecraftfuture:loom", "futuremc:loom");
            map.put("minecraftfuture:stonecutter", "futuremc:stonecutter");
            map.put("minecraftfuture:stripped_birch_log", "futuremc:stripped_birch_log");
            map.put("minecraftfuture:grindstone", "futuremc:grindstone");
            map.put("minecraftfuture:honeycomb", "futuremc:honeycomb");
            map.put("minecraftfuture:diorite_wall", "futuremc:diorite_wall");
            map.put("minecraftfuture:lantern", "futuremc:lantern");
            map.put("minecraftfuture:honey_bottle", "futuremc:honey_bottle");
            map.put("minecraftfuture:brick_wall", "futuremc:brick_wall");
            map.put("minecraftfuture:smoothstone", "futuremc:smooth_stone");
            map.put("minecraftfuture:end_stone_wall", "futuremc:end_stone_wall");
            map.put("minecraftfuture:honeycomb_block", "futuremc:honeycomb_block");
            map.put("minecraftfuture:bee_hive", "futuremc:beehive");
            map.put("minecraftfuture:stripped_acacia_log", "futuremc:stripped_acacia_log");
            map.put("minecraftfuture:banner_pattern", "futuremc:banner_pattern");
            map.put("minecraftfuture:composter", "futuremc:composter");
            map.put("minecraftfuture:mossy_stone_wall", "futuremc:mossy_stone_wall");
            map.put("minecraftfuture:trident", "futuremc:trident");
            map.put("minecraftfuture:andesite_wall", "futuremc:andesite_wall");
            map.put("minecraftfuture:stripped_spruce_log", "futuremc:stripped_spruce_log");
            map.put("minecraftfuture:stripped_jungle_log", "futuremc:stripped_jungle_log");
            map.put("minecraftfuture:nether_brick_wall", "futuremc:nether_brick_wall");
            map.put("minecraftfuture:flowerblack", "futuremc:wither_rose");
            map.put("minecraftfuture:blue_ice", "futuremc:blue_ice");
            map.put("minecraftfuture:smoker", "futuremc:smoker");
            map.put("minecraftfuture:suspiciousstew", "futuremc:suspicious_stew");
            map.put("minecraftfuture:stone_brick_wall", "futuremc:stone_brick_wall");
            map.put("minecraftfuture:barrel", "futuremc:barrel");
            map.put("minecraftfuture:blast_furnace", "futuremc:blast_furnace");
            map.put("minecraftfuture:scaffolding", "futuremc:scaffolding");
            map.put("minecraftfuture:flowerwhite", "futuremc:lily_of_the_valley");
            map.put("minecraftfuture:bamboo", "futuremc:bamboo");
            map.put("minecraftfuture:flowerblue", "futuremc:cornflower");
            map.put("minecraftfuture:campfire", "futuremc:campfire");
        });
    }

    @SubscribeEvent
    public static void onOldBlocksLoad(RegistryEvent.MissingMappings<Block> event) {
        for (RegistryEvent.MissingMappings.Mapping<Block> mapping : event.getAllMappings()) {
            if (mapping.key.getNamespace().equals("minecraftfuture")) {
                try {
                    mapping.remap(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(getBlockMappings().get(mapping.key.toString()))));
                } catch (NullPointerException e) {
                    System.out.println(mapping.key);
                }
            }
        }
        blocks = null;
    }

    @SubscribeEvent
    public static void onOldEnchantmentsLoad(RegistryEvent.MissingMappings<Enchantment> event) {
        for (RegistryEvent.MissingMappings.Mapping<Enchantment> mapping : event.getAllMappings()) {
            if (mapping.key.getNamespace().equals("minecraftfuture")) {
                try {
                    mapping.remap(ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(getEnchantmentMappings().get(mapping.key.toString()))));
                } catch (NullPointerException e) {
                    System.out.println(mapping.key);
                }
            }
        }
        enchantments = null;
    }

    @SubscribeEvent
    public static void onOldItemsLoad(RegistryEvent.MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getAllMappings()) {
            if (mapping.key.getNamespace().equals("minecraftfuture")) {
                try {
                    mapping.remap(ForgeRegistries.ITEMS.getValue(new ResourceLocation(getItemMappings().get(mapping.key.toString()))));
                } catch (NullPointerException e) {
                    System.out.println(mapping.key);
                }
            }
        }
        items = null;
    }

    @SubscribeEvent
    public static void onOldEntityLoad(RegistryEvent.MissingMappings<EntityEntry> event) {
        for (RegistryEvent.MissingMappings.Mapping<EntityEntry> mapping : event.getAllMappings()) {
            if (mapping.key.getNamespace().equals("minecraftfuture")) {
                try {
                    mapping.remap(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(ENTITY_MAPPINGS.get(mapping.key.toString()))));
                } catch (NullPointerException e) {
                    System.out.println(mapping.key);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onOldSoundLoad(RegistryEvent.MissingMappings<SoundEvent> event) {
        for (RegistryEvent.MissingMappings.Mapping<SoundEvent> mapping : event.getAllMappings()) {
            if (mapping.key.getNamespace().equals("minecraftfuture")) {
                mapping.ignore();
            }
        }
    }

    @SubscribeEvent
    public static void onChunkEntityLoad(ChunkDataEvent event) {
        NBTTagList tileEntities = event.getData().getCompoundTag("Level").getTagList("TileEntities", 10);
        Chunk chunk = event.getChunk();

        for (int i = 0; i < tileEntities.tagCount(); ++i) {
            NBTTagCompound data = tileEntities.getCompoundTagAt(i);
            String id = data.getString("id");

            if (id.startsWith("minecraftfuture")) {
                BlockPos pos = new BlockPos(data.getInteger("x"), data.getInteger("y"), data.getInteger("z"));
                remapTile(data, id);
                chunk.removeTileEntity(pos);
                chunk.addTileEntity(pos, TileEntity.create(chunk.getWorld(), data));
            }
        }
    }

    public static void remapTile(NBTTagCompound data, String oldID) {
        data.setString("id", TILE_MAPPINGS.get(oldID));
    }
}