package thedarkcolour.futuremc.registry

import net.minecraft.block.SoundType
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraftforge.fml.common.registry.ForgeRegistries
import thedarkcolour.futuremc.FutureMC

object FSounds {
    var TRIDENT_THROW = sound("trident_throw")
    var TRIDENT_PIERCE = sound("trident_pierce")
    var TRIDENT_IMPACT = sound("trident_impact")
    var TRIDENT_CONDUCTIVIDAD = sound("trident_channeling")
    var TRIDENT_LOYALTY = sound("loyalty")
    var TRIDENT_RIPTIDE_I = sound("riptide_i")
    var TRIDENT_RIPTIDE_II = sound("riptide_ii")
    var TRIDENT_RIPTIDE_III = sound("riptide_iii")
    var CROSSBOW_SHOOT = sound("crossbow_shoot")
    var CROSSBOW_QUICK_CHARGE_I = sound("quick_charge_i")
    var CROSSBOW_QUICK_CHARGE_II = sound("quick_charge_ii")
    var CROSSBOW_QUICK_CHARGE_III = sound("quick_charge_iii")
    var CROSSBOW_LOADING_START = sound("crossbow_loading_start")
    var CROSSBOW_LOADING_MIDDLE = sound("crossbow_loading_middle")
    var CROSSBOW_LOADING_END = sound("crossbow_loading_end")
    var CAMPFIRE_CRACKLE = sound("campfire_crackle")
    var GRINDSTONE_USE = sound("grindstone_use")
    var COMPOSTER_EMPTY = sound("composter_empty")
    var COMPOSTER_FILL = sound("composter_fill")
    var COMPOSTER_FILL_SUCCESS = sound("composter_fill_success")
    var COMPOSTER_READY = sound("composter_ready")
    var PANDA_PRE_SNEEZE = sound("panda_pre_sneeze")
    var PANDA_SNEEZE = sound("panda_sneeze")
    var PANDA_AMBIENT = sound("panda_ambient")
    var PANDA_DEATH = sound("panda_death")
    var PANDA_EAT = sound("panda_eat")
    var PANDA_STEP = sound("panda_step")
    var PANDA_CANNOT_BREED = sound("panda_cannot_breed")
    var PANDA_AGGRESSIVE_AMBIENT = sound("panda_aggressive_ambient")
    var PANDA_WORRIED_AMBIENT = sound("panda_worried_ambient")
    var PANDA_HURT = sound("panda_hurt")
    var PANDA_BITE = sound("panda_bite")
    var BAMBOO_STEP = sound("bamboo_step")
    var BAMBOO_SAPLING_PLACE = sound("bamboo_sapling_place")
    var BAMBOO_SAPLING_HIT = sound("bamboo_sapling_hit")
    var BAMBOO_PLACE = sound("bamboo_place")
    var SCAFFOLD_BREAK = sound("scaffold_break")
    var SCAFFOLD_STEP = sound("scaffold_step")
    var SCAFFOLD_PLACE = sound("scaffold_place")
    var SCAFFOLD_HIT = sound("scaffold_hit")
    var SCAFFOLD_FALL = sound("scaffold_fall")
    var HONEY_BLOCK_BREAK = sound("honey_block_break")
    var HONEY_BLOCK_STEP = sound("honey_block_step")
    var HONEY_BLOCK_SLIDE = sound("honey_block_slide")
    var STONECUTTER_CARVE = sound("stonecutter_carve")
    var CORAL_DIG = sound("coral_dig")
    var CORAL_STEP = sound("coral_step")
    var BELL_RING = sound("bell_ring")
    var BEE_ENTER_HIVE = sound("bee_enter_hive")
    var ENTITY_BEE_EXIT_HIVE = sound("bee_exit_hive")
    var BEE_STING = sound("bee_sting")
    var BEE_DEATH = sound("bee_death")
    var BEE_HURT = sound("bee_hurt")
    var BEE_POLLINATE = sound("bee_pollinate")
    var BEE_WORK = sound("bee_work")
    var BEE_AGGRESSIVE = sound("bee_aggressive")
    var BEE_PASSIVE = sound("bee_passive")
    var HONEY_BOTTLE_DRINK = sound("honey_bottle_drink")
    var BEEHIVE_SHEAR = sound("shear_hive")
    var LANTERN_BREAK = sound("lantern_place")
    var LANTERN_PLACE = sound("lantern_break")
    var BUCKET_FILL_FISH: SoundEvent private set
    var BUCKET_EMPTY_FISH: SoundEvent private set
    var FISH_SWIM: SoundEvent private set
    var COD_FLOP: SoundEvent private set
    var COD_HURT: SoundEvent private set
    var COD_DEATH: SoundEvent private set
    var COD_AMBIENT: SoundEvent private set
    var PUFFERFISH_FLOP: SoundEvent private set
    var PUFFERFISH_HURT: SoundEvent private set
    var PUFFERFISH_DEATH: SoundEvent private set
    var PUFFERFISH_AMBIENT: SoundEvent private set
    var PUFFERFISH_INFLATE: SoundEvent private set
    var PUFFERFISH_DEFLATE: SoundEvent private set
    var PUFFERFISH_STING: SoundEvent private set
    var SALMON_FLOP: SoundEvent private set
    var SALMON_HURT: SoundEvent private set
    var SALMON_DEATH: SoundEvent private set
    var SALMON_AMBIENT: SoundEvent private set
    var TROPICAL_FISH_FLOP: SoundEvent private set
    var TROPICAL_FISH_HURT: SoundEvent private set
    var TROPICAL_FISH_DEATH: SoundEvent private set
    var TROPICAL_FISH_AMBIENT: SoundEvent private set
    var DROWNED_AMBIENT: SoundEvent private set
    var DROWNED_HURT: SoundEvent private set
    var DROWNED_DEATH: SoundEvent private set
    var DROWNED_STEP: SoundEvent private set
    var DROWNED_SWIM: SoundEvent private set
    var IRON_GOLEM_REPAIR: SoundEvent private set
    var BLOCK_NETHERITE_BREAK: SoundEvent private set
    var BLOCK_NETHERITE_STEP: SoundEvent private set
    var BLOCK_NETHERITE_PLACE: SoundEvent private set
    var BLOCK_NETHERITE_HIT: SoundEvent private set
    var BLOCK_NETHERITE_FALL: SoundEvent private set
    var BLOCK_ANCIENT_DEBRIS_BREAK: SoundEvent private set
    var BLOCK_ANCIENT_DEBRIS_STEP: SoundEvent private set
    var BLOCK_ANCIENT_DEBRIS_PLACE: SoundEvent private set
    var BLOCK_ANCIENT_DEBRIS_HIT: SoundEvent private set
    var BLOCK_ANCIENT_DEBRIS_FALL: SoundEvent private set
    var BLOCK_SOUL_SOIL_BREAK: SoundEvent private set
    var BLOCK_SOUL_SOIL_STEP: SoundEvent private set
    var BLOCK_SOUL_SOIL_PLACE: SoundEvent private set
    var BLOCK_SOUL_SOIL_HIT: SoundEvent private set
    var BLOCK_SOUL_SOIL_FALL: SoundEvent private set
    var BLOCK_NYLIUM_BREAK: SoundEvent private set
    var BLOCK_NYLIUM_STEP: SoundEvent private set
    var BLOCK_NYLIUM_PLACE: SoundEvent private set
    var BLOCK_NYLIUM_HIT: SoundEvent private set
    var BLOCK_NYLIUM_FALL: SoundEvent private set
    var BLOCK_NETHER_STEM_BREAK: SoundEvent private set
    var BLOCK_NETHER_STEM_STEP: SoundEvent private set
    var BLOCK_NETHER_STEM_PLACE: SoundEvent private set
    var BLOCK_NETHER_STEM_HIT: SoundEvent private set
    var BLOCK_NETHER_STEM_FALL: SoundEvent private set
    var BLOCK_NETHER_SPROUTS_BREAK: SoundEvent private set
    var BLOCK_NETHER_SPROUTS_STEP: SoundEvent private set
    var BLOCK_NETHER_SPROUTS_PLACE: SoundEvent private set
    var BLOCK_NETHER_SPROUTS_HIT: SoundEvent private set
    var BLOCK_NETHER_SPROUTS_FALL: SoundEvent private set
    var BLOCK_FUNGUS_BREAK: SoundEvent private set
    var BLOCK_FUNGUS_STEP: SoundEvent private set
    var BLOCK_FUNGUS_PLACE: SoundEvent private set
    var BLOCK_FUNGUS_HIT: SoundEvent private set
    var BLOCK_FUNGUS_FALL: SoundEvent private set
    var BLOCK_BASALT_BREAK: SoundEvent private set
    var BLOCK_BASALT_STEP: SoundEvent private set
    var BLOCK_BASALT_PLACE: SoundEvent private set
    var BLOCK_BASALT_HIT: SoundEvent private set
    var BLOCK_BASALT_FALL: SoundEvent private set
    var BLOCK_WEEPING_VINES_BREAK: SoundEvent private set
    var BLOCK_WEEPING_VINES_STEP: SoundEvent private set
    var BLOCK_WEEPING_VINES_PLACE: SoundEvent private set
    var BLOCK_WEEPING_VINES_HIT: SoundEvent private set
    var BLOCK_WEEPING_VINES_FALL: SoundEvent private set
    var BLOCK_SHROOMLIGHT_BREAK: SoundEvent private set
    var BLOCK_SHROOMLIGHT_STEP: SoundEvent private set
    var BLOCK_SHROOMLIGHT_PLACE: SoundEvent private set
    var BLOCK_SHROOMLIGHT_HIT: SoundEvent private set
    var BLOCK_SHROOMLIGHT_FALL: SoundEvent private set
    var BLOCK_ROOTS_BREAK: SoundEvent private set
    var BLOCK_ROOTS_STEP: SoundEvent private set
    var BLOCK_ROOTS_PLACE: SoundEvent private set
    var BLOCK_ROOTS_HIT: SoundEvent private set
    var BLOCK_ROOTS_FALL: SoundEvent private set
    val BLOCK_SMITHING_TABLE_USE = sound("smithing_table_use")
    val ITEM_ARMOR_EQUIP_NETHERITE = sound("equip_netherite")

