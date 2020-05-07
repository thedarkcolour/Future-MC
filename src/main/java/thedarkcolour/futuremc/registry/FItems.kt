package thedarkcolour.futuremc.registry

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.oredict.OreDictionary
import thedarkcolour.core.item.ItemDebugger
import thedarkcolour.core.item.ItemModeled
import thedarkcolour.core.item.ItemModeledBlock
import thedarkcolour.core.util.addListener
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.entity.fish.cod.EntityCod
import thedarkcolour.futuremc.entity.fish.pufferfish.EntityPufferfish
import thedarkcolour.futuremc.entity.fish.salmon.EntitySalmon
import thedarkcolour.futuremc.entity.fish.tropical.EntityTropicalFish
import thedarkcolour.futuremc.item.*

@Suppress("MemberVisibilityCanBePrivate")
object FItems {
    lateinit var DYES: Item private set
    lateinit var TRIDENT: Item private set
    lateinit var BANNER_PATTERN: Item private set
    lateinit var CROSSBOW: Item private set
    lateinit var HONEYCOMB: Item private set
    lateinit var HONEY_BOTTLE: Item private set
    lateinit var SWEET_BERRIES: Item private set
    lateinit var SUSPICIOUS_STEW: Item private set
    lateinit var BAMBOO: Item private set
    lateinit var PUFFERFISH_BUCKET: Item private set
    lateinit var SALMON_BUCKET: Item private set
    lateinit var COD_BUCKET: Item private set
    lateinit var TROPICAL_FISH_BUCKET: Item private set
    lateinit var DEBUGGER: Item private set
    lateinit var NAUTILUS_SHELL: Item private set

