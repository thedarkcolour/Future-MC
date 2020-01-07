package thedarkcolour.futuremc.init

import net.minecraft.block.SoundType
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraftforge.fml.common.registry.ForgeRegistries
import thedarkcolour.futuremc.FutureMC

object Sounds {
    var TRIDENT_THROW:                SoundEvent private set
    var TRIDENT_PIERCE:               SoundEvent private set
    var TRIDENT_IMPACT:               SoundEvent private set
    var TRIDENT_CONDUCTIVIDAD:        SoundEvent private set
    var TRIDENT_LOYALTY:              SoundEvent private set
    var TRIDENT_RIPTIDE_I:            SoundEvent private set
    var TRIDENT_RIPTIDE_II:           SoundEvent private set
    var TRIDENT_RIPTIDE_III:          SoundEvent private set
    var CROSSBOW_SHOOT:               SoundEvent private set
    var CROSSBOW_QUICK_CHARGE_I:      SoundEvent private set
    var CROSSBOW_QUICK_CHARGE_II:     SoundEvent private set
    var CROSSBOW_QUICK_CHARGE_III:    SoundEvent private set
    var CROSSBOW_LOADING_START:       SoundEvent private set
    var CROSSBOW_LOADING_MIDDLE:      SoundEvent private set
    var CROSSBOW_LOADING_END:         SoundEvent private set
    var CAMPFIRE_CRACKLE:             SoundEvent private set
    var GRINDSTONE_USE:               SoundEvent private set
    var COMPOSTER_EMPTY:              SoundEvent private set
    var COMPOSTER_FILL:               SoundEvent private set
    var COMPOSTER_FILL_SUCCESS:       SoundEvent private set
    var COMPOSTER_READY:              SoundEvent private set
    var PANDA_PRE_SNEEZE:             SoundEvent private set
    var PANDA_SNEEZE:                 SoundEvent private set
    var PANDA_AMBIENT:                SoundEvent private set
    var PANDA_DEATH:                  SoundEvent private set
    var PANDA_EAT:                    SoundEvent private set
    var PANDA_STEP:                   SoundEvent private set
    var PANDA_CANNOT_BREED:           SoundEvent private set
    var PANDA_AGGRESSIVE_AMBIENT:     SoundEvent private set
    var PANDA_WORRIED_AMBIENT:        SoundEvent private set
    var PANDA_HURT:                   SoundEvent private set
    var PANDA_BITE:                   SoundEvent private set
    var BAMBOO_STEP:                  SoundEvent private set
    var BAMBOO_SAPLING_PLACE:         SoundEvent private set
    var BAMBOO_SAPLING_HIT:           SoundEvent private set
    var BAMBOO_PLACE:                 SoundEvent private set
    var SCAFFOLD_BREAK:               SoundEvent private set
    var SCAFFOLD_STEP:                SoundEvent private set
    var SCAFFOLD_PLACE:               SoundEvent private set
    var SCAFFOLD_HIT:                 SoundEvent private set
    var SCAFFOLD_FALL:                SoundEvent private set
    var HONEY_BLOCK_BREAK:            SoundEvent private set
    var HONEY_BLOCK_STEP:             SoundEvent private set
    var HONEY_BLOCK_SLIDE:            SoundEvent private set
    var STONECUTTER_CARVE:            SoundEvent private set
    var CORAL_DIG:                    SoundEvent private set
    var CORAL_STEP:                   SoundEvent private set
    var BELL_RING:                    SoundEvent private set
    var BEE_ENTER_HIVE:               SoundEvent private set
    var BEE_EXIT_HIVE:                SoundEvent private set
    var BEE_STING:                    SoundEvent private set
    var BEE_DEATH:                    SoundEvent private set
    var BEE_HURT:                     SoundEvent private set
    var BEE_POLLINATE:                SoundEvent private set
    var BEE_WORK:                     SoundEvent private set
    var BEE_AGGRESSIVE:               SoundEvent private set
    var BEE_PASSIVE:                  SoundEvent private set
    var HONEY_BOTTLE_DRINK:           SoundEvent private set
    var BEEHIVE_SHEAR:                SoundEvent private set
    var LANTERN_BREAK:                SoundEvent private set
    var LANTERN_PLACE:                SoundEvent private set
    var BUCKET_FILL_FISH:             SoundEvent private set
    var BUCKET_EMPTY_FISH:            SoundEvent private set
    var FISH_SWIM:                    SoundEvent private set
    var COD_FLOP:                     SoundEvent private set
    var COD_HURT:                     SoundEvent private set
    var COD_DEATH:                    SoundEvent private set
    var COD_AMBIENT:                  SoundEvent private set
    var PUFFERFISH_FLOP:              SoundEvent private set
    var PUFFERFISH_HURT:              SoundEvent private set
    var PUFFERFISH_DEATH:             SoundEvent private set
    var PUFFERFISH_AMBIENT:           SoundEvent private set
    var PUFFERFISH_INFLATE:           SoundEvent private set
    var PUFFERFISH_DEFLATE:           SoundEvent private set
    var PUFFERFISH_STING:             SoundEvent private set
    var SALMON_FLOP:                  SoundEvent private set
    var SALMON_HURT:                  SoundEvent private set
    var SALMON_DEATH:                 SoundEvent private set
    var SALMON_AMBIENT:               SoundEvent private set
    var TROPICAL_FISH_FLOP:           SoundEvent private set
    var TROPICAL_FISH_HURT:           SoundEvent private set
    var TROPICAL_FISH_DEATH:          SoundEvent private set
    var TROPICAL_FISH_AMBIENT:        SoundEvent private set
    var DROWNED_AMBIENT:              SoundEvent private set
    var DROWNED_HURT:                 SoundEvent private set
    var DROWNED_DEATH:                SoundEvent private set
    var DROWNED_STEP:                 SoundEvent private set
    var DROWNED_SWIM:                 SoundEvent private set
    var IRON_GOLEM_REPAIR:            SoundEvent private set

