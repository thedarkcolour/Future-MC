package thedarkcolour.futuremc.entity.fish.salmon

import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.util.SoundEvent
import net.minecraft.world.World
import thedarkcolour.core.util.stack
import thedarkcolour.futuremc.entity.fish.EntityGroupFish
import thedarkcolour.futuremc.init.FItems
import thedarkcolour.futuremc.init.Sounds

class EntitySalmon(worldIn: World) : EntityGroupFish(worldIn) {
    init {
        setSize(0.7F, 0.4F)
    }

    override val maxGroupSize: Int
        get() = 5

    override fun getFishBucket(): ItemStack = FItems.SALMON_BUCKET.stack

    override fun getAmbientSound(): SoundEvent = Sounds.SALMON_AMBIENT

    override fun getDeathSound(): SoundEvent = Sounds.SALMON_DEATH

    override fun getHurtSound(damageSourceIn: DamageSource?): SoundEvent = Sounds.SALMON_HURT

    override val flopSound: SoundEvent
        get() = Sounds.SALMON_FLOP
}