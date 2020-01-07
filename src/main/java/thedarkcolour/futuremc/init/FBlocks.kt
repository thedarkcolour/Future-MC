package thedarkcolour.futuremc.init

import net.minecraft.block.Block
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraftforge.fml.common.registry.ForgeRegistries
import thedarkcolour.core.block.BlockBase
import thedarkcolour.core.block.RotatableBlock
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.block.*
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.tile.*

object FBlocks {
    lateinit var LANTERN: Block private set
    lateinit var STONECUTTER: Block private set
    lateinit var BARREL: Block private set
    lateinit var SMOKER: Block private set
    lateinit var BLAST_FURNACE: Block private set
    lateinit var LOOM: Block private set
    lateinit var FLETCHING_TABLE: Block private set
    lateinit var SMITHING_TABLE: Block private set
    lateinit var CARTOGRAPHY_TABLE: Block private set
    lateinit var GRINDSTONE: Block private set
    lateinit var COMPOSTER: Block private set
    lateinit var HONEY_BLOCK: Block private set
    lateinit var HONEYCOMB_BLOCK: Block private set
    lateinit var LILY_OF_THE_VALLEY: Block private set
    lateinit var CORNFLOWER: Block private set
    lateinit var WITHER_ROSE: Block private set
    lateinit var SWEET_BERRY_BUSH: Block private set
    lateinit var CAMPFIRE: Block private set
    lateinit var BAMBOO: Block private set
    lateinit var BAMBOO_SAPLING: Block private set
    lateinit var BEE_NEST: Block private set
    lateinit var BEEHIVE: Block private set
    lateinit var STRIPPED_ACACIA_LOG: Block private set
    lateinit var STRIPPED_JUNGLE_LOG: Block private set
    lateinit var STRIPPED_BIRCH_LOG: Block private set
    lateinit var STRIPPED_OAK_LOG: Block private set
    lateinit var STRIPPED_SPRUCE_LOG: Block private set
    lateinit var STRIPPED_DARK_OAK_LOG: Block private set
    lateinit var BRICK_WALL: Block private set
    lateinit var GRANITE_WALL: Block private set
    lateinit var ANDESITE_WALL: Block private set
    lateinit var DIORITE_WALL: Block private set
    lateinit var SANDSTONE_WALL: Block private set
    lateinit var RED_SANDSTONE_WALL: Block private set
    lateinit var STONE_BRICK_WALL: Block private set
    lateinit var MOSSY_STONE_WALL: Block private set
    lateinit var NETHER_BRICK_WALL: Block private set
    lateinit var RED_NETHER_BRICK_WALL: Block private set
    lateinit var END_STONE_WALL: Block private set
    lateinit var PRISMARINE_WALL: Block private set
    lateinit var SMOOTH_STONE: Block private set
    lateinit var SMOOTH_QUARTZ: Block private set
    lateinit var BLUE_ICE: Block private set
    lateinit var STRIPPED_ACACIA_WOOD: Block private set
    lateinit var STRIPPED_JUNGLE_WOOD: Block private set
    lateinit var STRIPPED_BIRCH_WOOD: Block private set
    lateinit var STRIPPED_OAK_WOOD: Block private set
    lateinit var STRIPPED_SPRUCE_WOOD: Block private set
    lateinit var STRIPPED_DARK_OAK_WOOD: Block private set
    lateinit var ACACIA_WOOD: Block private set
    lateinit var JUNGLE_WOOD: Block private set
    lateinit var BIRCH_WOOD: Block private set
    lateinit var OAK_WOOD: Block private set
    lateinit var SPRUCE_WOOD: Block private set
    lateinit var DARK_OAK_WOOD: Block private set
    lateinit var ACACIA_TRAPDOOR: Block private set
    lateinit var JUNGLE_TRAPDOOR: Block private set
    lateinit var BIRCH_TRAPDOOR: Block private set
    lateinit var SPRUCE_TRAPDOOR: Block private set
    lateinit var DARK_OAK_TRAPDOOR: Block private set

    // TODO
    lateinit var SCAFFOLDING: Block private set
    lateinit var BELL: Block private set

