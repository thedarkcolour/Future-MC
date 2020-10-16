@file:Suppress("MemberVisibilityCanBePrivate")

package thedarkcolour.futuremc.registry

import net.minecraft.block.SoundType
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraftforge.registries.IForgeRegistry
import thedarkcolour.futuremc.FutureMC

// todo sort
object FSounds {
    val BLOCK_NETHERITE_BREAK = sound("netherite_block_break")
    val BLOCK_NETHERITE_STEP = sound("netherite_block_step")
    val BLOCK_NETHERITE_PLACE = sound("netherite_block_place")
    val BLOCK_NETHERITE_HIT = sound("netherite_block_hit")
    val BLOCK_NETHERITE_FALL = sound("netherite_block_fall")
    val BLOCK_ANCIENT_DEBRIS_BREAK = sound("ancient_debris_break")
    val BLOCK_ANCIENT_DEBRIS_STEP = sound("ancient_debris_step")
    val BLOCK_ANCIENT_DEBRIS_PLACE = sound("ancient_debris_place")
    val BLOCK_ANCIENT_DEBRIS_HIT = sound("ancient_debris_hit")
    val BLOCK_ANCIENT_DEBRIS_FALL = sound("ancient_debris_fall")
    val BLOCK_SOUL_SOIL_BREAK = sound("soul_soil_break")
    val BLOCK_SOUL_SOIL_STEP = sound("soul_soil_step")
    val BLOCK_SOUL_SOIL_PLACE = sound("soul_soil_place")
    val BLOCK_SOUL_SOIL_HIT = sound("soul_soil_hit")
    val BLOCK_SOUL_SOIL_FALL = sound("soul_soil_fall")
    val BLOCK_NYLIUM_BREAK = sound("nylium_break")
    val BLOCK_NYLIUM_STEP = sound("nylium_step")
    val BLOCK_NYLIUM_PLACE = sound("nylium_place")
    val BLOCK_NYLIUM_HIT = sound("nylium_hit")
    val BLOCK_NYLIUM_FALL = sound("nylium_fall")
    val BLOCK_NETHER_STEM_BREAK = sound("nether_stem_break")
    val BLOCK_NETHER_STEM_STEP = sound("nether_stem_step")
    val BLOCK_NETHER_STEM_PLACE = sound("nether_stem_place")
    val BLOCK_NETHER_STEM_HIT = sound("nether_stem_hit")
    val BLOCK_NETHER_STEM_FALL = sound("nether_stem_fall")
    val BLOCK_NETHER_SPROUTS_BREAK = sound("nether_sprouts_break")
    val BLOCK_NETHER_SPROUTS_STEP = sound("nether_sprouts_step")
    val BLOCK_NETHER_SPROUTS_PLACE = sound("nether_sprouts_place")
    val BLOCK_NETHER_SPROUTS_HIT = sound("nether_sprouts_hit")
    val BLOCK_NETHER_SPROUTS_FALL = sound("nether_sprouts_fall")
    val BLOCK_FUNGUS_BREAK = sound("fungus_break")
    val BLOCK_FUNGUS_STEP = sound("fungus_step")
    val BLOCK_FUNGUS_PLACE = sound("fungus_place")
    val BLOCK_FUNGUS_HIT = sound("fungus_hit")
    val BLOCK_FUNGUS_FALL = sound("fungus_fall")
    val BLOCK_BASALT_BREAK = sound("basalt_break")
    val BLOCK_BASALT_STEP = sound("basalt_step")
    val BLOCK_BASALT_PLACE = sound("basalt_place")
    val BLOCK_BASALT_HIT = sound("basalt_hit")
    val BLOCK_BASALT_FALL = sound("basalt_fall")
    val BLOCK_WEEPING_VINES_BREAK = sound("weeping_vines_break")
    val BLOCK_WEEPING_VINES_STEP = sound("weeping_vines_step")
    val BLOCK_WEEPING_VINES_PLACE = sound("weeping_vines_place")
    val BLOCK_WEEPING_VINES_HIT = sound("weeping_vines_hit")
    val BLOCK_WEEPING_VINES_FALL = sound("weeping_vines_fall")
    val BLOCK_SHROOMLIGHT_BREAK = sound("shroomlight_break")
    val BLOCK_SHROOMLIGHT_STEP = sound("shroomlight_step")
    val BLOCK_SHROOMLIGHT_PLACE = sound("shroomlight_place")
    val BLOCK_SHROOMLIGHT_HIT = sound("shroomlight_hit")
    val BLOCK_SHROOMLIGHT_FALL = sound("shroomlight_fall")
    val BLOCK_ROOTS_BREAK = sound("roots_break")
    val BLOCK_ROOTS_STEP = sound("roots_step")
    val BLOCK_ROOTS_PLACE = sound("roots_place")
    val BLOCK_ROOTS_HIT = sound("roots_hit")
    val BLOCK_ROOTS_FALL = sound("roots_fall")
    val BLOCK_NETHER_ORE_BREAK = sound("minecraft:nether_ore_break")
    val BLOCK_NETHER_ORE_STEP = sound("minecraft:nether_ore_step")
    val BLOCK_NETHER_ORE_PLACE = sound("minecraft:nether_ore_place")
    val BLOCK_NETHER_ORE_HIT = sound("minecraft:nether_ore_hit")
    val BLOCK_NETHER_ORE_FALL = sound("minecraft:nether_ore_fall")
    val BLOCK_LODESTONE_BREAK = sound("lodestone_break")
    val BLOCK_LODESTONE_STEP = sound("lodestone_step")
    val BLOCK_LODESTONE_PLACE = sound("lodestone_place")
    val BLOCK_LODESTONE_HIT = sound("lodestone_hit")
    val BLOCK_LODESTONE_FALL = sound("lodestone_fall")
    val BLOCK_CHAIN_BREAK = sound("chain_break")
    val BLOCK_CHAIN_STEP = sound("chain_step")
    val BLOCK_CHAIN_PLACE = sound("chain_place")
    val BLOCK_CHAIN_HIT = sound("chain_hit")
    val BLOCK_CHAIN_FALL = sound("chain_fall")
    val BLOCK_NETHER_GOLD_ORE_BREAK = sound("nether_gold_ore_break")
    val BLOCK_NETHER_GOLD_ORE_STEP = sound("nether_gold_ore_step")
    val BLOCK_NETHER_GOLD_ORE_PLACE = sound("nether_gold_ore_place")
    val BLOCK_NETHER_GOLD_ORE_HIT = sound("nether_gold_ore_hit")
    val BLOCK_NETHER_GOLD_ORE_FALL = sound("nether_gold_ore_fall")
    val BLOCK_GILDED_BLACKSTONE_BREAK = sound("gilded_blackstone_break")
    val BLOCK_GILDED_BLACKSTONE_STEP = sound("gilded_blackstone_step")
    val BLOCK_GILDED_BLACKSTONE_PLACE = sound("gilded_blackstone_place")
    val BLOCK_GILDED_BLACKSTONE_HIT = sound("gilded_blackstone_hit")
    val BLOCK_GILDED_BLACKSTONE_FALL = sound("gilded_blackstone_fall")
    val BLOCK_NETHER_BRICK_BREAK = sound("nether_brick_break")
    val BLOCK_NETHER_BRICK_STEP = sound("nether_brick_step")
    val BLOCK_NETHER_BRICK_PLACE = sound("nether_brick_place")
    val BLOCK_NETHER_BRICK_HIT = sound("nether_brick_hit")
    val BLOCK_NETHER_BRICK_FALL = sound("nether_brick_fall")
    val BLOCK_RESPAWN_ANCHOR_SET_SPAWN = sound("block_respawn_anchor_set_spawn")
    val BLOCK_RESPAWN_ANCHOR_CHARGE = sound("block_respawn_anchor_charge")

