package thedarkcolour.futuremc.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import thedarkcolour.core.item.ItemDebugger;
import thedarkcolour.core.item.Modeled;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.compat.oredict.OreDict;
import thedarkcolour.futuremc.item.ItemGroup;
import thedarkcolour.futuremc.tile.TileBarrel;
import thedarkcolour.futuremc.tile.TileBeeHive;
import thedarkcolour.futuremc.tile.TileCampfire;
import thedarkcolour.futuremc.tile.TileComposter;
import thedarkcolour.futuremc.tile.TileFurnaceAdvanced;
import thedarkcolour.futuremc.world.gen.feature.WorldGenBamboo;
import thedarkcolour.futuremc.world.gen.feature.WorldGenFlower;

import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.common.BiomeDictionary.Type;
import static thedarkcolour.futuremc.init.FutureConfig.general;
import static thedarkcolour.futuremc.init.FutureConfig.modFlowers;
import static thedarkcolour.futuremc.init.Init.*;

@Mod.EventBusSubscriber
public final class InitElements {
    public static ArrayList<Modeled> MODELED = new ArrayList<>();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        FutureMC.TAB = FutureConfig.general.useVanillaTabs ? null : new ItemGroup();

        IForgeRegistry<Block> r = event.getRegistry();
        if (general.lantern) r.register(LANTERN);
        if (general.stonecutter) r.register(STONECUTTER);
        if (general.barrel) r.register(BARREL);
        if (general.blastFurnace) r.register(BLAST_FURNACE);
        if (general.smoker) r.register(SMOKER);
        if (general.loom) r.register(LOOM);
        if (general.fletchingTable) r.register(FLETCHING_TABLE);
        if (general.smithingTable) r.register(SMITHING_TABLE);
        if (general.grindstone) r.register(GRINDSTONE);
        //if (general.lectern) r.register(LECTERN);
        if (general.composter) r.register(COMPOSTER);
        if (DEBUG) r.register(SCAFFOLDING);
        if (general.blueIce) r.register(BLUE_ICE);
        if (general.bee) r.registerAll(BEE_NEST, BEE_HIVE, HONEY_BLOCK, HONEYCOMB_BLOCK);

        if (modFlowers.cornflower) r.register(CORNFLOWER);
        if (modFlowers.lily) r.register(LILY_OF_VALLEY);
        if (modFlowers.witherRose) r.register(WITHER_ROSE);
        if (general.berryBush) r.register(SWEET_BERRY_BUSH);
        if (general.campfire) r.register(CAMPFIRE);

        if (general.strippedLogs) r.registerAll(STRIPPED_OAK_LOG, STRIPPED_SPRUCE_LOG, STRIPPED_BIRCH_LOG, STRIPPED_JUNGLE_LOG, STRIPPED_ACACIA_LOG, STRIPPED_DARK_OAK_LOG);
        if (general.newWallVariants) r.registerAll(BRICK_WALL, GRANITE_WALL, ANDESITE_WALL, DIORITE_WALL, SANDSTONE_WALL, RED_SANDSTONE_WALL, STONE_BRICK_WALL, MOSSY_STONE_WALL, NETHER_BRICK_WALL, RED_NETHER_BRICK_WALL, END_STONE_WALL, PRISMARINE_WALL);
        //if (general.newSlabVariants) r.registerAll(ItemNewSlab.Slabs.SLAB_HALVES.toArray(new BlockNewSlab.Half[0]));
        //if (general.newSlabVariants) r.registerAll(ItemNewSlab.Slabs.SLAB_DOUBLES.toArray(new BlockNewSlab.Double[0]));
        if (general.smoothStone) r.register(SMOOTH_STONE);
        if (general.smoothQuartz) r.register(SMOOTH_QUARTZ);
        if (general.bamboo) r.register(BAMBOO_STALK);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();

