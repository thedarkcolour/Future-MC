package thedarkcolour.futuremc.registry

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.brain.memory.MemoryModuleType
import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.monster.WitherSkeletonEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import thedarkcolour.futuremc.entity.hoglin.HoglinEntity
import thedarkcolour.futuremc.entity.piglin.PiglinEntity
import java.util.*

object FMemoryModules {
    val NEAREST_VISIBLE_WANTED_ITEM = memoryModule<ItemEntity>("nearest_visible_wanted_item")
    val ANGRY_AT = memoryModule<Any>("angry_at")// DynamicSerializableUuid
    val ADMIRING_ITEM = memoryModule<Any>("admiring_item")// DynamicSerializableBoolean
    val ADMIRING_DISABLED = memoryModule<Any>("admiring_disabled")// DynamicSerializableBoolean
    val HUNTED_RECENTLY = memoryModule<Any>("hunted_recently")// DynamicSerializableBoolean
    val CELEBRATE_LOCATION = memoryModule<BlockPos>("celebrate_location")
    val NEAREST_VISIBLE_WITHER_SKELETON = memoryModule<WitherSkeletonEntity>("nearest_visible_wither_skeleton")
    val NEAREST_VISIBLE_HUNTABLE_HOGLIN = memoryModule<HoglinEntity>("nearest_visible_huntable_hoglin")
    val NEAREST_VISIBLE_BABY_HOGLIN = memoryModule<HoglinEntity>("nearest_visible_baby_hoglin")
    val NEAREST_VISIBLE_BABY_PIGLIN = memoryModule<PiglinEntity>("nearest_visible_baby_piglin")
    val NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD = memoryModule<PlayerEntity>("nearest_targetable_player_not_wearing_gold")
    val NEAREST_ADULT_PIGLINS = memoryModule<List<PiglinEntity>>("nearest_adult_piglins")
    val NEAREST_VISIBLE_ADULT_PIGLINS = memoryModule<List<PiglinEntity>>("nearest_visible_adult_piglins")
    val NEAREST_VISIBLE_ADULT_HOGLINS = memoryModule<List<HoglinEntity>>("nearest_visible_adult_hoglins")
    val NEAREST_VISIBLE_ADULT_PIGLIN = memoryModule<PiglinEntity>("nearest_visible_adult_piglin")
    val NEAREST_VISIBLE_ZOMBIFIED = memoryModule<LivingEntity>("nearest_visible_zombified")
    val VISIBLE_ADULT_PIGLIN_COUNT = memoryModule<Int>("visible_adult_piglin_count")
    val VISIBLE_ADULT_HOGLIN_COUNT = memoryModule<Int>("visible_adult_hoglin_count")
    val NEAREST_PLAYER_HOLDING_WANTED_ITEM = memoryModule<PlayerEntity>("nearest_player_holding_wanted_item")
    val ATE_RECENTLY = memoryModule<Boolean>("ate_recently")
    val NEAREST_REPELLENT = memoryModule<BlockPos>("nearest_repellent")
    val PACIFIED = memoryModule<Boolean>("pacified")

    private fun <U> memoryModule(name: String): MemoryModuleType<U> {
        return MemoryModuleType<U>(Optional.empty()).setRegistryKey(name)
    }
}