@file:Suppress("MemberVisibilityCanBePrivate")

package thedarkcolour.futuremc.config

import thedarkcolour.futuremc.config.option.OptionType

object Config {
    val netherUpdate = OptionType.CATEGORY
        .configure("nether_update")
        .description("Options for the Nether Update")
        .defaultValue(Unit)
        .build()

    val ancientDebris = OptionType.CATEGORY
        .configure("ancient_debris")
        .description("Options for the Nether Update")
        .defaultValue(Unit)
        .build()
    val ancientDebrisEnabled = OptionType.BOOL
        .configure("ancient_debris")
        .description("Whether ancient debris is added")
        .defaultValue(true)
        .build()
    val ancientDebrisGenerates = OptionType.BOOL
        .configure("ancient_debris_generates")
        .description("Whether ancient debris generates in the nether")
        .defaultValue(true)
        .build()
    val netherite = OptionType.BOOL
        .configure("netherite")
        .description("Whether netherite and netherite equipment is added")
        .defaultValue(true)
        .build()
    val netherWorldType = OptionType.BOOL
        .configure("nether_world_type")
        .description("Whether the world type with 1.16 biomes is added")
        .defaultValue(true)
        .build()
    val smithingTable = OptionType.BOOL
        .configure("smithing_table")
        .description("Whether the smithing table has functionality")
        .defaultValue(true)
        .build()
    val locateBiomeCommand = OptionType.BOOL
        .configure("locate_biome_command")
        .description("Whether the Locate biome command is added")
        .defaultValue(true)
        .build()
    val soulSandValley = OptionType.BOOL
        .configure("soul_sand_valley")
        .description("Whether the Soul Sand Valley biome is added")
        .defaultValue(true)
        .build()
    val warpedForest = OptionType.BOOL
        .configure("warped_forest")
        .description("Whether the Warped Forest biome is added")
        .defaultValue(true)
        .build()
    val crimsonForest = OptionType.BOOL
        .configure("crimson_forest")
        .description("Whether the Crimson Forest biome is added")
        .defaultValue(true)
        .build()
    val blackstone = OptionType.BOOL
        .configure("blackstone")
        .description("Whether Blackstone and its variants are added")
        .defaultValue(true)
        .build()

    private var setup = false

    fun setup() {
        if (!setup) {
            netherUpdate.add(ancientDebris)
                ancientDebris.add(ancientDebrisEnabled)
                ancientDebris.add(ancientDebrisGenerates)
            netherUpdate.add(netherite)
            netherUpdate.add(smithingTable)
            netherUpdate.add(locateBiomeCommand)
            netherUpdate.add(soulSandValley)
            netherUpdate.add(warpedForest)
            netherUpdate.add(crimsonForest)
            netherUpdate.add(blackstone)

            setup = true
        }
    }
}