        if (general.lantern) r.register(makeItemBlock(LANTERN));
        if (general.stonecutter) r.register(makeItemBlock(STONECUTTER));
        if (general.barrel && !Init.isCharmItemLoaded("barrel")) r.register(makeItemBlock(BARREL));
        if (general.blastFurnace) r.register(makeItemBlock(BLAST_FURNACE));
        if (general.smoker) r.register(makeItemBlock(SMOKER));
        if (general.loom) r.register(makeItemBlock(LOOM));
        if (general.fletchingTable) r.register(makeItemBlock(FLETCHING_TABLE));
        if (general.smithingTable) r.register(makeItemBlock(SMITHING_TABLE));
        if (general.grindstone) r.register(makeItemBlock(GRINDSTONE));
        if (general.composter) r.register(makeItemBlock(COMPOSTER));
        if (DEBUG) r.register(makeItemBlock(SCAFFOLDING));
        if (general.blueIce) r.register(makeItemBlock(BLUE_ICE));
        if (general.bee) r.registerAll(makeItemBlock(BEE_NEST), makeItemBlock(BEE_HIVE), makeItemBlock(HONEY_BLOCK), makeItemBlock(HONEYCOMB_BLOCK), HONEY_BOTTLE, HONEY_COMB);

        if (general.trident) r.register(TRIDENT);
        //if (general.crossbow) r.register(CROSSBOW);

        if (modFlowers.lily) r.register(makeItemBlock(LILY_OF_VALLEY));
        if (modFlowers.cornflower) r.register(makeItemBlock(CORNFLOWER));
        if (modFlowers.witherRose) r.register(makeItemBlock(WITHER_ROSE));
        if (modFlowers.suspiciousStew) r.register(SUSPICIOUS_STEW);
        if (general.loom && DEBUG) r.register(PATTERNS);
        if (modFlowers.dyes) r.register(DYES);
        if (general.bamboo) r.register(BAMBOO_ITEM);
        if (general.berryBush) r.register(SWEET_BERRY);
        if (general.campfire) r.register(makeItemBlock(CAMPFIRE));

        if (general.strippedLogs) r.registerAll(makeItemBlocks(STRIPPED_OAK_LOG, STRIPPED_SPRUCE_LOG, STRIPPED_BIRCH_LOG, STRIPPED_JUNGLE_LOG, STRIPPED_ACACIA_LOG, STRIPPED_DARK_OAK_LOG));
        if (general.smoothStone) r.register(makeItemBlock(SMOOTH_STONE));
        if (general.smoothQuartz) r.register(makeItemBlock(SMOOTH_QUARTZ));
        if (general.newWallVariants) r.registerAll(makeItemBlocks(BRICK_WALL, GRANITE_WALL, ANDESITE_WALL, DIORITE_WALL, SANDSTONE_WALL, RED_SANDSTONE_WALL, STONE_BRICK_WALL, MOSSY_STONE_WALL, NETHER_BRICK_WALL, RED_NETHER_BRICK_WALL, END_STONE_WALL, PRISMARINE_WALL));
        //if (general.newSlabVariants) r.registerAll(ItemNewSlab.Slabs.SLAB_ITEMS.toArray(new ItemNewSlab[0]));

        if (DEBUG) r.register(new ItemDebugger());

