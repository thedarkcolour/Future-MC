package thedarkcolour.futuremc.entity.fish.cod

import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.entity.fish.EntityGroupFish
import thedarkcolour.futuremc.registry.FItems
import thedarkcolour.futuremc.registry.FSounds

class EntityCod(worldIn: World) : EntityGroupFish(worldIn) {
    init {
        setSize(0.5F, 0.3F)
    }

    override fun getDeathSound() = FSounds.COD_DEATH
    override fun getHurtSound(damageSourceIn: DamageSource?) = FSounds.COD_HURT
    override val flopSound = FSounds.COD_FLOP
    override fun getSwimSound() = FSounds.COD_SWIM

    override val fishBucket = ItemStack(FItems.COD_BUCKET)

    override fun getLootTable() = LOOT_TABLE

    companion object {
        val LOOT_TABLE = ResourceLocation(FutureMC.ID, "entities/cod")
    }
}