    var BAMBOO:            SoundType private set
    var BAMBOO_SAPLING:    SoundType private set
    var CORAL:             SoundType private set
    var HONEY_BLOCK:       SoundType private set
    var LANTERN:           SoundType private set
    var SCAFFOLDING:       SoundType private set

    private fun sound(name: String): SoundEvent {
        val loc = ResourceLocation(FutureMC.ID, name)
        return SoundEvent(loc).setRegistryName(loc)
    }

    init {
        val register = ForgeRegistries.SOUND_EVENTS

        TRIDENT_THROW = register(sound("trident_throw"))
        TRIDENT_PIERCE = register(sound("trident_pierce"))
        TRIDENT_IMPACT = register(sound("trident_impact"))
        TRIDENT_CONDUCTIVIDAD = register(sound("trident_channeling"))
        TRIDENT_LOYALTY = register(sound("loyalty"))
        TRIDENT_RIPTIDE_I = register(sound("riptide_i"))
        TRIDENT_RIPTIDE_II = register(sound("riptide_ii"))
        TRIDENT_RIPTIDE_III = register(sound("riptide_iii"))
        CROSSBOW_SHOOT = register(sound("crossbow_shoot"))
        CROSSBOW_QUICK_CHARGE_I = register(sound("quick_charge_i"))
        CROSSBOW_QUICK_CHARGE_II = register(sound("quick_charge_ii"))
        CROSSBOW_QUICK_CHARGE_III = register(sound("quick_charge_iii"))
        CROSSBOW_LOADING_START = register(sound("crossbow_loading_start"))
        CROSSBOW_LOADING_MIDDLE = register(sound("crossbow_loading_middle"))
        CROSSBOW_LOADING_END = register(sound("crossbow_loading_end"))
        CAMPFIRE_CRACKLE = register(sound("campfire_crackle"))
        GRINDSTONE_USE = register(sound("grindstone_use"))
        COMPOSTER_EMPTY = register(sound("composter_empty"))
        COMPOSTER_FILL = register(sound("composter_fill"))
        COMPOSTER_FILL_SUCCESS = register(sound("composter_fill_success"))
        COMPOSTER_READY = register(sound("composter_ready"))
        PANDA_PRE_SNEEZE = register(sound("panda_pre_sneeze"))
        PANDA_SNEEZE = register(sound("panda_sneeze"))
        PANDA_AMBIENT = register(sound("panda_ambient"))
        PANDA_DEATH = register(sound("panda_death"))
        PANDA_EAT = register(sound("panda_eat"))
        PANDA_STEP = register(sound("panda_step"))
        PANDA_CANNOT_BREED = register(sound("panda_cannot_breed"))
        PANDA_AGGRESSIVE_AMBIENT = register(sound("panda_aggressive_ambient"))
        PANDA_WORRIED_AMBIENT = register(sound("panda_worried_ambient"))
        PANDA_HURT = register(sound("panda_hurt"))
        PANDA_BITE = register(sound("panda_bite"))
        BAMBOO_STEP = register(sound("bamboo_step"))
        BAMBOO_PLACE = register(sound("bamboo_place"))
        BAMBOO_SAPLING_PLACE = register(sound("bamboo_sapling_place"))
        BAMBOO_SAPLING_HIT = register(sound("bamboo_sapling_hit"))
        SCAFFOLD_BREAK = register(sound("scaffold_break"))
        SCAFFOLD_STEP = register(sound("scaffold_step"))
        SCAFFOLD_PLACE = register(sound("scaffold_place"))
        SCAFFOLD_HIT = register(sound("scaffold_hit"))
        SCAFFOLD_FALL = register(sound("scaffold_fall"))
        HONEY_BLOCK_BREAK = register(sound("honey_block_break"))
        HONEY_BLOCK_STEP = register(sound("honey_block_step"))
        HONEY_BLOCK_SLIDE = register(sound("honey_block_slide"))
        STONECUTTER_CARVE = register(sound("stonecutter_carve"))
        CORAL_DIG = register(sound("coral_dig"))
        CORAL_STEP = register(sound("coral_step"))
        BELL_RING = register(sound("bell_ring"))
        BEE_ENTER_HIVE = register(sound("bee_enter_hive"))
        BEE_EXIT_HIVE = register(sound("bee_exit_hive"))
        BEE_STING = register(sound("bee_sting"))
        BEE_DEATH = register(sound("bee_death"))
        BEE_HURT = register(sound("bee_hurt"))
        BEE_POLLINATE = register(sound("bee_pollinate"))
        BEE_WORK = register(sound("bee_work"))
        BEE_AGGRESSIVE = register(sound("bee_aggressive"))
        BEE_PASSIVE = register(sound("bee_passive"))
        HONEY_BOTTLE_DRINK = register(sound("honey_bottle_drink"))
        BEEHIVE_SHEAR = register(sound("shear_hive"))
        LANTERN_PLACE = register(sound("lantern_place"))
        LANTERN_BREAK = register(sound("lantern_break"))
        // TODO add sounds
        BUCKET_FILL_FISH = register(sound("bucket_fill_fish"))
        BUCKET_EMPTY_FISH = register(sound("bucket_empty_fish"))
        FISH_SWIM = register(sound("fish_swim"))
        COD_FLOP = register(sound("cod_flop"))
        COD_HURT = register(sound("cod_hurt"))
        COD_DEATH = register(sound("cod_death"))
        COD_AMBIENT = register(sound("cod_ambient"))
        PUFFERFISH_FLOP = register(sound("pufferfish_flop"))
        PUFFERFISH_HURT = register(sound("pufferfish_hurt"))
        PUFFERFISH_DEATH = register(sound("pufferfish_death"))
        PUFFERFISH_AMBIENT = register(sound("pufferfish_ambient"))
        PUFFERFISH_INFLATE = register(sound("pufferfish_inflate"))
        PUFFERFISH_DEFLATE = register(sound("pufferfish_deflate"))
        PUFFERFISH_STING = register(sound("pufferfish_sting"))
        SALMON_FLOP = register(sound("salmon_flop"))
        SALMON_HURT = register(sound("salmon_hurt"))
        SALMON_DEATH = register(sound("salmon_death"))
        SALMON_AMBIENT = register(sound("salmon_ambient"))
        TROPICAL_FISH_FLOP = register(sound("tropical_fish_flop"))
        TROPICAL_FISH_HURT = register(sound("tropical_fish_hurt"))
        TROPICAL_FISH_DEATH = register(sound("tropical_fish_death"))
        TROPICAL_FISH_AMBIENT = register(sound("tropical_fish_ambient"))
        DROWNED_AMBIENT = register(sound("drowned_ambient"))
        DROWNED_HURT = register(sound("drowned_hurt"))
        DROWNED_DEATH = register(sound("drowned_death"))
        DROWNED_STEP = register(sound("drowned_step"))
        DROWNED_SWIM = register(sound("drowned_swim"))
        IRON_GOLEM_REPAIR = register(sound("iron_golem_repair"))

        BAMBOO = SoundType(1F, 1F, BAMBOO_PLACE, BAMBOO_STEP, BAMBOO_PLACE, BAMBOO_PLACE, BAMBOO_STEP)
        BAMBOO_SAPLING = SoundType(1F, 1F, BAMBOO_SAPLING_HIT, BAMBOO_SAPLING_PLACE, BAMBOO_SAPLING_PLACE, BAMBOO_SAPLING_HIT, BAMBOO_SAPLING_PLACE)
        CORAL = SoundType(1F, 1F, SCAFFOLD_BREAK, SCAFFOLD_STEP, SCAFFOLD_PLACE, SCAFFOLD_HIT, SCAFFOLD_FALL)
        HONEY_BLOCK = SoundType(1F, 1F, HONEY_BLOCK_BREAK, HONEY_BLOCK_STEP, HONEY_BLOCK_BREAK, HONEY_BLOCK_BREAK, HONEY_BLOCK_STEP)
        LANTERN = SoundType(1F, 1F, LANTERN_BREAK, LANTERN_PLACE, LANTERN_PLACE, LANTERN_BREAK, LANTERN_PLACE)
        SCAFFOLDING = SoundType(1F, 1F, CORAL_DIG, CORAL_STEP, CORAL_DIG, CORAL_DIG, CORAL_STEP)
    }
}