    val BLOCK_SMITHING_TABLE_USE = sound("smithing_table_use")
    val ITEM_ARMOR_EQUIP_NETHERITE = sound("equip_netherite")

    // val  = SoundType(1.0f, 1.0f, BLOCK__BREAK, BLOCK__STEP, BLOCK__PLACE, BLOCK__HIT, BLOCK__FALL)
    val NETHERITE = SoundType(1.0f, 1.0f, BLOCK_NETHERITE_BREAK, BLOCK_NETHERITE_STEP, BLOCK_NETHERITE_PLACE, BLOCK_NETHERITE_HIT, BLOCK_NETHERITE_FALL)
    val ANCIENT_DEBRIS = SoundType(1.0f, 1.0f, BLOCK_ANCIENT_DEBRIS_BREAK, BLOCK_ANCIENT_DEBRIS_STEP, BLOCK_ANCIENT_DEBRIS_PLACE, BLOCK_ANCIENT_DEBRIS_HIT, BLOCK_ANCIENT_DEBRIS_FALL)
    val SOUL_SOIL = SoundType(1.0f, 1.0f, BLOCK_SOUL_SOIL_BREAK, BLOCK_SOUL_SOIL_STEP, BLOCK_SOUL_SOIL_PLACE, BLOCK_SOUL_SOIL_HIT, BLOCK_SOUL_SOIL_FALL)
    val NYLIUM = SoundType(1.0f, 1.0f, BLOCK_NYLIUM_BREAK, BLOCK_NYLIUM_STEP, BLOCK_NYLIUM_PLACE, BLOCK_NYLIUM_HIT, BLOCK_NYLIUM_FALL)
    val NETHER_STEM = SoundType(1.0f, 1.0f, BLOCK_NETHER_STEM_BREAK, BLOCK_NETHER_STEM_STEP, BLOCK_NETHER_STEM_PLACE, BLOCK_NETHER_STEM_HIT, BLOCK_NETHER_STEM_FALL)
    val NETHER_SPROUTS = SoundType(1.0f, 1.0f, BLOCK_NETHER_SPROUTS_BREAK, BLOCK_NETHER_SPROUTS_STEP, BLOCK_NETHER_SPROUTS_PLACE, BLOCK_NETHER_SPROUTS_HIT, BLOCK_NETHER_SPROUTS_FALL)
    val FUNGUS = SoundType(1.0f, 1.0f, BLOCK_FUNGUS_BREAK, BLOCK_FUNGUS_STEP, BLOCK_FUNGUS_PLACE, BLOCK_FUNGUS_HIT, BLOCK_FUNGUS_FALL)
    val BASALT = SoundType(1.0f, 1.0f, BLOCK_BASALT_BREAK, BLOCK_BASALT_STEP, BLOCK_BASALT_PLACE, BLOCK_BASALT_HIT, BLOCK_BASALT_FALL)
    val WEEPING_VINES = SoundType(1.0f, 1.0f, BLOCK_WEEPING_VINES_BREAK, BLOCK_WEEPING_VINES_STEP, BLOCK_WEEPING_VINES_PLACE, BLOCK_WEEPING_VINES_HIT, BLOCK_WEEPING_VINES_FALL)
    val SHROOMLIGHT = SoundType(1.0f, 1.0f, BLOCK_SHROOMLIGHT_BREAK, BLOCK_SHROOMLIGHT_STEP, BLOCK_SHROOMLIGHT_PLACE, BLOCK_SHROOMLIGHT_HIT, BLOCK_SHROOMLIGHT_FALL)
    val ROOTS = SoundType(1.0f, 1.0f, BLOCK_ROOTS_BREAK, BLOCK_ROOTS_STEP, BLOCK_ROOTS_PLACE, BLOCK_ROOTS_HIT, BLOCK_ROOTS_FALL)
    // todo override quartz ore sounds
    val NETHER_ORE = SoundType(1.0f, 1.0f, BLOCK_NETHER_ORE_BREAK, BLOCK_NETHER_ORE_STEP, BLOCK_NETHER_ORE_PLACE, BLOCK_NETHER_ORE_HIT, BLOCK_NETHER_ORE_FALL)
    val LODESTONE = SoundType(1.0f, 1.0f, BLOCK_LODESTONE_BREAK, BLOCK_LODESTONE_STEP, BLOCK_LODESTONE_PLACE, BLOCK_LODESTONE_HIT, BLOCK_LODESTONE_FALL)
    val CHAIN = SoundType(1.0f, 1.0f, BLOCK_CHAIN_BREAK, BLOCK_CHAIN_STEP, BLOCK_CHAIN_PLACE, BLOCK_CHAIN_HIT, BLOCK_CHAIN_FALL)
    val NETHER_BRICK = SoundType(1.0f, 1.0f, BLOCK_NETHER_BRICK_BREAK, BLOCK_NETHER_BRICK_STEP, BLOCK_NETHER_BRICK_PLACE, BLOCK_NETHER_BRICK_HIT, BLOCK_NETHER_BRICK_FALL)
    val NETHER_GOLD_ORE = SoundType(1.0f, 1.0f, BLOCK_NETHER_GOLD_ORE_BREAK, BLOCK_NETHER_GOLD_ORE_STEP, BLOCK_NETHER_GOLD_ORE_PLACE, BLOCK_NETHER_GOLD_ORE_HIT, BLOCK_NETHER_GOLD_ORE_FALL)
    val GILDED_BLACKSTONE = SoundType(1.0f, 1.0f, BLOCK_GILDED_BLACKSTONE_BREAK, BLOCK_GILDED_BLACKSTONE_STEP, BLOCK_GILDED_BLACKSTONE_PLACE, BLOCK_GILDED_BLACKSTONE_HIT, BLOCK_GILDED_BLACKSTONE_FALL)

