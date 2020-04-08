package thedarkcolour.futuremc.entity.fish.cod

import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.util.SoundEvent
import net.minecraft.world.World
import thedarkcolour.core.util.stack
import thedarkcolour.futuremc.entity.fish.EntityGroupFish
import thedarkcolour.futuremc.registry.FItems
import thedarkcolour.futuremc.registry.FSounds

class EntityCod(worldIn: World) : EntityGroupFish(worldIn) {
    init {
        setSize(0.5F, 0.3F)
    }

    override fun getFishBucket(): ItemStack = FItems.COD_BUCKET.stack

    override fun getAmbientSound(): SoundEvent = FSounds.COD_AMBIENT

    override fun getDeathSound(): SoundEvent = FSounds.COD_DEATH

    override fun getHurtSound(damageSourceIn: DamageSource?): SoundEvent = FSounds.COD_HURT

    override val flopSound: SoundEvent
        get() = FSounds.COD_FLOP
}