    fun init() {
        val register = ForgeRegistries.BLOCKS

        LANTERN = register(BlockLantern(), FConfig.villageAndPillage.lantern)
        STONECUTTER = register(BlockStonecutter(), FConfig.villageAndPillage.stonecutter.enabled)
        BARREL = register(BlockBarrel(), FConfig.villageAndPillage.barrel)
        SMOKER = register(BlockFurnaceAdvanced(BlockFurnaceAdvanced.FurnaceType.SMOKER), FConfig.villageAndPillage.smoker)
        BLAST_FURNACE = register(BlockFurnaceAdvanced(BlockFurnaceAdvanced.FurnaceType.BLAST_FURNACE), FConfig.villageAndPillage.blastFurnace)
        LOOM = register(BlockLoom(), FConfig.villageAndPillage.loom.enabled)
        FLETCHING_TABLE = register(RotatableBlock("fletching_table"), FConfig.villageAndPillage.fletchingTable)
        SMITHING_TABLE = register(RotatableBlock("smithing_table"), FConfig.villageAndPillage.smithingTable)
        CARTOGRAPHY_TABLE = register(BlockCartographyTable(), FConfig.villageAndPillage.cartographyTable)
        GRINDSTONE = register(BlockGrindstone(), FConfig.villageAndPillage.grindstone)
        COMPOSTER = register(BlockComposter(), FConfig.villageAndPillage.composter)
        //BELL = register(BlockBell(), villageAndPillage.bell)
        //SCAFFOLDING = register(BlockScaffolding(), villageAndPillage.scaffolding)
        HONEY_BLOCK = register(BlockHoneyBlock(), FConfig.buzzyBees.honeyBlock)
        HONEYCOMB_BLOCK = register(BlockBase("honeycomb_block", Material.CLAY, MapColor.ADOBE, Sounds.CORAL).setHardness(0.6F).setCreativeTab(if (FConfig.useVanillaCreativeTabs) CreativeTabs.DECORATIONS else FutureMC.TAB), FConfig.buzzyBees.honeycombBlock)
        LILY_OF_THE_VALLEY = register(BlockLilyOfTheValley(), FConfig.villageAndPillage.lilyOfTheValley.enabled)
        CORNFLOWER = register(BlockCornflower(), FConfig.villageAndPillage.cornflower.enabled)
        WITHER_ROSE = register(BlockWitherRose(), FConfig.villageAndPillage.witherRose.enabled)
        SWEET_BERRY_BUSH = register(BlockSweetBerryBush(), FConfig.villageAndPillage.sweetBerryBush.enabled)
        CAMPFIRE = register(BlockCampfire(), FConfig.villageAndPillage.campfire.enabled)
        BAMBOO = register(BlockBamboo(), FConfig.villageAndPillage.bamboo.enabled)
        BAMBOO_SAPLING = register(BlockBambooSapling(), FConfig.villageAndPillage.bamboo.enabled)
        BEE_NEST = register(BlockBeeHive("bee_nest").setHardness(0.3F), FConfig.buzzyBees.bee)
        BEEHIVE = register(BlockBeeHive("beehive").setHardness(0.6F), FConfig.buzzyBees.bee)
        STRIPPED_ACACIA_LOG = register(BlockStrippedLog("acacia"), FConfig.updateAquatic.strippedLogs.acacia)
        STRIPPED_JUNGLE_LOG = register(BlockStrippedLog("jungle"), FConfig.updateAquatic.strippedLogs.jungle)
        STRIPPED_BIRCH_LOG = register(BlockStrippedLog("birch"), FConfig.updateAquatic.strippedLogs.birch)
        STRIPPED_OAK_LOG = register(BlockStrippedLog("oak"), FConfig.updateAquatic.strippedLogs.oak)
        STRIPPED_SPRUCE_LOG = register(BlockStrippedLog("spruce"), FConfig.updateAquatic.strippedLogs.spruce)
        STRIPPED_DARK_OAK_LOG = register(BlockStrippedLog("dark_oak"), FConfig.updateAquatic.strippedLogs.darkOak)
        BRICK_WALL = register(BlockWall("brick"), FConfig.villageAndPillage.newWalls.brick)
        GRANITE_WALL = register(BlockWall("granite"), FConfig.villageAndPillage.newWalls.granite)
        ANDESITE_WALL = register(BlockWall("andesite"), FConfig.villageAndPillage.newWalls.andesite)
        DIORITE_WALL = register(BlockWall("diorite"), FConfig.villageAndPillage.newWalls.diorite)
        SANDSTONE_WALL = register(BlockWall("sandstone"), FConfig.villageAndPillage.newWalls.sandstone)
        RED_SANDSTONE_WALL = register(BlockWall("red_sandstone"), FConfig.villageAndPillage.newWalls.redSandstone)
        STONE_BRICK_WALL = register(BlockWall("stone_brick"), FConfig.villageAndPillage.newWalls.stoneBrick)
        MOSSY_STONE_WALL = register(BlockWall("mossy_stone"), FConfig.villageAndPillage.newWalls.mossyStone)
        NETHER_BRICK_WALL = register(BlockWall("nether_brick"), FConfig.villageAndPillage.newWalls.netherBrick)
        RED_NETHER_BRICK_WALL = register(BlockWall("red_nether_brick"), FConfig.villageAndPillage.newWalls.redNetherBrick)
        END_STONE_WALL = register(BlockWall("end_stone"), FConfig.villageAndPillage.newWalls.endStoneBrick)
        PRISMARINE_WALL = register(BlockWall("prismarine"), FConfig.villageAndPillage.newWalls.prismarine)
        SMOOTH_STONE = register(BlockBase("smooth_stone").setHardness(2.0F).setCreativeTab(if (FConfig.useVanillaCreativeTabs) CreativeTabs.BUILDING_BLOCKS else FutureMC.TAB), FConfig.villageAndPillage.smoothStone)
        SMOOTH_QUARTZ = register(BlockBase("smooth_quartz").setHardness(2.0F).setCreativeTab(if (FConfig.useVanillaCreativeTabs) CreativeTabs.BUILDING_BLOCKS else FutureMC.TAB), FConfig.villageAndPillage.smoothQuartz)
        BLUE_ICE = register(BlockBlueIce(), FConfig.updateAquatic.blueIce)
        STRIPPED_ACACIA_WOOD = register(BlockWood().setRegistryName("stripped_acacia_wood"))
        STRIPPED_JUNGLE_WOOD = register(BlockWood().setRegistryName("stripped_jungle_wood"))
        STRIPPED_BIRCH_WOOD = register(BlockWood().setRegistryName("stripped_birch_wood"))
        STRIPPED_OAK_WOOD = register(BlockWood().setRegistryName("stripped_oak_wood"))
        STRIPPED_SPRUCE_WOOD = register(BlockWood().setRegistryName("stripped_spruce_wood"))
        STRIPPED_DARK_OAK_WOOD = register(BlockWood().setRegistryName("stripped_dark_oak_wood"))
        ACACIA_WOOD = register(BlockWood().setRegistryName("acacia_wood"))
        JUNGLE_WOOD = register(BlockWood().setRegistryName("jungle_wood"))
        BIRCH_WOOD = register(BlockWood().setRegistryName("birch_wood"))
        OAK_WOOD = register(BlockWood().setRegistryName("oak_wood"))
        SPRUCE_WOOD = register(BlockWood().setRegistryName("spruce_wood"))
        DARK_OAK_WOOD = register(BlockWood().setRegistryName("dark_oak_wood"))
        ACACIA_TRAPDOOR = register(BlockTrapdoor("acacia_trapdoor"), FConfig.villageAndPillage.newTrapdoors.acacia)
        JUNGLE_TRAPDOOR = register(BlockTrapdoor("jungle_trapdoor"), FConfig.villageAndPillage.newTrapdoors.jungle)
        BIRCH_TRAPDOOR = register(BlockTrapdoor("birch_trapdoor"), FConfig.villageAndPillage.newTrapdoors.birch)
        SPRUCE_TRAPDOOR = register(BlockTrapdoor("spruce_trapdoor"), FConfig.villageAndPillage.newTrapdoors.spruce)
        DARK_OAK_TRAPDOOR = register(BlockTrapdoor("dark_oak_trapdoor"), FConfig.villageAndPillage.newTrapdoors.darkOak)

        registerTE("futuremc:barrel", TileBarrel::class.java, FConfig.villageAndPillage.barrel)
        registerTE("futuremc:blast_furnace", TileFurnaceAdvanced.TileBlastFurnace::class.java, FConfig.villageAndPillage.blastFurnace)
        registerTE("futuremc:smoker", TileFurnaceAdvanced.TileSmoker::class.java, FConfig.villageAndPillage.smoker)
        registerTE("futuremc:composter", TileComposter::class.java, FConfig.villageAndPillage.composter)
        registerTE("futuremc:beehive", TileBeeHive::class.java, FConfig.buzzyBees.bee)
        registerTE("futuremc:campfire", TileCampfire::class.java, FConfig.villageAndPillage.campfire.enabled)
    }
}