    var BAMBOO: SoundType private set
    var BAMBOO_SAPLING: SoundType private set
    var CORAL: SoundType private set
    var HONEY_BLOCK: SoundType private set
    var LANTERN: SoundType private set
    var SCAFFOLDING: SoundType private set
    val NETHERITE: SoundType
    val ANCIENT_DEBRIS: SoundType
    val SOUL_SOIL: SoundType
    val NYLIUM: SoundType
    val NETHER_STEM: SoundType
    val NETHER_SPROUTS: SoundType
    val FUNGUS: SoundType
    val BASALT: SoundType
    val WEEPING_VINES: SoundType
    val SHROOMLIGHT: SoundType
    val ROOTS: SoundType

    private fun sound(name: String): SoundEvent {
        val loc = ResourceLocation(FutureMC.ID, name)
        return SoundEvent(loc).setRegistryName(loc)
    }

    init {
        val sounds = ForgeRegistries.SOUND_EVENTS

        sounds.register(TRIDENT_THROW)
        sounds.register(TRIDENT_PIERCE)
        sounds.register(TRIDENT_IMPACT)
        sounds.register(TRIDENT_CONDUCTIVIDAD)
        sounds.register(TRIDENT_LOYALTY)
        sounds.register(TRIDENT_RIPTIDE_I)
        sounds.register(TRIDENT_RIPTIDE_II)
        sounds.register(TRIDENT_RIPTIDE_III)
        sounds.register(CROSSBOW_SHOOT)
        sounds.register(CROSSBOW_QUICK_CHARGE_I)
        sounds.register(CROSSBOW_QUICK_CHARGE_II)
        sounds.register(CROSSBOW_QUICK_CHARGE_III)
        sounds.register(CROSSBOW_LOADING_START)
        sounds.register(CROSSBOW_LOADING_MIDDLE)
        sounds.register(CROSSBOW_LOADING_END)
        sounds.register(CAMPFIRE_CRACKLE)
        sounds.register(GRINDSTONE_USE)
        sounds.register(COMPOSTER_EMPTY)
        sounds.register(COMPOSTER_FILL)
        sounds.register(COMPOSTER_FILL_SUCCESS)
        sounds.register(COMPOSTER_READY)
        sounds.register(PANDA_PRE_SNEEZE)
        sounds.register(PANDA_SNEEZE)
        sounds.register(PANDA_AMBIENT)
        sounds.register(PANDA_DEATH)
        sounds.register(PANDA_EAT)
        sounds.register(PANDA_STEP)
        sounds.register(PANDA_CANNOT_BREED)
        sounds.register(PANDA_AGGRESSIVE_AMBIENT)
        sounds.register(PANDA_WORRIED_AMBIENT)
        sounds.register(PANDA_HURT)
        sounds.register(PANDA_BITE)
        sounds.register(BAMBOO_STEP)
        sounds.register(BAMBOO_PLACE)
        sounds.register(BAMBOO_SAPLING_PLACE)
        sounds.register(BAMBOO_SAPLING_HIT)
        sounds.register(SCAFFOLD_BREAK)
        sounds.register(SCAFFOLD_STEP)
        sounds.register(SCAFFOLD_PLACE)
        sounds.register(SCAFFOLD_HIT)
        sounds.register(SCAFFOLD_FALL)
        sounds.register(HONEY_BLOCK_BREAK)
        sounds.register(HONEY_BLOCK_STEP)
        sounds.register(HONEY_BLOCK_SLIDE)
        sounds.register(STONECUTTER_CARVE)
        sounds.register(CORAL_DIG)
        sounds.register(CORAL_STEP)
        sounds.register(BELL_RING)
        sounds.register(BEE_ENTER_HIVE)
        sounds.register(ENTITY_BEE_EXIT_HIVE)
        sounds.register(BEE_STING)
        sounds.register(BEE_DEATH)
        sounds.register(BEE_HURT)
        sounds.register(BEE_POLLINATE)
        sounds.register(BEE_WORK)
        sounds.register(BEE_AGGRESSIVE)
        sounds.register(BEE_PASSIVE)
        sounds.register(HONEY_BOTTLE_DRINK)
        sounds.register(BEEHIVE_SHEAR)
        sounds.register(LANTERN_PLACE)
        sounds.register(LANTERN_BREAK)
        // TODO add sounds
        BUCKET_FILL_FISH = sounds(sound("bucket_fill_fish"))
        BUCKET_EMPTY_FISH = sounds(sound("bucket_empty_fish"))
        FISH_SWIM = sounds(sound("fish_swim"))
        COD_FLOP = sounds(sound("cod_flop"))
        COD_HURT = sounds(sound("cod_hurt"))
        COD_DEATH = sounds(sound("cod_death"))
        COD_AMBIENT = sounds(sound("cod_ambient"))
        PUFFERFISH_FLOP = sounds(sound("pufferfish_flop"))
        PUFFERFISH_HURT = sounds(sound("pufferfish_hurt"))
        PUFFERFISH_DEATH = sounds(sound("pufferfish_death"))
        PUFFERFISH_AMBIENT = sounds(sound("pufferfish_ambient"))
        PUFFERFISH_INFLATE = sounds(sound("pufferfish_inflate"))
        PUFFERFISH_DEFLATE = sounds(sound("pufferfish_deflate"))
        PUFFERFISH_STING = sounds(sound("pufferfish_sting"))
        SALMON_FLOP = sounds(sound("salmon_flop"))
        SALMON_HURT = sounds(sound("salmon_hurt"))
        SALMON_DEATH = sounds(sound("salmon_death"))
        SALMON_AMBIENT = sounds(sound("salmon_ambient"))
        TROPICAL_FISH_FLOP = sounds(sound("tropical_fish_flop"))
        TROPICAL_FISH_HURT = sounds(sound("tropical_fish_hurt"))
        TROPICAL_FISH_DEATH = sounds(sound("tropical_fish_death"))
        TROPICAL_FISH_AMBIENT = sounds(sound("tropical_fish_ambient"))
        DROWNED_AMBIENT = sounds(sound("drowned_ambient"))
        DROWNED_HURT = sounds(sound("drowned_hurt"))
        DROWNED_DEATH = sounds(sound("drowned_death"))
        DROWNED_STEP = sounds(sound("drowned_step"))
        DROWNED_SWIM = sounds(sound("drowned_swim"))
        IRON_GOLEM_REPAIR = sounds(sound("iron_golem_repair"))
        BLOCK_NETHERITE_BREAK = sounds(sound("netherite_block_break"))
        BLOCK_NETHERITE_STEP = sounds(sound("netherite_block_step"))
        BLOCK_NETHERITE_PLACE = sounds(sound("netherite_block_place"))
        BLOCK_NETHERITE_HIT = sounds(sound("netherite_block_hit"))
        BLOCK_NETHERITE_FALL = sounds(sound("netherite_block_fall"))
        BLOCK_ANCIENT_DEBRIS_BREAK = sounds(sound("ancient_debris_break"))
        BLOCK_ANCIENT_DEBRIS_STEP = sounds(sound("ancient_debris_step"))
        BLOCK_ANCIENT_DEBRIS_PLACE = sounds(sound("ancient_debris_place"))
        BLOCK_ANCIENT_DEBRIS_HIT = sounds(sound("ancient_debris_hit"))
        BLOCK_ANCIENT_DEBRIS_FALL = sounds(sound("ancient_debris_fall"))
        BLOCK_SOUL_SOIL_BREAK = sounds(sound("soul_soil_break"))
        BLOCK_SOUL_SOIL_STEP = sounds(sound("soul_soil_step"))
        BLOCK_SOUL_SOIL_PLACE = sounds(sound("soul_soil_place"))
        BLOCK_SOUL_SOIL_HIT = sounds(sound("soul_soil_hit"))
        BLOCK_SOUL_SOIL_FALL = sounds(sound("soul_soil_fall"))
        BLOCK_NYLIUM_BREAK = sounds(sound("nylium_break"))
        BLOCK_NYLIUM_STEP = sounds(sound("nylium_step"))
        BLOCK_NYLIUM_PLACE = sounds(sound("nylium_place"))
        BLOCK_NYLIUM_HIT = sounds(sound("nylium_hit"))
        BLOCK_NYLIUM_FALL = sounds(sound("nylium_fall"))
        BLOCK_NETHER_STEM_BREAK = sounds(sound("nether_stem_break"))
        BLOCK_NETHER_STEM_STEP = sounds(sound("nether_stem_step"))
        BLOCK_NETHER_STEM_PLACE = sounds(sound("nether_stem_place"))
        BLOCK_NETHER_STEM_HIT = sounds(sound("nether_stem_hit"))
        BLOCK_NETHER_STEM_FALL = sounds(sound("nether_stem_fall"))
        BLOCK_NETHER_SPROUTS_BREAK = sounds(sound("nether_sprouts_break"))
        BLOCK_NETHER_SPROUTS_STEP = sounds(sound("nether_sprouts_step"))
        BLOCK_NETHER_SPROUTS_PLACE = sounds(sound("nether_sprouts_place"))
        BLOCK_NETHER_SPROUTS_HIT = sounds(sound("nether_sprouts_hit"))
        BLOCK_NETHER_SPROUTS_FALL = sounds(sound("nether_sprouts_fall"))
        BLOCK_FUNGUS_BREAK = sounds(sound("fungus_break"))
        BLOCK_FUNGUS_STEP = sounds(sound("fungus_step"))
        BLOCK_FUNGUS_PLACE = sounds(sound("fungus_place"))
        BLOCK_FUNGUS_HIT = sounds(sound("fungus_hit"))
        BLOCK_FUNGUS_FALL = sounds(sound("fungus_fall"))
        BLOCK_BASALT_BREAK = sounds(sound("basalt_break"))
        BLOCK_BASALT_STEP = sounds(sound("basalt_step"))
        BLOCK_BASALT_PLACE = sounds(sound("basalt_place"))
        BLOCK_BASALT_HIT = sounds(sound("basalt_hit"))
        BLOCK_BASALT_FALL = sounds(sound("basalt_fall"))
        BLOCK_WEEPING_VINES_BREAK = sounds(sound("weeping_vines_break"))
        BLOCK_WEEPING_VINES_STEP = sounds(sound("weeping_vines_step"))
        BLOCK_WEEPING_VINES_PLACE = sounds(sound("weeping_vines_place"))
        BLOCK_WEEPING_VINES_HIT = sounds(sound("weeping_vines_hit"))
        BLOCK_WEEPING_VINES_FALL = sounds(sound("weeping_vines_fall"))
        BLOCK_SHROOMLIGHT_BREAK = sounds(sound("shroomlight_break"))
        BLOCK_SHROOMLIGHT_STEP = sounds(sound("shroomlight_step"))
        BLOCK_SHROOMLIGHT_PLACE = sounds(sound("shroomlight_place"))
        BLOCK_SHROOMLIGHT_HIT = sounds(sound("shroomlight_hit"))
        BLOCK_SHROOMLIGHT_FALL = sounds(sound("shroomlight_fall"))
        BLOCK_ROOTS_BREAK = sounds(sound("roots_break"))
        BLOCK_ROOTS_STEP = sounds(sound("roots_step"))
        BLOCK_ROOTS_PLACE = sounds(sound("roots_place"))
        BLOCK_ROOTS_HIT = sounds(sound("roots_hit"))
        BLOCK_ROOTS_FALL = sounds(sound("roots_fall"))

        BAMBOO = SoundType(1.0f, 1.0f, BAMBOO_PLACE, BAMBOO_STEP, BAMBOO_PLACE, BAMBOO_PLACE, BAMBOO_STEP)
        BAMBOO_SAPLING = SoundType(1.0f, 1.0f, BAMBOO_SAPLING_HIT, BAMBOO_SAPLING_PLACE, BAMBOO_SAPLING_PLACE, BAMBOO_SAPLING_HIT, BAMBOO_SAPLING_PLACE)
        CORAL = SoundType(1.0f, 1.0f, SCAFFOLD_BREAK, SCAFFOLD_STEP, SCAFFOLD_PLACE, SCAFFOLD_HIT, SCAFFOLD_FALL)
        HONEY_BLOCK = SoundType(1.0f, 1.0f, HONEY_BLOCK_BREAK, HONEY_BLOCK_STEP, HONEY_BLOCK_BREAK, HONEY_BLOCK_BREAK, HONEY_BLOCK_STEP)
        LANTERN = SoundType(1.0f, 1.0f, LANTERN_BREAK, LANTERN_PLACE, LANTERN_PLACE, LANTERN_BREAK, LANTERN_PLACE)
        SCAFFOLDING = SoundType(1.0f, 1.0f, CORAL_DIG, CORAL_STEP, CORAL_DIG, CORAL_DIG, CORAL_STEP)
        NETHERITE = SoundType(1.0f, 1.0f, BLOCK_NETHERITE_BREAK, BLOCK_NETHERITE_STEP, BLOCK_NETHERITE_PLACE, BLOCK_NETHERITE_HIT, BLOCK_NETHERITE_FALL)
        ANCIENT_DEBRIS = SoundType(1.0f, 1.0f, BLOCK_ANCIENT_DEBRIS_BREAK, BLOCK_ANCIENT_DEBRIS_STEP, BLOCK_ANCIENT_DEBRIS_PLACE, BLOCK_ANCIENT_DEBRIS_HIT, BLOCK_ANCIENT_DEBRIS_FALL)
        SOUL_SOIL = SoundType(1.0f, 1.0f, BLOCK_SOUL_SOIL_BREAK, BLOCK_SOUL_SOIL_STEP, BLOCK_SOUL_SOIL_PLACE, BLOCK_SOUL_SOIL_HIT, BLOCK_SOUL_SOIL_FALL)
        NYLIUM = SoundType(1.0f, 1.0f, BLOCK_NYLIUM_BREAK, BLOCK_NYLIUM_STEP, BLOCK_NYLIUM_PLACE, BLOCK_NYLIUM_HIT, BLOCK_NYLIUM_FALL)
        NETHER_STEM = SoundType(1.0f, 1.0f, BLOCK_NETHER_STEM_BREAK, BLOCK_NETHER_STEM_STEP, BLOCK_NETHER_STEM_PLACE, BLOCK_NETHER_STEM_HIT, BLOCK_NETHER_STEM_FALL)
        NETHER_SPROUTS = SoundType(1.0f, 1.0f, BLOCK_NETHER_SPROUTS_BREAK, BLOCK_NETHER_SPROUTS_STEP, BLOCK_NETHER_SPROUTS_PLACE, BLOCK_NETHER_SPROUTS_HIT, BLOCK_NETHER_SPROUTS_FALL)
        FUNGUS = SoundType(1.0f, 1.0f, BLOCK_FUNGUS_BREAK, BLOCK_FUNGUS_STEP, BLOCK_FUNGUS_PLACE, BLOCK_FUNGUS_HIT, BLOCK_FUNGUS_FALL)
        BASALT = SoundType(1.0f, 1.0f, BLOCK_BASALT_BREAK, BLOCK_BASALT_STEP, BLOCK_BASALT_PLACE, BLOCK_BASALT_HIT, BLOCK_BASALT_FALL)
        WEEPING_VINES = SoundType(1.0f, 1.0f, BLOCK_WEEPING_VINES_BREAK, BLOCK_WEEPING_VINES_STEP, BLOCK_WEEPING_VINES_PLACE, BLOCK_WEEPING_VINES_HIT, BLOCK_WEEPING_VINES_FALL)
        SHROOMLIGHT = SoundType(1.0f, 1.0f, BLOCK_SHROOMLIGHT_BREAK, BLOCK_SHROOMLIGHT_STEP, BLOCK_SHROOMLIGHT_PLACE, BLOCK_SHROOMLIGHT_HIT, BLOCK_SHROOMLIGHT_FALL)
        ROOTS = SoundType(1.0f, 1.0f, BLOCK_ROOTS_BREAK, BLOCK_ROOTS_STEP, BLOCK_ROOTS_PLACE, BLOCK_ROOTS_HIT, BLOCK_ROOTS_FALL)
    }
}