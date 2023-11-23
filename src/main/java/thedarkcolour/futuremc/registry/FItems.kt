package thedarkcolour.futuremc.registry

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.registries.IForgeRegistry
import thedarkcolour.core.item.DebuggerItem
import thedarkcolour.core.item.ModeledItem
import thedarkcolour.core.item.ModeledItemBlock
import thedarkcolour.core.util.setItemGroup
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.entity.fish.cod.EntityCod
import thedarkcolour.futuremc.entity.fish.pufferfish.EntityPufferfish
import thedarkcolour.futuremc.entity.fish.salmon.EntitySalmon
import thedarkcolour.futuremc.entity.fish.tropical.EntityTropicalFish
import thedarkcolour.futuremc.integration.Integration
import thedarkcolour.futuremc.item.*

/**
 * All items and block items in Future MC
 *
 * @author TheDarkColour
 */
@Suppress("MemberVisibilityCanBePrivate", "HasPlatformType", "DuplicatedCode")
object FItems {
    val DEBUGGER = DebuggerItem()

    val NAUTILUS_SHELL = ModeledItem("nautilus_shell").setItemGroup(CreativeTabs.MISC)
    val SCUTE = ModeledItem("scute").setItemGroup(CreativeTabs.MATERIALS)
    val TRIDENT = TridentItem()
    val PUFFERFISH_BUCKET = FishBucketItem("pufferfish_bucket", ::EntityPufferfish)
    val COD_BUCKET = FishBucketItem("cod_bucket", ::EntityCod)
    val TROPICAL_FISH_BUCKET = FishBucketItem("tropical_fish_bucket", ::EntityTropicalFish)
    val SALMON_BUCKET = FishBucketItem("salmon_bucket", ::EntitySalmon)

    val DYES = ItemDye()
    val BANNER_PATTERN = BannerPatternItem()
    val CROSSBOW = CrossbowItem()
    val SWEET_BERRIES = SweetBerriesItem()
    val SUSPICIOUS_STEW = SuspiciousStewItem()
    val BAMBOO = BambooItem()

    val HONEYCOMB = ModeledItem("honeycomb").setItemGroup(CreativeTabs.MISC)
    val HONEY_BOTTLE = HoneyBottleItem()

    // see Util.kt for Item.setItemGroup
    val NETHERITE_INGOT = FireproofItem("netherite_ingot").setItemGroup(CreativeTabs.MATERIALS)
    val NETHERITE_SCRAP = FireproofItem("netherite_scrap").setItemGroup(CreativeTabs.MATERIALS)
    val NETHERITE_AXE = FireproofAxeItem("netherite_axe", FToolMaterial.NETHERITE, 9.0f, -3.0f).setItemGroup(CreativeTabs.TOOLS)
    val NETHERITE_HOE = FireproofHoeItem("netherite_hoe", FToolMaterial.NETHERITE).setItemGroup(CreativeTabs.TOOLS)
    val NETHERITE_PICKAXE = FireproofPickaxeItem("netherite_pickaxe", FToolMaterial.NETHERITE).setItemGroup(CreativeTabs.TOOLS)
    val NETHERITE_SHOVEL = FireproofShovelItem("netherite_shovel", FToolMaterial.NETHERITE).setItemGroup(CreativeTabs.TOOLS)
    val NETHERITE_SWORD = FireproofSwordItem("netherite_sword", FToolMaterial.NETHERITE).setItemGroup(CreativeTabs.COMBAT)
    val NETHERITE_HELMET = NetheriteArmorItem("netherite_helmet", FArmorMaterial.NETHERITE, EntityEquipmentSlot.HEAD).setItemGroup(CreativeTabs.COMBAT)
    val NETHERITE_CHESTPLATE = NetheriteArmorItem("netherite_chestplate", FArmorMaterial.NETHERITE, EntityEquipmentSlot.CHEST).setItemGroup(CreativeTabs.COMBAT)
    val NETHERITE_LEGGINGS = NetheriteArmorItem("netherite_leggings", FArmorMaterial.NETHERITE, EntityEquipmentSlot.LEGS).setItemGroup(CreativeTabs.COMBAT)
    val NETHERITE_BOOTS = NetheriteArmorItem("netherite_boots", FArmorMaterial.NETHERITE, EntityEquipmentSlot.FEET).setItemGroup(CreativeTabs.COMBAT)

