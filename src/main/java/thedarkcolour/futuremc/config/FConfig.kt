package thedarkcolour.futuremc.config

import net.minecraftforge.common.config.Config
import net.minecraftforge.common.config.Config.*

object FConfig {
    val updateAquatic: UpdateAquatic
        get() = _Internal.updateAquatic

    val villageAndPillage: VillageAndPillage
        get() = _Internal.villageAndPillage

    val buzzyBees: BuzzyBees
        get() = _Internal.buzzyBees

    val useVanillaCreativeTabs: Boolean
        get() = _Internal.useVanillaCreativeTabs

    class UpdateAquatic {
        @Name("Blue Ice")
        @Comment("Whether Blue Ice is enabled.")
        @RequiresMcRestart
        @JvmField var blueIce = true

        //@Name("Data Command")
        //@Comment("Whether the /data command is enabled.")
        //@RequiresMcRestart
        //@JvmField var dataCommand = false

        @Name("Drowned")
        @Comment("Whether the Drowned is enabled.")
        @RequiresMcRestart
        @JvmField var drowned = false

        @Name("Fish")
        @Comment("Options for fish")
        @JvmField val fish = FishConfig()

        @Name("Stripped Logs")
        @Comment("Options for stripped logs")
        @RequiresMcRestart
        @JvmField val strippedLogs = StrippedLogs()

        @Name("ItemTrident")
        @Comment("Whether the Trident is enabled.")
        @RequiresMcRestart
        @JvmField var trident = true

        @Name("Wood")
        @Comment("Options for wood blocks")
        @RequiresMcRestart
        @JvmField val wood = Wood()

        class FishConfig {
            @Name("Cod")
            @Comment("Options for Cod")
            @RequiresMcRestart
            @JvmField var cod = Fish("minecraft:frozen_ocean:15:3:6", "minecraft:ocean:10:3:6", "minecraft:deep_ocean:10:3:6")

            @Name("Pufferfish")
            @Comment("Options for Pufferfish")
            @RequiresMcRestart
            @JvmField var pufferfish = Fish("minecraft:deep_ocean:5:1:3")

            @Name("Salmon")
            @Comment("Options for Salmon")
            @RequiresMcRestart
            @JvmField var salmon = Fish("minecraft:frozen_ocean:15:1:5", "minecraft:deep_ocean:15:1:5", "minecraft:river:5:1:5", "minecraft:frozen_river:5:1:5")

            @Name("Tropical Fish")
            @Comment("Options for Tropical Fish")
            @RequiresMcRestart
            @JvmField var tropicalFish = Fish("minecraft:deep_ocean:25:8:8")

            @Name("Invalid Biome Messages")
            @Comment("Whether missing biomes in a fish's Valid Biomes are logged to the console. Recommended for testing tweaked config options.")
            @JvmField var logMissingValidBiomes = false

            class Fish(vararg defaultValidBiomes: String) {
                @Name("Enabled")
                @Comment("Whether this feature is enabled.")
                @RequiresMcRestart
                @JvmField var enabled = true

                @Name("Valid Biomes")
                @Comment("Which biomes this fish can spawn in." +
                        "The format goes: <modname:biomeName:weight:groupCountMin:groupCountMax>")
                @RequiresMcRestart
                @JvmField var validBiomes = defaultValidBiomes as Array<String>
            }
        }

        class StrippedLogs {
            @Name("Acacia")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var acacia = true

            @Name("Jungle")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var jungle = true

            @Name("Birch")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var birch = true

            @Name("Oak")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var oak = true

            @Name("Spruce")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var spruce = true

            @Name("Dark Oak")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var darkOak = true

            @Name("Right click to strip")
            @Comment("Whether right clicking with an axe will strip a log.")
            @JvmField var rightClickToStrip = true
        }

        class Wood {
            @Name("Acacia")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var acacia = true

            @Name("Stripped Acacia")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var strippedAcacia = true

            @Name("Jungle")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var jungle = true

            @Name("Stripped Jungle")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var strippedJungle = true

            @Name("Birch")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var birch = true

            @Name("Stripped Birch")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var strippedBirch = true

            @Name("Oak")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var oak = true

            @Name("Stripped Oak")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var strippedOak = true

            @Name("Spruce")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var spruce = true

            @Name("Stripped Spruce")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var strippedSpruce = true

            @Name("Dark Oak")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var darkOak = true

            @Name("Stripped Dark Oak")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var strippedDarkOak = true
        }
    }