    fun init() {
        val register = ForgeRegistries.ITEMS

        DYES = register(ItemDye(), FConfig.villageAndPillage.dyes)
        TRIDENT = register(ItemTrident(), FConfig.updateAquatic.trident)
        BANNER_PATTERN = register(ItemBannerPattern(), FConfig.villageAndPillage.loom.enabled)
        CROSSBOW = register(ItemCrossbow(), FConfig.villageAndPillage.crossbow) {
            addListener(it::update)
        }
        HONEYCOMB = register(
            ItemModeled("honeycomb").setCreativeTab(if (FConfig.useVanillaCreativeTabs) CreativeTabs.MISC else FutureMC.GROUP),
            FConfig.buzzyBees.bee.enabled
        )
        HONEY_BOTTLE = register(ItemHoneyBottle(), FConfig.buzzyBees.bee.enabled)
        SWEET_BERRIES = register(ItemSweetBerries(), FConfig.villageAndPillage.sweetBerryBush.enabled)
        SUSPICIOUS_STEW = register(ItemSuspiciousStew(), FConfig.villageAndPillage.suspiciousStew)
        BAMBOO = register(ItemBamboo(), FConfig.villageAndPillage.bamboo.enabled)
        PUFFERFISH_BUCKET = register(ItemFishBucket("pufferfish_bucket", ::EntityPufferfish), FConfig.updateAquatic.fish.pufferfish.enabled)
        SALMON_BUCKET =
            register(ItemFishBucket("salmon_bucket", ::EntitySalmon), FConfig.updateAquatic.fish.salmon.enabled)
        COD_BUCKET =
            register(ItemFishBucket("cod_bucket", ::EntityCod), FConfig.updateAquatic.fish.cod.enabled)
        TROPICAL_FISH_BUCKET =
            register(ItemFishBucket("tropical_fish_bucket", ::EntityTropicalFish), FConfig.updateAquatic.fish.tropicalFish.enabled)
        NAUTILUS_SHELL =
            register(ItemModeled("nautilus_shell").setCreativeTab(if (FConfig.useVanillaCreativeTabs) CreativeTabs.MISC else FutureMC.GROUP))

        DEBUGGER = register(ItemDebugger(), FutureMC.DEBUG)

        register(ItemModeledBlock(FBlocks.LANTERN), FConfig.villageAndPillage.lantern)
        register(ItemModeledBlock(FBlocks.STONECUTTER), FConfig.villageAndPillage.stonecutter.enabled)
        register(ItemModeledBlock(FBlocks.BARREL), FConfig.villageAndPillage.barrel)
        register(ItemModeledBlock(FBlocks.SMOKER), FConfig.villageAndPillage.smoker)
        register(ItemModeledBlock(FBlocks.BLAST_FURNACE), FConfig.villageAndPillage.blastFurnace)
        register(ItemModeledBlock(FBlocks.LOOM), FConfig.villageAndPillage.loom.enabled)
        register(ItemModeledBlock(FBlocks.FLETCHING_TABLE), FConfig.villageAndPillage.fletchingTable)
        register(ItemModeledBlock(FBlocks.SMITHING_TABLE), FConfig.villageAndPillage.smithingTable.enabled)
        register(ItemModeledBlock(FBlocks.CARTOGRAPHY_TABLE), FConfig.villageAndPillage.cartographyTable.enabled)
        register(ItemModeledBlock(FBlocks.GRINDSTONE), FConfig.villageAndPillage.grindstone.enabled)
        register(ItemModeledBlock(FBlocks.COMPOSTER), FConfig.villageAndPillage.composter)
        register(ItemModeledBlock(FBlocks.BELL), FConfig.villageAndPillage.bell)
        //register(ItemModeledBlock(FBlocks.SCAFFOLDING), villageAndPillage.scaffolding)
        register(ItemModeledBlock(FBlocks.HONEY_BLOCK), FConfig.buzzyBees.honeyBlock.enabled)
        register(ItemModeledBlock(FBlocks.HONEYCOMB_BLOCK), FConfig.buzzyBees.honeycombBlock)
        register(ItemModeledBlock(FBlocks.LILY_OF_THE_VALLEY), FConfig.villageAndPillage.lilyOfTheValley.enabled)
        register(ItemModeledBlock(FBlocks.CORNFLOWER), FConfig.villageAndPillage.cornflower.enabled)
        register(ItemModeledBlock(FBlocks.WITHER_ROSE), FConfig.villageAndPillage.witherRose.enabled)
        register(ItemModeledBlock(FBlocks.CAMPFIRE), FConfig.villageAndPillage.campfire.enabled)
        register(ItemModeledBlock(FBlocks.BEE_NEST), FConfig.buzzyBees.bee.enabled)
        register(ItemModeledBlock(FBlocks.BEEHIVE), FConfig.buzzyBees.bee.enabled)
        register(ItemModeledBlock(FBlocks.STRIPPED_ACACIA_LOG), FConfig.updateAquatic.strippedLogs.acacia)
        register(ItemModeledBlock(FBlocks.STRIPPED_JUNGLE_LOG), FConfig.updateAquatic.strippedLogs.jungle)
        register(ItemModeledBlock(FBlocks.STRIPPED_BIRCH_LOG), FConfig.updateAquatic.strippedLogs.birch)
        register(ItemModeledBlock(FBlocks.STRIPPED_OAK_LOG), FConfig.updateAquatic.strippedLogs.oak)
        register(ItemModeledBlock(FBlocks.STRIPPED_SPRUCE_LOG), FConfig.updateAquatic.strippedLogs.spruce)
        register(ItemModeledBlock(FBlocks.STRIPPED_DARK_OAK_LOG), FConfig.updateAquatic.strippedLogs.darkOak)
        register(ItemModeledBlock(FBlocks.BRICK_WALL), FConfig.villageAndPillage.newWalls.brick)
        register(ItemModeledBlock(FBlocks.GRANITE_WALL), FConfig.villageAndPillage.newWalls.granite)
        register(ItemModeledBlock(FBlocks.ANDESITE_WALL), FConfig.villageAndPillage.newWalls.andesite)
        register(ItemModeledBlock(FBlocks.DIORITE_WALL), FConfig.villageAndPillage.newWalls.diorite)
        register(ItemModeledBlock(FBlocks.SANDSTONE_WALL), FConfig.villageAndPillage.newWalls.sandstone)
        register(ItemModeledBlock(FBlocks.RED_SANDSTONE_WALL), FConfig.villageAndPillage.newWalls.redSandstone)
        register(ItemModeledBlock(FBlocks.STONE_BRICK_WALL), FConfig.villageAndPillage.newWalls.stoneBrick)
        register(ItemModeledBlock(FBlocks.MOSSY_STONE_BRICK_WALL), FConfig.villageAndPillage.newWalls.mossyStone)
        register(ItemModeledBlock(FBlocks.NETHER_BRICK_WALL), FConfig.villageAndPillage.newWalls.netherBrick)
        register(ItemModeledBlock(FBlocks.RED_NETHER_BRICK_WALL), FConfig.villageAndPillage.newWalls.redNetherBrick)
        register(ItemModeledBlock(FBlocks.END_STONE_BRICK_WALL), FConfig.villageAndPillage.newWalls.endStoneBrick)
        register(ItemModeledBlock(FBlocks.PRISMARINE_WALL), FConfig.villageAndPillage.newWalls.prismarine)
        register(ItemModeledBlock(FBlocks.SMOOTH_STONE), FConfig.villageAndPillage.smoothStone)
        register(ItemModeledBlock(FBlocks.SMOOTH_QUARTZ), FConfig.villageAndPillage.smoothQuartz)
        register(ItemModeledBlock(FBlocks.BLUE_ICE), FConfig.updateAquatic.blueIce)
        register(ItemModeledBlock(FBlocks.STRIPPED_ACACIA_WOOD), FConfig.updateAquatic.wood.strippedAcacia)
        register(ItemModeledBlock(FBlocks.STRIPPED_JUNGLE_WOOD), FConfig.updateAquatic.wood.strippedJungle)
        register(ItemModeledBlock(FBlocks.STRIPPED_BIRCH_WOOD), FConfig.updateAquatic.wood.strippedBirch)
        register(ItemModeledBlock(FBlocks.STRIPPED_OAK_WOOD), FConfig.updateAquatic.wood.strippedOak)
        register(ItemModeledBlock(FBlocks.STRIPPED_SPRUCE_WOOD), FConfig.updateAquatic.wood.strippedSpruce)
        register(ItemModeledBlock(FBlocks.STRIPPED_DARK_OAK_WOOD), FConfig.updateAquatic.wood.strippedDarkOak)
        register(ItemModeledBlock(FBlocks.ACACIA_WOOD), FConfig.updateAquatic.wood.acacia)
        register(ItemModeledBlock(FBlocks.JUNGLE_WOOD), FConfig.updateAquatic.wood.jungle)
        register(ItemModeledBlock(FBlocks.BIRCH_WOOD), FConfig.updateAquatic.wood.birch)
        register(ItemModeledBlock(FBlocks.OAK_WOOD), FConfig.updateAquatic.wood.oak)
        register(ItemModeledBlock(FBlocks.SPRUCE_WOOD), FConfig.updateAquatic.wood.spruce)
        register(ItemModeledBlock(FBlocks.DARK_OAK_WOOD), FConfig.updateAquatic.wood.darkOak)
        register(ItemModeledBlock(FBlocks.ACACIA_TRAPDOOR), FConfig.villageAndPillage.newTrapdoors.acacia)
        register(ItemModeledBlock(FBlocks.JUNGLE_TRAPDOOR), FConfig.villageAndPillage.newTrapdoors.jungle)
        register(ItemModeledBlock(FBlocks.BIRCH_TRAPDOOR), FConfig.villageAndPillage.newTrapdoors.birch)
        register(ItemModeledBlock(FBlocks.SPRUCE_TRAPDOOR), FConfig.villageAndPillage.newTrapdoors.spruce)
        register(ItemModeledBlock(FBlocks.DARK_OAK_TRAPDOOR), FConfig.villageAndPillage.newTrapdoors.darkOak)

        register(ItemModeledBlock(FBlocks.SOUL_FIRE_LANTERN), FConfig.netherUpdate.soulFireLantern)
        register(ItemModeledBlock(FBlocks.SOUL_FIRE_TORCH), FConfig.netherUpdate.soulFireTorch)
        register(ItemModeledBlock(FBlocks.SOUL_SOIL), FConfig.netherUpdate.soulSoil)

        //register(ItemSeagrass(), FConfig.updateAquatic.seagrass)

        if (FConfig.villageAndPillage.dyes) {
            OreDictionary.registerOre("dyeWhite", ItemStack(DYES, 1, 0))
            OreDictionary.registerOre("dyeBlue", ItemStack(DYES, 1, 1))
            OreDictionary.registerOre("dyeBrown", ItemStack(DYES, 1, 2))
            OreDictionary.registerOre("dyeBlack", ItemStack(DYES, 1, 3))
        }

        if (FConfig.villageAndPillage.sweetBerryBush.enabled) {
            OreDictionary.registerOre("seedSweetBerry", SWEET_BERRIES)
            OreDictionary.registerOre("cropSweetBerry", SWEET_BERRIES)
        }

        if (FConfig.villageAndPillage.bamboo.enabled) {
            OreDictionary.registerOre("cropBamboo", BAMBOO)
        }


        if (FConfig.updateAquatic.strippedLogs.acacia)
            OreDictionary.registerOre("logWood", FBlocks.STRIPPED_ACACIA_LOG)
        if (FConfig.updateAquatic.strippedLogs.jungle)
            OreDictionary.registerOre("logWood", FBlocks.STRIPPED_JUNGLE_LOG)
        if (FConfig.updateAquatic.strippedLogs.birch)
            OreDictionary.registerOre("logWood", FBlocks.STRIPPED_BIRCH_LOG)
        if (FConfig.updateAquatic.strippedLogs.oak)
            OreDictionary.registerOre("logWood", FBlocks.STRIPPED_OAK_LOG)
        if (FConfig.updateAquatic.strippedLogs.spruce)
            OreDictionary.registerOre("logWood", FBlocks.STRIPPED_SPRUCE_LOG)
        if (FConfig.updateAquatic.strippedLogs.darkOak)
            OreDictionary.registerOre("logWood", FBlocks.STRIPPED_DARK_OAK_LOG)

        if (FConfig.updateAquatic.wood.strippedAcacia)
            OreDictionary.registerOre("logWood", FBlocks.STRIPPED_ACACIA_WOOD)
        if (FConfig.updateAquatic.wood.strippedJungle)
            OreDictionary.registerOre("logWood", FBlocks.STRIPPED_JUNGLE_WOOD)
        if (FConfig.updateAquatic.wood.strippedBirch)
            OreDictionary.registerOre("logWood", FBlocks.STRIPPED_BIRCH_WOOD)
        if (FConfig.updateAquatic.wood.strippedOak)
            OreDictionary.registerOre("logWood", FBlocks.STRIPPED_OAK_WOOD)
        if (FConfig.updateAquatic.wood.strippedSpruce)
            OreDictionary.registerOre("logWood", FBlocks.STRIPPED_SPRUCE_WOOD)
        if (FConfig.updateAquatic.wood.strippedDarkOak)
            OreDictionary.registerOre("logWood", FBlocks.STRIPPED_DARK_OAK_WOOD)
        if (FConfig.updateAquatic.wood.acacia)
            OreDictionary.registerOre("logWood", FBlocks.ACACIA_WOOD)
        if (FConfig.updateAquatic.wood.jungle)
            OreDictionary.registerOre("logWood", FBlocks.JUNGLE_WOOD)
        if (FConfig.updateAquatic.wood.oak)
            OreDictionary.registerOre("logWood", FBlocks.BIRCH_WOOD)
        if (FConfig.updateAquatic.wood.spruce)
            OreDictionary.registerOre("logWood", FBlocks.OAK_WOOD)
        if (FConfig.updateAquatic.wood.darkOak)
            OreDictionary.registerOre("logWood", FBlocks.SPRUCE_WOOD)
    }
}