    val LANTERN = ModeledItemBlock(FBlocks.LANTERN)
    val STONECUTTER = ModeledItemBlock(FBlocks.STONECUTTER)
    val BARREL = ModeledItemBlock(FBlocks.BARREL)
    val SMOKER = ModeledItemBlock(FBlocks.SMOKER)
    val BLAST_FURNACE = ModeledItemBlock(FBlocks.BLAST_FURNACE)
    val LOOM = ModeledItemBlock(FBlocks.LOOM)
    val FLETCHING_TABLE = ModeledItemBlock(FBlocks.FLETCHING_TABLE)
    val SMITHING_TABLE = ModeledItemBlock(FBlocks.SMITHING_TABLE)
    val CARTOGRAPHY_TABLE = ModeledItemBlock(FBlocks.CARTOGRAPHY_TABLE)
    val GRINDSTONE = ModeledItemBlock(FBlocks.GRINDSTONE)
    val COMPOSTER = ModeledItemBlock(FBlocks.COMPOSTER)
    val BELL = ModeledItemBlock(FBlocks.BELL)
    val HONEY_BLOCK = ModeledItemBlock(FBlocks.HONEY_BLOCK)
    val HONEYCOMB_BLOCK = ModeledItemBlock(FBlocks.HONEYCOMB_BLOCK)
    val LILY_OF_THE_VALLEY = ModeledItemBlock(FBlocks.LILY_OF_THE_VALLEY)
    val CORNFLOWER = ModeledItemBlock(FBlocks.CORNFLOWER)
    val WITHER_ROSE = ModeledItemBlock(FBlocks.WITHER_ROSE)
    val CAMPFIRE = ModeledItemBlock(FBlocks.CAMPFIRE)
    val SCAFFOLDING = ScaffoldingItem()
    val BEE_NEST = ModeledItemBlock(FBlocks.BEE_NEST)
    val BEEHIVE = ModeledItemBlock(FBlocks.BEEHIVE)
    val STRIPPED_ACACIA_LOG = ModeledItemBlock(FBlocks.STRIPPED_ACACIA_LOG)
    val STRIPPED_JUNGLE_LOG = ModeledItemBlock(FBlocks.STRIPPED_JUNGLE_LOG)
    val STRIPPED_BIRCH_LOG = ModeledItemBlock(FBlocks.STRIPPED_BIRCH_LOG)
    val STRIPPED_OAK_LOG = ModeledItemBlock(FBlocks.STRIPPED_OAK_LOG)
    val STRIPPED_SPRUCE_LOG = ModeledItemBlock(FBlocks.STRIPPED_SPRUCE_LOG)
    val STRIPPED_DARK_OAK_LOG = ModeledItemBlock(FBlocks.STRIPPED_DARK_OAK_LOG)
    val BRICK_WALL = ModeledItemBlock(FBlocks.BRICK_WALL)
    val GRANITE_WALL = ModeledItemBlock(FBlocks.GRANITE_WALL)
    val ANDESITE_WALL = ModeledItemBlock(FBlocks.ANDESITE_WALL)
    val DIORITE_WALL = ModeledItemBlock(FBlocks.DIORITE_WALL)
    val SANDSTONE_WALL = ModeledItemBlock(FBlocks.SANDSTONE_WALL)
    val RED_SANDSTONE_WALL = ModeledItemBlock(FBlocks.RED_SANDSTONE_WALL)
    val STONE_BRICK_WALL = ModeledItemBlock(FBlocks.STONE_BRICK_WALL)
    val MOSSY_STONE_BRICK_WALL = ModeledItemBlock(FBlocks.MOSSY_STONE_BRICK_WALL)
    val NETHER_BRICK_WALL = ModeledItemBlock(FBlocks.NETHER_BRICK_WALL)
    val RED_NETHER_BRICK_WALL = ModeledItemBlock(FBlocks.RED_NETHER_BRICK_WALL)
    val END_STONE_BRICK_WALL = ModeledItemBlock(FBlocks.END_STONE_BRICK_WALL)
    val PRISMARINE_WALL = ModeledItemBlock(FBlocks.PRISMARINE_WALL)
    val SMOOTH_STONE = ModeledItemBlock(FBlocks.SMOOTH_STONE)
    val SMOOTH_SANDSTONE = ModeledItemBlock(FBlocks.SMOOTH_SANDSTONE)
    val SMOOTH_QUARTZ = ModeledItemBlock(FBlocks.SMOOTH_QUARTZ)
    val SMOOTH_RED_SANDSTONE = ModeledItemBlock(FBlocks.SMOOTH_RED_SANDSTONE)
    val BLUE_ICE = ModeledItemBlock(FBlocks.BLUE_ICE)
    val STRIPPED_ACACIA_WOOD = ModeledItemBlock(FBlocks.STRIPPED_ACACIA_WOOD)
    val STRIPPED_JUNGLE_WOOD = ModeledItemBlock(FBlocks.STRIPPED_JUNGLE_WOOD)
    val STRIPPED_BIRCH_WOOD = ModeledItemBlock(FBlocks.STRIPPED_BIRCH_WOOD)
    val STRIPPED_OAK_WOOD = ModeledItemBlock(FBlocks.STRIPPED_OAK_WOOD)
    val STRIPPED_SPRUCE_WOOD = ModeledItemBlock(FBlocks.STRIPPED_SPRUCE_WOOD)
    val STRIPPED_DARK_OAK_WOOD = ModeledItemBlock(FBlocks.STRIPPED_DARK_OAK_WOOD)
    val ACACIA_WOOD = ModeledItemBlock(FBlocks.ACACIA_WOOD)
    val JUNGLE_WOOD = ModeledItemBlock(FBlocks.JUNGLE_WOOD)
    val BIRCH_WOOD = ModeledItemBlock(FBlocks.BIRCH_WOOD)
    val OAK_WOOD = ModeledItemBlock(FBlocks.OAK_WOOD)
    val SPRUCE_WOOD = ModeledItemBlock(FBlocks.SPRUCE_WOOD)
    val DARK_OAK_WOOD = ModeledItemBlock(FBlocks.DARK_OAK_WOOD)
    val ACACIA_TRAPDOOR = ModeledItemBlock(FBlocks.ACACIA_TRAPDOOR)
    val JUNGLE_TRAPDOOR = ModeledItemBlock(FBlocks.JUNGLE_TRAPDOOR)
    val BIRCH_TRAPDOOR = ModeledItemBlock(FBlocks.BIRCH_TRAPDOOR)
    val SPRUCE_TRAPDOOR = ModeledItemBlock(FBlocks.SPRUCE_TRAPDOOR)
    val DARK_OAK_TRAPDOOR = ModeledItemBlock(FBlocks.DARK_OAK_TRAPDOOR)

