package thedarkcolour.futuremc.entity.piglin

import net.minecraft.entity.EntityType
import net.minecraft.entity.MobEntity
import net.minecraft.entity.ai.brain.Brain
import net.minecraft.world.World
import thedarkcolour.futuremc.util.cast

class PiglinEntity(type: EntityType<out MobEntity>, worldIn: World) : MobEntity(type, worldIn) {
    override fun getBrain(): Brain<PiglinEntity> {
        return cast(super.getBrain())
    }
}