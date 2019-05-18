package com.herobrine.future.init;

import com.herobrine.future.blocks.*;
import com.herobrine.future.config.FutureConfig;
import com.herobrine.future.items.*;
import com.herobrine.future.worldgen.WorldGenBamboo;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;

public final class Init {
    public static CreativeTabs FUTURE_MC_TAB = FutureConfig.general.useVanillaTabs ? null : new CreativeTabs("Future") {
        @Override    //New creative tab
        public ItemStack getTabIconItem() {
            return new ItemStack(Init.LANTERN);
        }
    };

    public static final String MODID = "minecraftfuture";

    public static final BlockLantern LANTERN;
    public static final BlockStonecutter STONECUTTER; // TODO - Stonecutter functionality
    //public static final BlockLectern LECTERN; // TODO - Lectern
    public static final BlockBarrel BARREL;
    public static final BlockFurnaceAdvanced SMOKER, BLAST_FURNACE; // TODO - JEI compat
    public static final BlockRotatable LOOM; // TODO - Loom functionality
    public static final BlockRotatable FLETCHING_TABLE;
    public static final BlockRotatable SMITHING_TABLE;
    public static final BlockGrindstone GRINDSTONE;
    public static final BlockComposter COMPOSTER; // TODO - CraftTweaker compat

    public static final BlockFlower LILY_OF_VALLEY;
    public static final BlockFlower CORNFLOWER;
    public static final BlockFlower WITHER_ROSE;
    public static final BlockFlower BERRY_BUSH;
    public static final BlockCampfire CAMPFIRE; // TODO - Campfire Functionality
    public static final BlockBamboo BAMBOO_STALK;

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

    public static final ItemDye DYES;
    public static final ItemTrident TRIDENT;
    public static final ItemCrossBow CROSSBOW; // TODO - Crossbow shooting
    public static final ItemSuspiciousStew SUSPICIOUS_STEW;
    public static final ItemBerry SWEET_BERRY;

    public static final Biome BIOME_BAMBOO_JUNGLE;
    public static final WorldGenBamboo BAMBOO_FEATURE;

    static {  // references
        LANTERN = new BlockLantern();
        STONECUTTER = new BlockStonecutter();
        //LECTERN = new BlockLectern();
        BARREL = new BlockBarrel();
        SMOKER = new BlockFurnaceAdvanced(BlockFurnaceAdvanced.FurnaceType.SMOKER);
        BLAST_FURNACE = new BlockFurnaceAdvanced(BlockFurnaceAdvanced.FurnaceType.BLAST_FURNACE);
        LOOM = (BlockRotatable) new BlockRotatable(new BlockProperties("Loom", Material.WOOD, SoundType.WOOD)).setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.DECORATIONS : FUTURE_MC_TAB);
        FLETCHING_TABLE = (BlockRotatable) new BlockRotatable(new BlockProperties("FletchingTable", Material.WOOD, SoundType.WOOD)).setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.DECORATIONS : FUTURE_MC_TAB);
        SMITHING_TABLE = (BlockRotatable) new BlockRotatable(new BlockProperties("SmithingTable", Material.WOOD, SoundType.WOOD)).setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.DECORATIONS : FUTURE_MC_TAB);
        GRINDSTONE = new BlockGrindstone();
        COMPOSTER = new BlockComposter();

        LILY_OF_VALLEY = new BlockLilyOfValley();
        CORNFLOWER = new BlockCornflower();
        WITHER_ROSE = new BlockWitherRose();
        BERRY_BUSH = new BlockBerryBush();
        CAMPFIRE = new BlockCampfire();
        BAMBOO_STALK = new BlockBamboo();

        DYES = new ItemDye();
        TRIDENT = new ItemTrident();
        CROSSBOW = new ItemCrossBow("Crossbow");
        SUSPICIOUS_STEW = new ItemSuspiciousStew();
        SWEET_BERRY = new ItemBerry();

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

        ItemNewSlab.Slabs.initSlab();

        SMOOTH_STONE = (BlockBase) new BlockBase(new BlockProperties("SmoothStone")).setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.BUILDING_BLOCKS : Init.FUTURE_MC_TAB);
        SMOOTH_QUARTZ = (BlockBase) new BlockBase(new BlockProperties("SmoothQuartz")).setHardness(2.0F).setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.BUILDING_BLOCKS : Init.FUTURE_MC_TAB);

        BIOME_BAMBOO_JUNGLE = WorldGenBamboo.BIOME_BAMBOO_JUNGLE;
        BAMBOO_FEATURE = WorldGenBamboo.BAMBOO_FEATURE;
    }
}