    val NETHER_GOLD_ORE = ModeledItemBlock(FBlocks.NETHER_GOLD_ORE)
    val BLACKSTONE = ModeledItemBlock(FBlocks.BLACKSTONE)
    val BLACKSTONE_STAIRS = ModeledItemBlock(FBlocks.BLACKSTONE_STAIRS)
    val BLACKSTONE_WALL = ModeledItemBlock(FBlocks.BLACKSTONE_WALL)
    val BLACKSTONE_SLAB = ModeledItemBlock(FBlocks.BLACKSTONE_SLAB)
    val POLISHED_BLACKSTONE = ModeledItemBlock(FBlocks.POLISHED_BLACKSTONE)
    val POLISHED_BLACKSTONE_BRICKS = ModeledItemBlock(FBlocks.POLISHED_BLACKSTONE_BRICKS)
    val CRACKED_POLISHED_BLACKSTONE_BRICKS = ModeledItemBlock(FBlocks.CRACKED_POLISHED_BLACKSTONE_BRICKS)
    val CHISELED_POLISHED_BLACKSTONE = ModeledItemBlock(FBlocks.CHISELED_POLISHED_BLACKSTONE)
    val POLISHED_BLACKSTONE_BRICK_SLAB = ModeledItemBlock(FBlocks.POLISHED_BLACKSTONE_BRICK_SLAB)
    val POLISHED_BLACKSTONE_BRICK_STAIRS = ModeledItemBlock(FBlocks.POLISHED_BLACKSTONE_BRICK_STAIRS)
    val POLISHED_BLACKSTONE_BRICK_WALL = ModeledItemBlock(FBlocks.POLISHED_BLACKSTONE_BRICK_WALL)
    val GILDED_BLACKSTONE = ModeledItemBlock(FBlocks.GILDED_BLACKSTONE)
    val POLISHED_BLACKSTONE_STAIRS = ModeledItemBlock(FBlocks.POLISHED_BLACKSTONE_STAIRS)
    val POLISHED_BLACKSTONE_SLAB = ModeledItemBlock(FBlocks.POLISHED_BLACKSTONE_SLAB)
    val POLISHED_BLACKSTONE_BUTTON = ModeledItemBlock(FBlocks.POLISHED_BLACKSTONE_BUTTON)
    val POLISHED_BLACKSTONE_WALL = ModeledItemBlock(FBlocks.POLISHED_BLACKSTONE_WALL)

