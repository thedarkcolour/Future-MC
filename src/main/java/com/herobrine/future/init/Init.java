package com.herobrine.future.init;

import com.herobrine.future.FutureMC;
import com.herobrine.future.block.*;
import com.herobrine.future.item.ItemBamboo;
import com.herobrine.future.item.ItemBannerPattern;
import com.herobrine.future.item.ItemBerry;
import com.herobrine.future.item.ItemCrossBow;
import com.herobrine.future.item.ItemDye;
import com.herobrine.future.item.ItemHoneyBottle;
import com.herobrine.future.item.ItemModeled;
import com.herobrine.future.item.ItemSuspiciousStew;
import com.herobrine.future.item.ItemTrident;
import com.herobrine.future.world.biome.BiomeBambooJungle;
import com.herobrine.future.world.gen.feature.FeatureBambooStalk;
import com.herobrine.future.world.gen.feature.WorldGenBamboo;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import thedarkcolour.core.block.BlockBase;

import java.io.FileNotFoundException;
import java.io.FileReader;

// Not sure if this should be here, i'll test once it compiles
@Mod.EventBusSubscriber
public final class Init {

    // BLOCK
    public static final BlockLantern LANTERN;
    public static final BlockStonecutter STONECUTTER; // TODO - Stonecutter functionality
    public static final BlockBarrel BARREL;
    public static final BlockFurnaceAdvanced SMOKER, BLAST_FURNACE; // TODO - JEI compat
    public static final BlockLoom LOOM;
    public static final BlockRotatable FLETCHING_TABLE;
    public static final BlockRotatable SMITHING_TABLE;
    public static final BlockRotatable CARTOGRAPHY_TABLE;
    public static final BlockGrindstone GRINDSTONE;
    public static final BlockComposter COMPOSTER;
    public static final BlockScaffold SCAFFOLDING;
    public static final BlockBell BELL;

    public static final BlockFlower LILY_OF_VALLEY;
    public static final BlockFlower CORNFLOWER;
    public static final BlockFlower WITHER_ROSE;
    public static final BlockFlower BERRY_BUSH;
    public static final BlockCampfire CAMPFIRE; // TODO - Campfire Functionality
    public static final BlockBamboo BAMBOO_STALK; // TODO - Fix offset by OptiFine
    public static final BlockBeeHive BEE_NEST;

    // TODO - Bark variants
    public static final BlockStrippedLog STRIPPED_ACACIA_LOG;
    public static final BlockStrippedLog STRIPPED_JUNGLE_LOG;
    public static final BlockStrippedLog STRIPPED_BIRCH_LOG;
    public static final BlockStrippedLog STRIPPED_OAK_LOG;
    public static final BlockStrippedLog STRIPPED_SPRUCE_LOG;
    public static final BlockStrippedLog STRIPPED_DARK_OAK_LOG;

    public static final BlockWall BRICK_WALL;
    public static final BlockWall GRANITE_WALL;
    public static final BlockWall ANDESITE_WALL;
    public static final BlockWall DIORITE_WALL;
    public static final BlockWall SANDSTONE_WALL;
    public static final BlockWall RED_SANDSTONE_WALL;
    public static final BlockWall STONE_BRICK_WALL;
    public static final BlockWall MOSSY_STONE_WALL;
    public static final BlockWall NETHER_BRICK_WALL;
    public static final BlockWall RED_NETHER_BRICK_WALL;
    public static final BlockWall END_STONE_WALL;
    public static final BlockWall PRISMARINE_WALL;

    public static final BlockBase SMOOTH_STONE;
    public static final BlockBase SMOOTH_QUARTZ;
    public static final BlockBlueIce BLUE_ICE;

    public static final ItemDye DYES;
    public static final ItemBannerPattern PATTERNS;
    public static final ItemTrident TRIDENT;
    public static final ItemCrossBow CROSSBOW; // TODO - Crossbow shooting
    public static final ItemSuspiciousStew SUSPICIOUS_STEW;
    public static final ItemBerry SWEET_BERRY;
    public static final Item HONEY_COMB;
    public static final Item HONEY_BOTTLE;

    public static final Biome BIOME_BAMBOO_JUNGLE; // TODO - Bamboo Forest
    public static final FeatureBambooStalk BAMBOO_FEATURE;
    public static final ItemBamboo BAMBOO_ITEM;

    public static final boolean isDebug;

