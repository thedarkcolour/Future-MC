package thedarkcolour.futuremc.entity.fish.salmon

import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.util.SoundEvent
import net.minecraft.world.World
import thedarkcolour.core.util.stack
import thedarkcolour.futuremc.entity.fish.EntityGroupFish
import thedarkcolour.futuremc.registry.FItems
import thedarkcolour.futuremc.registry.FSounds

class EntitySalmon(worldIn: World) : EntityGroupFish(worldIn) {
    init {
        setSize(0.7F, 0.4F)
    }

    override val maxGroupSize: Int
        get() = 5

    override fun getFishBucket(): ItemStack = FItems.SALMON_BUCKET.stack

    override fun getAmbientSound(): SoundEvent = FSounds.SALMON_AMBIENT

    override fun getDeathSound(): SoundEvent = FSounds.SALMON_DEATH

    override fun getHurtSound(damageSourceIn: DamageSource?): SoundEvent = FSounds.SALMON_HURT

    override val flopSound: SoundEvent
        get() = FSounds.SALMON_FLOP
}