    private fun sound(name: String): SoundEvent {
        val registryName = ResourceLocation(if (name.contains(':')) name else FutureMC.ID + ":" + name)
        return SoundEvent(registryName).setRegistryName(registryName)
    }

    fun registerSounds(sounds: IForgeRegistry<SoundEvent>) {
        sounds.register(BLOCK_NETHERITE_BREAK)
        sounds.register(BLOCK_NETHERITE_STEP)
        sounds.register(BLOCK_NETHERITE_PLACE)
        sounds.register(BLOCK_NETHERITE_HIT)
        sounds.register(BLOCK_NETHERITE_FALL)
        sounds.register(BLOCK_ANCIENT_DEBRIS_BREAK)
        sounds.register(BLOCK_ANCIENT_DEBRIS_STEP)
        sounds.register(BLOCK_ANCIENT_DEBRIS_PLACE)
        sounds.register(BLOCK_ANCIENT_DEBRIS_HIT)
        sounds.register(BLOCK_ANCIENT_DEBRIS_FALL)
        sounds.register(BLOCK_SOUL_SOIL_BREAK)
        sounds.register(BLOCK_SOUL_SOIL_STEP)
        sounds.register(BLOCK_SOUL_SOIL_PLACE)
        sounds.register(BLOCK_SOUL_SOIL_HIT)
        sounds.register(BLOCK_SOUL_SOIL_FALL)
        sounds.register(BLOCK_NYLIUM_BREAK)
        sounds.register(BLOCK_NYLIUM_STEP)
        sounds.register(BLOCK_NYLIUM_PLACE)
        sounds.register(BLOCK_NYLIUM_HIT)
        sounds.register(BLOCK_NYLIUM_FALL)
        sounds.register(BLOCK_NETHER_STEM_BREAK)
        sounds.register(BLOCK_NETHER_STEM_STEP)
        sounds.register(BLOCK_NETHER_STEM_PLACE)
        sounds.register(BLOCK_NETHER_STEM_HIT)
        sounds.register(BLOCK_NETHER_STEM_FALL)
        sounds.register(BLOCK_NETHER_SPROUTS_BREAK)
        sounds.register(BLOCK_NETHER_SPROUTS_STEP)
        sounds.register(BLOCK_NETHER_SPROUTS_PLACE)
        sounds.register(BLOCK_NETHER_SPROUTS_HIT)
        sounds.register(BLOCK_NETHER_SPROUTS_FALL)
        sounds.register(BLOCK_FUNGUS_BREAK)
        sounds.register(BLOCK_FUNGUS_STEP)
        sounds.register(BLOCK_FUNGUS_PLACE)
        sounds.register(BLOCK_FUNGUS_HIT)
        sounds.register(BLOCK_FUNGUS_FALL)
        sounds.register(BLOCK_BASALT_BREAK)
        sounds.register(BLOCK_BASALT_STEP)
        sounds.register(BLOCK_BASALT_PLACE)
        sounds.register(BLOCK_BASALT_HIT)
        sounds.register(BLOCK_BASALT_FALL)
        sounds.register(BLOCK_WEEPING_VINES_BREAK)
        sounds.register(BLOCK_WEEPING_VINES_STEP)
        sounds.register(BLOCK_WEEPING_VINES_PLACE)
        sounds.register(BLOCK_WEEPING_VINES_HIT)
        sounds.register(BLOCK_WEEPING_VINES_FALL)
        sounds.register(BLOCK_SHROOMLIGHT_BREAK)
        sounds.register(BLOCK_SHROOMLIGHT_STEP)
        sounds.register(BLOCK_SHROOMLIGHT_PLACE)
        sounds.register(BLOCK_SHROOMLIGHT_HIT)
        sounds.register(BLOCK_SHROOMLIGHT_FALL)
        sounds.register(BLOCK_ROOTS_BREAK)
        sounds.register(BLOCK_ROOTS_STEP)
        sounds.register(BLOCK_ROOTS_PLACE)
        sounds.register(BLOCK_ROOTS_HIT)
        sounds.register(BLOCK_ROOTS_FALL)
        sounds.register(BLOCK_NETHER_ORE_BREAK)
        sounds.register(BLOCK_NETHER_ORE_STEP)
        sounds.register(BLOCK_NETHER_ORE_PLACE)
        sounds.register(BLOCK_NETHER_ORE_HIT)
        sounds.register(BLOCK_NETHER_ORE_FALL)
        sounds.register(BLOCK_LODESTONE_BREAK)
        sounds.register(BLOCK_LODESTONE_STEP)
        sounds.register(BLOCK_LODESTONE_PLACE)
        sounds.register(BLOCK_LODESTONE_HIT)
        sounds.register(BLOCK_LODESTONE_FALL)
        sounds.register(BLOCK_CHAIN_BREAK)
        sounds.register(BLOCK_CHAIN_STEP)
        sounds.register(BLOCK_CHAIN_PLACE)
        sounds.register(BLOCK_CHAIN_HIT)
        sounds.register(BLOCK_CHAIN_FALL)
        sounds.register(BLOCK_NETHER_GOLD_ORE_BREAK)
        sounds.register(BLOCK_NETHER_GOLD_ORE_STEP)
        sounds.register(BLOCK_NETHER_GOLD_ORE_PLACE)
        sounds.register(BLOCK_NETHER_GOLD_ORE_HIT)
        sounds.register(BLOCK_NETHER_GOLD_ORE_FALL)
        sounds.register(BLOCK_GILDED_BLACKSTONE_BREAK)
        sounds.register(BLOCK_GILDED_BLACKSTONE_STEP)
        sounds.register(BLOCK_GILDED_BLACKSTONE_PLACE)
        sounds.register(BLOCK_GILDED_BLACKSTONE_HIT)
        sounds.register(BLOCK_GILDED_BLACKSTONE_FALL)
        sounds.register(BLOCK_NETHER_BRICK_BREAK)
        sounds.register(BLOCK_NETHER_BRICK_STEP)
        sounds.register(BLOCK_NETHER_BRICK_PLACE)
        sounds.register(BLOCK_NETHER_BRICK_HIT)
        sounds.register(BLOCK_NETHER_BRICK_FALL)

        sounds.register(BLOCK_SMITHING_TABLE_USE)
        sounds.register(ITEM_ARMOR_EQUIP_NETHERITE)
    }
}