    static {
        LANTERN = new BlockLantern();
        STONECUTTER = new BlockStonecutter();
        //LECTERN = new BlockLectern();
        BARREL = new BlockBarrel();
        SMOKER = new BlockFurnaceAdvanced(BlockFurnaceAdvanced.FurnaceType.SMOKER);
        BLAST_FURNACE = new BlockFurnaceAdvanced(BlockFurnaceAdvanced.FurnaceType.BLAST_FURNACE);
        LOOM = new BlockLoom();
        FLETCHING_TABLE = (BlockRotatable) new BlockRotatable("FletchingTable", Material.WOOD, SoundType.WOOD).setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.DECORATIONS : FutureMC.TAB);
        SMITHING_TABLE = (BlockRotatable) new BlockRotatable("SmithingTable", Material.WOOD, SoundType.WOOD).setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.DECORATIONS : FutureMC.TAB);
        CARTOGRAPHY_TABLE = (BlockRotatable) new BlockRotatable("CartographyTable", Material.WOOD, SoundType.WOOD).setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.DECORATIONS : FutureMC.TAB);
        GRINDSTONE = new BlockGrindstone();
        COMPOSTER = new BlockComposter();
        SCAFFOLDING = new BlockScaffold();
        BLUE_ICE = new BlockBlueIce();
        BELL = new BlockBell();

        LILY_OF_VALLEY = new BlockLilyOfValley();
        CORNFLOWER = new BlockCornflower();
        WITHER_ROSE = new BlockWitherRose();
        BERRY_BUSH = new BlockBerryBush();
        CAMPFIRE = new BlockCampfire();
        BAMBOO_STALK = new BlockBamboo();
        BEE_NEST = new BlockBeeHive("bee_nest");

        DYES = new ItemDye();
        PATTERNS = new ItemBannerPattern();
        TRIDENT = new ItemTrident();
        CROSSBOW = new ItemCrossBow("Crossbow");
        SUSPICIOUS_STEW = new ItemSuspiciousStew();
        SWEET_BERRY = new ItemBerry();
        BAMBOO_ITEM = new ItemBamboo();
        HONEY_COMB = new ItemModeled("honeycomb");
        HONEY_BOTTLE = new ItemHoneyBottle();


        STRIPPED_ACACIA_LOG = new BlockStrippedLog("acacia");
        STRIPPED_JUNGLE_LOG = new BlockStrippedLog("jungle");
        STRIPPED_BIRCH_LOG = new BlockStrippedLog("birch");
        STRIPPED_OAK_LOG = new BlockStrippedLog("oak");
        STRIPPED_SPRUCE_LOG = new BlockStrippedLog("spruce");
        STRIPPED_DARK_OAK_LOG = new BlockStrippedLog("dark_oak");

        BRICK_WALL = new BlockWall("brick");
        GRANITE_WALL = new BlockWall("granite");
        ANDESITE_WALL = new BlockWall("andesite");
        DIORITE_WALL = new BlockWall("diorite");
        SANDSTONE_WALL = new BlockWall("sandstone");
        RED_SANDSTONE_WALL = new BlockWall("red_sandstone");
        STONE_BRICK_WALL = new BlockWall("stone_brick");
        MOSSY_STONE_WALL = new BlockWall("mossy_stone");
        NETHER_BRICK_WALL = new BlockWall("nether_brick");
        RED_NETHER_BRICK_WALL = new BlockWall("nether_brick_red");
        END_STONE_WALL = new BlockWall("end_stone");
        PRISMARINE_WALL = new BlockWall("prismarine");

        //ItemNewSlab.Slabs.initSlab();

        SMOOTH_STONE = (BlockBase) new BlockBase("SmoothStone").setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.BUILDING_BLOCKS : FutureMC.TAB);
        SMOOTH_QUARTZ = (BlockBase) new BlockBase("SmoothQuartz").setHardness(2.0F).setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.BUILDING_BLOCKS : FutureMC.TAB);

        BAMBOO_FEATURE = WorldGenBamboo.BAMBOO_FEATURE;
        BIOME_BAMBOO_JUNGLE = new BiomeBambooJungle(false);

        boolean thrown = false;

        try {
            new FileReader("debug_future_mc.txt");
        } catch (FileNotFoundException e) {
            thrown = true;
        }

        isDebug = !thrown;
    }

    public static boolean isCharmItemLoaded(String registryName) {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation("charm:" + registryName)) != Blocks.AIR;
    }
}