package thedarkcolour.futuremc.entity.fish.cod

import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.util.SoundEvent
import net.minecraft.world.World
import thedarkcolour.core.util.stack
import thedarkcolour.futuremc.entity.fish.EntityGroupFish
import thedarkcolour.futuremc.init.FItems
import thedarkcolour.futuremc.init.Sounds

class EntityCod(worldIn: World) : EntityGroupFish(worldIn) {
    init {
        setSize(0.5F, 0.3F)
    }

    override fun getFishBucket(): ItemStack = FItems.COD_BUCKET.stack

    override fun getAmbientSound(): SoundEvent = Sounds.COD_AMBIENT

    override fun getDeathSound(): SoundEvent= Sounds.COD_DEATH

    override fun getHurtSound(damageSourceIn: DamageSource?): SoundEvent = Sounds.COD_HURT

    override val flopSound: SoundEvent
        get() = Sounds.COD_FLOP
}