        registerTileEntities();
        OreDict.registerOres();
    }

    public static void registerTileEntities() {
        //if (general.stonecutter) GameRegistry.registerTileEntity(TileStonecutter.class, new ResourceLocation(FutureMC.ID, "stonecutter"));
        if (general.barrel) GameRegistry.registerTileEntity(TileBarrel.class, new ResourceLocation(FutureMC.ID, "containerBarrel"));
        if (general.blastFurnace) GameRegistry.registerTileEntity(TileFurnaceAdvanced.TileBlastFurnace.class, new ResourceLocation(FutureMC.ID, "containerBlastFurnace"));
        if (general.smoker) GameRegistry.registerTileEntity(TileFurnaceAdvanced.TileSmoker.class, new ResourceLocation(FutureMC.ID, "containerSmoker"));
        if (general.composter) GameRegistry.registerTileEntity(TileComposter.class, new ResourceLocation(FutureMC.ID, "composter"));
        if (general.bee) GameRegistry.registerTileEntity(TileBeeHive.class, new ResourceLocation(FutureMC.ID, "beehive"));
        if (general.campfire) GameRegistry.registerTileEntity(TileCampfire.class, new ResourceLocation(FutureMC.ID, "campfire"));
        //if (general.bell) GameRegistry.registerTileEntity(TileBell.class, new ResourceLocation(FutureMC.ID, "bell"));
    }

    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> event) {
        if (general.addLegacyBambooJungle) registerBiome(event, BIOME_BAMBOO_JUNGLE, "bamboo_forest", BiomeManager.BiomeType.WARM, Type.JUNGLE, Type.HOT, Type.WET, Type.DENSE, Type.RARE, Type.FOREST);
        /*if (general.bee) {
            //event.getRegistry().register(BIOME_PLAINS_WITH_NESTS.setRegistryName("minecraft:plains"));
            //event.getRegistry().register(BIOME_SUNFLOWER_PLAINS_WITH_NESTS.setRegistryName("minecraft:mutated_plains"));
            event.getRegistry().register(BIOME_FLOWER_FOREST_WITH_NESTS.setRegistryName("minecraft:mutated_forest"));

            //BiomeProvider.allowedBiomes.remove(Biomes.PLAINS);
            //BiomeProvider.allowedBiomes.add(BIOME_PLAINS_WITH_NESTS);

            //BiomeDictionary.addTypes(BIOME_PLAINS_WITH_NESTS, Type.PLAINS);
            //BiomeDictionary.addTypes(BIOME_SUNFLOWER_PLAINS_WITH_NESTS, Type.PLAINS, Type.RARE);
            //BiomeDictionary.addTypes(BIOME_FLOWER_FOREST_WITH_NESTS, Type.FOREST, Type.HILLS, Type.RARE);
        }*/
        //if (general.bee) registerBiome(event, BIOME_PLAINS_WITH_NESTS, "minecraft:plains", new BiomeManager.BiomeType[]{BiomeManager.BiomeType.WARM, BiomeManager.BiomeType.COOL}, Type.PLAINS);
        //if (general.bee) registerBiome(event, BIOME_SUNFLOWER_PLAINS_WITH_NESTS, "minecraft:mutated_plains", new BiomeManager.BiomeType[]{BiomeManager.BiomeType.WARM, BiomeManager.BiomeType.COOL}, Type.PLAINS);
        //if (general.bee) registerBiome(event, BIOME_FLOWER_FOREST_WITH_NESTS, "minecraft:mutated_forest", new BiomeManager.BiomeType[]{BiomeManager.BiomeType.WARM, BiomeManager.BiomeType.COOL}, Type.PLAINS);
    }

    public static void registerBiome(RegistryEvent.Register<Biome> event, Biome biome, String name, BiomeManager.BiomeType type, Type... types) {
        biome.setRegistryName(name);
        event.getRegistry().register(biome);
        BiomeDictionary.addTypes(biome, types);
        BiomeManager.addBiome(type, new BiomeManager.BiomeEntry(biome, 50));
        BiomeManager.addSpawnBiome(biome);
    }

    public static Item makeItemBlock(Block block) {
        return new ItemModeledBlock(block).setRegistryName(block.getRegistryName());
    }

    public static Item[] makeItemBlocks(Block... blocks) {
        List<Item> list = new ArrayList<>();

        for(Block block : blocks) {
            list.add(makeItemBlock(block));
        }
        return list.toArray(new Item[0]);
    }

    public static void registerGenerators() {
        if (modFlowers.lily && modFlowers.lilyGen) {
            GameRegistry.registerWorldGenerator(new WorldGenFlower(LILY_OF_VALLEY), 0);
        }

        if (modFlowers.cornflower && modFlowers.cornflowerGen) {
            GameRegistry.registerWorldGenerator(new WorldGenFlower(CORNFLOWER), 0);
        }

        if (general.berryBush && general.berryBushGen) {
            GameRegistry.registerWorldGenerator(new WorldGenFlower(SWEET_BERRY_BUSH), 0);
        }

        if (general.bamboo && general.bambooSpawnsInJungles) {
            GameRegistry.registerWorldGenerator(new WorldGenBamboo(), 0);
        }
    }

    public static class ItemModeledBlock extends ItemBlock implements Modeled {
        public ItemModeledBlock(Block block) {
            super(block);
            addModel();
        }
    }
}