    class VillageAndPillage {
        @Name("Bamboo")
        @Comment("Options for Bamboo")
        @JvmField val bamboo = Bamboo()

        @Name("Barrel")
        @Comment("Whether the Barrel is enabled.")
        @RequiresMcRestart
        @JvmField var barrel = true

        @Name("Blast Furnace")
        @Comment("Whether the Blast Furnace is enabled.")
        @RequiresMcRestart
        @JvmField var blastFurnace = true

        @Name("Campfire")
        @Comment("Options for Campfire")
        @JvmField val campfire = Campfire()

        @Name("Cartography Table")
        @Comment("Whether the Cartography Table is enabled.")
        @RequiresMcRestart
        @JvmField val cartographyTable = true

        @Name("Composter")
        @Comment("Whether the Composter is enabled")
        @RequiresMcRestart
        @JvmField var composter = true

        @Name("Cornflower")
        @Comment("Options for Cornflower")
        @JvmField val cornflower = Foliage()

        @Name("Crossbow")
        @Comment("Whether the Crossbow is enabled.")
        @RequiresMcRestart
        @JvmField var crossbow = false

        @Name("Dyes")
        @Comment("Whether the new dyes are enabled.")
        @RequiresMcRestart
        @JvmField var dyes = true

        @Name("Fletching Table")
        @Comment("Whether the Fletching Table is enabled.")
        @RequiresMcRestart
        @JvmField var fletchingTable = true

        @Name("Grindstone")
        @Comment("Whether the Grindstone is enabled.")
        @RequiresMcRestart
        @JvmField var grindstone = true

        @Name("Lantern")
        @Comment("Whether the Lantern is enabled.")
        @RequiresMcRestart
        @JvmField var lantern = true

        @Name("Lily of the Valley")
        @Comment("Options for Lily of the Valley")
        @JvmField val lilyOfTheValley = Foliage()

        @Name("Loom")
        @Comment("Options for the Loom")
        @RequiresMcRestart
        @JvmField val loom = Loom()

        @Name("New Trapdoors")
        @Comment("Enable/disable any of the new trapdoors from 1.14")
        @JvmField val newTrapdoors = NewTrapdoors()

        @Name("New Walls")
        @Comment("Enable/disable any of the new walls from 1.14")
        @JvmField val newWalls = NewWalls()

        @Name("Panda")
        @Comment("Whether the Panda is enabled.")
        @RequiresMcRestart
        @JvmField var panda = true

        @Name("Smithing Table")
        @Comment("Whether the Smithing Table is enabled.")
        @RequiresMcRestart
        @JvmField var smithingTable = true

        @Name("Smoker")
        @Comment("Whether the Smoker is enabled")
        @RequiresMcRestart
        @JvmField var smoker = true

        @Name("Smooth Stone")
        @Comment("Whether Smooth Stone is enabled.")
        @RequiresMcRestart
        @JvmField var smoothStone = true

        @Name("Smooth Quartz")
        @Comment("Whether Smooth Quartz is enabled.")
        @RequiresMcRestart
        @JvmField var smoothQuartz = true

        @Name("Stonecutter")
        @Comment("Options for the Stonecutter")
        @JvmField var stonecutter = Stonecutter()

        @Name("Suspicious Stew")
        @Comment("")
        @RequiresMcRestart
        @JvmField var suspiciousStew = true

        @Name("Berry Bush")
        @Comment("Options for Berry Bush")
        @JvmField val sweetBerryBush = SweetBerryBush()

        @Name("Wither Rose")
        @Comment("Options for the Wither Rose")
        @JvmField val witherRose = WitherRose()

        class Bamboo {
            @Name("Enabled")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var enabled = true

            @Name("Tight Bounding Box")
            @Comment("Whether the selection bounding box is tightened to the collision box.")
            @JvmField var tightBoundingBox = false
        }

        class Campfire {
            @Name("Enabled")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var enabled = true

            @Name("Functionality")
            @Comment("Whether the Campfire can cook food.")
            @RequiresMcRestart
            @JvmField var functionality = true

            @Name("Does Damage")
            @Comment("Whether the Campfire will hurt entities when walked on.")
            @JvmField var damage = true
        }

        class Foliage {
            @Name("Enabled")
            @Comment("Whether this flower is enabled.")
            @RequiresMcRestart
            @JvmField var enabled = true

