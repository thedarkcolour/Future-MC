package thedarkcolour.futuremc.entity.fish.salmon

import net.minecraft.util.DamageSource
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

    override fun getFishBucket() = FItems.SALMON_BUCKET.stack

    override fun getAmbientSound() = FSounds.SALMON_AMBIENT

    override fun getDeathSound() = FSounds.SALMON_DEATH

    override fun getHurtSound(damageSourceIn: DamageSource?) = FSounds.SALMON_HURT

    override val flopSound
        get() = FSounds.SALMON_FLOP
}