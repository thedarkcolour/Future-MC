package thedarkcolour.futuremc.entity.fish.salmon

import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.entity.fish.EntityGroupFish
import thedarkcolour.futuremc.registry.FItems
import thedarkcolour.futuremc.registry.FSounds

class EntitySalmon(worldIn: World) : EntityGroupFish(worldIn) {
    init {
        setSize(0.7F, 0.4F)
    }

    override val maxGroupSize = 5

    override fun getDeathSound() = FSounds.SALMON_DEATH
    override fun getHurtSound(damageSourceIn: DamageSource?) = FSounds.SALMON_HURT
    override val flopSound = FSounds.SALMON_FLOP
    override fun getSwimSound() = FSounds.SALMON_SWIM

    override val fishBucket = ItemStack(FItems.SALMON_BUCKET)

    override fun getLootTable() = LOOT_TABLE

    companion object {
        val LOOT_TABLE = ResourceLocation(FutureMC.ID, "entities/salmon")
    }
}