    val SOUL_FIRE_LANTERN = ModeledItemBlock(FBlocks.SOUL_FIRE_LANTERN)
    val SOUL_FIRE_TORCH = ModeledItemBlock(FBlocks.SOUL_FIRE_TORCH)
    val SOUL_SOIL = ModeledItemBlock(FBlocks.SOUL_SOIL)
    val CHAIN = ModeledItemBlock(FBlocks.CHAIN)
    val NETHERITE_BLOCK = FireproofItemBlock(FBlocks.NETHERITE_BLOCK)
    val ANCIENT_DEBRIS = FireproofItemBlock(FBlocks.ANCIENT_DEBRIS)
    val RECORD_PIGSTEP = RecordItem("futuremc.pigstep", FSounds.RECORD_PIGSTEP).setItemGroup(CreativeTabs.MISC)

    fun registerItems(items: IForgeRegistry<Item>) {
        if (FConfig.villageAndPillage.dyes) items.register(DYES)
        if (!Integration.Mods.OE.isEnabled) {
            if (FConfig.updateAquatic.trident) items.register(TRIDENT)
        }
        if (FConfig.villageAndPillage.loom.enabled) items.register(BANNER_PATTERN)
        if (FConfig.villageAndPillage.crossbow) {
            items.register(CROSSBOW)
        }
        if (FConfig.buzzyBees.bee.enabled) items.register(HONEYCOMB)
        if (FConfig.buzzyBees.bee.enabled) items.register(HONEY_BOTTLE)
        if (FConfig.villageAndPillage.sweetBerryBush.enabled) items.register(SWEET_BERRIES)
        if (FConfig.villageAndPillage.suspiciousStew) items.register(SUSPICIOUS_STEW)
        if (FConfig.villageAndPillage.bamboo.enabled) items.register(BAMBOO)
        if (!Integration.Mods.OE.isEnabled) {
            if (FConfig.updateAquatic.fish.pufferfish.enabled) items.register(PUFFERFISH_BUCKET)
            if (FConfig.updateAquatic.fish.salmon.enabled) items.register(SALMON_BUCKET)
            if (FConfig.updateAquatic.fish.cod.enabled) items.register(COD_BUCKET)
            if (FConfig.updateAquatic.fish.tropicalFish.enabled) items.register(TROPICAL_FISH_BUCKET)
            if (FConfig.updateAquatic.nautilusShell) items.register(NAUTILUS_SHELL)
        }


        if (FutureMC.DEBUG) items.register(DEBUGGER)
        //items.register(AGRO)

        // todo add config
        if (FutureMC.DEBUG) {
            items.register(NETHER_GOLD_ORE)
            items.register(BLACKSTONE)
            items.register(BLACKSTONE_STAIRS)
            items.register(BLACKSTONE_WALL)
            items.register(BLACKSTONE_SLAB)
            items.register(POLISHED_BLACKSTONE)
            items.register(POLISHED_BLACKSTONE_BRICKS)
            items.register(CRACKED_POLISHED_BLACKSTONE_BRICKS)
            items.register(CHISELED_POLISHED_BLACKSTONE)
            items.register(POLISHED_BLACKSTONE_BRICK_SLAB)
            items.register(POLISHED_BLACKSTONE_BRICK_STAIRS)
            items.register(POLISHED_BLACKSTONE_BRICK_WALL)
            items.register(GILDED_BLACKSTONE)
            items.register(POLISHED_BLACKSTONE_STAIRS)
            items.register(POLISHED_BLACKSTONE_SLAB)
            items.register(POLISHED_BLACKSTONE_BUTTON)
            items.register(POLISHED_BLACKSTONE_WALL)
        }

        if (FConfig.villageAndPillage.lantern) items.register(LANTERN)
        if (FConfig.villageAndPillage.stonecutter.enabled) items.register(STONECUTTER)
        if (FConfig.villageAndPillage.barrel) items.register(BARREL)
        if (FConfig.villageAndPillage.smoker) items.register(SMOKER)
        if (FConfig.villageAndPillage.blastFurnace) items.register(BLAST_FURNACE)
        if (FConfig.villageAndPillage.loom.enabled) items.register(LOOM)
        if (FConfig.villageAndPillage.fletchingTable) items.register(FLETCHING_TABLE)
        if (FConfig.villageAndPillage.smithingTable.enabled) items.register(SMITHING_TABLE)
        if (FConfig.villageAndPillage.cartographyTable.enabled) items.register(CARTOGRAPHY_TABLE)
        if (FConfig.villageAndPillage.grindstone.enabled) items.register(GRINDSTONE)
        if (FConfig.villageAndPillage.composter) items.register(COMPOSTER)
        if (FConfig.villageAndPillage.bell) items.register(BELL)
        if (FConfig.buzzyBees.honeyBlock.enabled) items.register(HONEY_BLOCK)
        if (FConfig.buzzyBees.honeycombBlock) items.register(HONEYCOMB_BLOCK)
        if (FConfig.villageAndPillage.lilyOfTheValley.enabled) items.register(LILY_OF_THE_VALLEY)
        if (FConfig.villageAndPillage.cornflower.enabled) items.register(CORNFLOWER)
        if (FConfig.villageAndPillage.witherRose.enabled) items.register(WITHER_ROSE)
        if (FConfig.villageAndPillage.campfire.enabled) items.register(CAMPFIRE)
        if (FConfig.villageAndPillage.scaffolding) items.register(SCAFFOLDING)
        if (FConfig.buzzyBees.bee.enabled) items.registerAll(BEE_NEST, BEEHIVE)
        if (FConfig.updateAquatic.strippedLogs.acacia) items.register(STRIPPED_ACACIA_LOG)
        if (FConfig.updateAquatic.strippedLogs.jungle) items.register(STRIPPED_JUNGLE_LOG)
        if (FConfig.updateAquatic.strippedLogs.birch) items.register(STRIPPED_BIRCH_LOG)
        if (FConfig.updateAquatic.strippedLogs.oak) items.register(STRIPPED_OAK_LOG)
        if (FConfig.updateAquatic.strippedLogs.spruce) items.register(STRIPPED_SPRUCE_LOG)
        if (FConfig.updateAquatic.strippedLogs.darkOak) items.register(STRIPPED_DARK_OAK_LOG)
        if (FConfig.villageAndPillage.newWalls.brick) items.register(BRICK_WALL)
        if (FConfig.villageAndPillage.newWalls.granite) items.register(GRANITE_WALL)
        if (FConfig.villageAndPillage.newWalls.andesite) items.register(ANDESITE_WALL)
        if (FConfig.villageAndPillage.newWalls.diorite) items.register(DIORITE_WALL)
        if (FConfig.villageAndPillage.newWalls.sandstone) items.register(SANDSTONE_WALL)
        if (FConfig.villageAndPillage.newWalls.redSandstone) items.register(RED_SANDSTONE_WALL)
        if (FConfig.villageAndPillage.newWalls.stoneBrick) items.register(STONE_BRICK_WALL)
        if (FConfig.villageAndPillage.newWalls.mossyStone) items.register(MOSSY_STONE_BRICK_WALL)
        if (FConfig.villageAndPillage.newWalls.netherBrick) items.register(NETHER_BRICK_WALL)
        if (FConfig.villageAndPillage.newWalls.redNetherBrick) items.register(RED_NETHER_BRICK_WALL)
        if (FConfig.villageAndPillage.newWalls.endStoneBrick) items.register(END_STONE_BRICK_WALL)
        if (FConfig.villageAndPillage.newWalls.prismarine) items.register(PRISMARINE_WALL)
        if (FConfig.villageAndPillage.smoothStone) items.register(SMOOTH_STONE)
        if (FConfig.villageAndPillage.smoothSandstone) items.register(SMOOTH_SANDSTONE)
        if (FConfig.villageAndPillage.smoothQuartz) items.register(SMOOTH_QUARTZ)
        if (FConfig.villageAndPillage.smoothRedSandstone) items.register(SMOOTH_RED_SANDSTONE)
        if (!Integration.Mods.OE.isEnabled) {
            if (FConfig.updateAquatic.blueIce) items.register(BLUE_ICE)
        }

        if (FConfig.updateAquatic.wood.strippedAcacia) items.register(STRIPPED_ACACIA_WOOD)
        if (FConfig.updateAquatic.wood.strippedJungle) items.register(STRIPPED_JUNGLE_WOOD)
        if (FConfig.updateAquatic.wood.strippedBirch) items.register(STRIPPED_BIRCH_WOOD)
        if (FConfig.updateAquatic.wood.strippedOak) items.register(STRIPPED_OAK_WOOD)
        if (FConfig.updateAquatic.wood.strippedSpruce) items.register(STRIPPED_SPRUCE_WOOD)
        if (FConfig.updateAquatic.wood.strippedDarkOak) items.register(STRIPPED_DARK_OAK_WOOD)
        if (FConfig.updateAquatic.wood.acacia) items.register(ACACIA_WOOD)
        if (FConfig.updateAquatic.wood.jungle) items.register(JUNGLE_WOOD)
        if (FConfig.updateAquatic.wood.birch) items.register(BIRCH_WOOD)
        if (FConfig.updateAquatic.wood.oak) items.register(OAK_WOOD)
        if (FConfig.updateAquatic.wood.spruce) items.register(SPRUCE_WOOD)
        if (FConfig.updateAquatic.wood.darkOak) items.register(DARK_OAK_WOOD)
        if (FConfig.villageAndPillage.newTrapdoors.acacia) items.register(ACACIA_TRAPDOOR)
        if (FConfig.villageAndPillage.newTrapdoors.jungle) items.register(JUNGLE_TRAPDOOR)
        if (FConfig.villageAndPillage.newTrapdoors.birch) items.register(BIRCH_TRAPDOOR)
        if (FConfig.villageAndPillage.newTrapdoors.spruce) items.register(SPRUCE_TRAPDOOR)
        if (FConfig.villageAndPillage.newTrapdoors.darkOak) items.register(DARK_OAK_TRAPDOOR)

        if (FConfig.netherUpdate.soulFireLantern) items.register(SOUL_FIRE_LANTERN)
        if (FConfig.netherUpdate.soulFireTorch) items.register(SOUL_FIRE_TORCH)
        if (FConfig.netherUpdate.soulSoil) items.register(SOUL_SOIL)

        if (FConfig.netherUpdate.netherite) {
            items.registerAll(NETHERITE_INGOT, NETHERITE_SCRAP, NETHERITE_AXE, NETHERITE_HOE, NETHERITE_PICKAXE, NETHERITE_SHOVEL, NETHERITE_SWORD, NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS)
        }

        if (FConfig.netherUpdate.chain)
            items.register(CHAIN)
        if (FConfig.netherUpdate.netherite)
            items.registerAll(NETHERITE_BLOCK, ANCIENT_DEBRIS)
        if (FConfig.netherUpdate.pigstep)
            items.register(RECORD_PIGSTEP)

        //register(ItemSeagrass(), FConfig.updateAquatic.seagrass)

        registerOres()
    }