            @Name("Spawn Rate")
            @Comment("Lower means that less patches spawn. Set to 0 to disable generation.")
            @Config.RangeDouble(min = 0.0, max = 1.0)
            @SlidingOption
            @JvmField var spawnRate = 0.5
        }

        class Loom {
            @Name("Enabled")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var enabled = true

            @Name("Functionality")
            @Comment("Whether the Loom can be used to make banners.")
            @JvmField var functionality = false
        }

        class NewTrapdoors {
            @Name("Acacia")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var acacia = false

            @Name("Jungle")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var jungle = false

            @Name("Birch")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var birch = false

            @Name("Spruce")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var spruce = false

            @Name("Dark Oak")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var darkOak = false
        }

        class NewWalls {
            @Name("Brick")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var brick = true

            @Name("Granite")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var granite = true

            @Name("Andesite")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var andesite = true

            @Name("Diorite")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var diorite = true

            @Name("Sandstone")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var sandstone = true

            @Name("Red Sandstone")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var redSandstone = true

            @Name("Stone Brick")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var stoneBrick = true

            @Name("Mossy Stone")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var mossyStone = true

            @Name("Nether Brick")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var netherBrick = true

            @Name("Red Nether Brick")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var redNetherBrick = true

            @Name("Brick")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var endStoneBrick = true

            @Name("Brick")
            @Comment("Whether this feature is enabled.")
            @RequiresMcRestart
            @JvmField var prismarine = true
        }

        class Stonecutter {
            @Name("Enabled")
            @Comment("Whether the Stonecutter is enabled.")
            @RequiresMcRestart
            @JvmField var enabled = true

            @Name("Functionality")
            @Comment("Whether the Stonecutter can be used to cut blocks.")
            @JvmField var functionality = true

            @Name("Recipe Button")
            @Comment("When JEI is installed and this option is true, a recipes button is added to the Stonecutter gui.")
            @JvmField var recipeButton = true
        }

        class SweetBerryBush {
            @Name("Enabled")
            @Comment("Whether the Sweet Berry Bush is enabled. Disabling this also disables Sweet Berries.")
            @RequiresMcRestart
            @JvmField var enabled = true

            @Name("Spawn Rate")
            @Comment("Lower means that less patches spawn. Set to 0 to disable generation.")
            @Config.RangeDouble(min = 0.0, max = 1.0)
            @SlidingOption
            @JvmField var spawnRate = 0.5
        }

        class WitherRose {
            @Name("Enabled")
            @Comment("Whether this flower is enabled.")
            @RequiresMcRestart
            @JvmField var enabled = true

            @Name("Does Damage")
            @Comment("Whether the Wither Rose will deal damage when walked on.")
            @JvmField var damage = true
        }
    }

    class BuzzyBees {
        @Name("Bee")
        @Comment("Whether the 1.15 creature and its related content should be enabled.")
        @RequiresMcRestart
        @JvmField var bee = true

        @Name("Bee Nest Spawn Rate")
        @Comment("The base chance a bee nest will spawn on a tree. Set to 0 to disable generation.")
        @RangeDouble(min = 0.0, max = 1.0)
        @SlidingOption
        @JvmField var beeNestChance = 0.05

        @Name("Honey Block")
        @Comment("Whether the Honey Block is enabled.")
        @RequiresMcRestart
        @JvmField var honeyBlock = true

        @Name("Honeycomb Block")
        @Comment("Whether the Honeycomb Block is enabled.")
        @RequiresMcRestart
        @JvmField var honeycombBlock = true

        @Name("Iron Golem")
        @Comment("Options for Iron Golem")
        @JvmField val ironGolems = IronGolem()

        @Name("Bee Nest Biome Whitelist")
        @Comment("The list of biomes that the Bee Nests will spawn in." +
                " Example: 'minecraft:taiga:2' allows bee nests to spawn in the Taiga biome and the nests have a 0.10 chance to spawn." +
                " You should lower the chance multiplier in biomes where there are lots of trees.")
        @RequiresMcRestart
        @JvmField var validBiomesForBeeNest = arrayOf("minecraft:plains:1", "minecraft:sunflower_plains:1", "minecraft:mutated_forest:0.2")

        class IronGolem {
            @Name("Crack")
            @Comment("Whether Iron Golems \"crack\" as their health decreases.")
            @RequiresMcRestart
            @JvmField var doCrack = true

            @Name("Healing with Iron Ingots")
            @Comment("Whether Iron Golems can be repaired and healed with Iron Ingots.")
            @JvmField var ironBarHealing = true
        }
    }
}