    private fun registerOres() {
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
            OreDictionary.registerOre("logWood", STRIPPED_ACACIA_LOG)
        if (FConfig.updateAquatic.strippedLogs.jungle)
            OreDictionary.registerOre("logWood", STRIPPED_JUNGLE_LOG)
        if (FConfig.updateAquatic.strippedLogs.birch)
            OreDictionary.registerOre("logWood", STRIPPED_BIRCH_LOG)
        if (FConfig.updateAquatic.strippedLogs.oak)
            OreDictionary.registerOre("logWood", STRIPPED_OAK_LOG)
        if (FConfig.updateAquatic.strippedLogs.spruce)
            OreDictionary.registerOre("logWood", STRIPPED_SPRUCE_LOG)
        if (FConfig.updateAquatic.strippedLogs.darkOak)
            OreDictionary.registerOre("logWood", STRIPPED_DARK_OAK_LOG)

        if (FConfig.updateAquatic.wood.strippedAcacia)
            OreDictionary.registerOre("logWood", STRIPPED_ACACIA_WOOD)
        if (FConfig.updateAquatic.wood.strippedJungle)
            OreDictionary.registerOre("logWood", STRIPPED_JUNGLE_WOOD)
        if (FConfig.updateAquatic.wood.strippedBirch)
            OreDictionary.registerOre("logWood", STRIPPED_BIRCH_WOOD)
        if (FConfig.updateAquatic.wood.strippedOak)
            OreDictionary.registerOre("logWood", STRIPPED_OAK_WOOD)
        if (FConfig.updateAquatic.wood.strippedSpruce)
            OreDictionary.registerOre("logWood", STRIPPED_SPRUCE_WOOD)
        if (FConfig.updateAquatic.wood.strippedDarkOak)
            OreDictionary.registerOre("logWood", STRIPPED_DARK_OAK_WOOD)
        if (FConfig.updateAquatic.wood.acacia)
            OreDictionary.registerOre("logWood", ACACIA_WOOD)
        if (FConfig.updateAquatic.wood.jungle)
            OreDictionary.registerOre("logWood", JUNGLE_WOOD)
        if (FConfig.updateAquatic.wood.birch)
            OreDictionary.registerOre("logWood", BIRCH_WOOD)
        if (FConfig.updateAquatic.wood.oak)
            OreDictionary.registerOre("logWood", OAK_WOOD)
        if (FConfig.updateAquatic.wood.spruce)
            OreDictionary.registerOre("logWood", SPRUCE_WOOD)
        if (FConfig.updateAquatic.wood.darkOak)
            OreDictionary.registerOre("logWood", DARK_OAK_WOOD)
        if (FConfig.villageAndPillage.newTrapdoors.acacia)
            OreDictionary.registerOre("trapdoorWood", ACACIA_TRAPDOOR)
        if (FConfig.villageAndPillage.newTrapdoors.jungle)
            OreDictionary.registerOre("trapdoorWood", JUNGLE_TRAPDOOR)
        if (FConfig.villageAndPillage.newTrapdoors.birch)
            OreDictionary.registerOre("trapdoorWood", BIRCH_TRAPDOOR)
        if (FConfig.villageAndPillage.newTrapdoors.spruce)
            OreDictionary.registerOre("trapdoorWood", SPRUCE_TRAPDOOR)
        if (FConfig.villageAndPillage.newTrapdoors.darkOak)
            OreDictionary.registerOre("trapdoorWood", DARK_OAK_TRAPDOOR)

        if (FConfig.netherUpdate.netherite) {
            OreDictionary.registerOre("blockNetherite", NETHERITE_BLOCK)
            OreDictionary.registerOre("ingotNetherite", NETHERITE_INGOT)

            // needed so that mod compat works properly
            OreDictionary.registerOre("ingotAncientDebris", NETHERITE_SCRAP)
            OreDictionary.registerOre("oreAncientDebris", ANCIENT_DEBRIS)
        }
    }
}