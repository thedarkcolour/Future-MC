package thedarkcolour.futuremc.block.villagepillage

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.projectile.EntityPotion
import net.minecraft.init.Items
import net.minecraft.init.PotionTypes
import net.minecraft.init.SoundEvents
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.event.entity.ProjectileImpactEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.block.villagepillage.CampfireBlock.Companion.LIT
import net.minecraft.potion.PotionUtils

@Mod.EventBusSubscriber(modid = FutureMC.ID)
object CampfireEventHandler {
    @SubscribeEvent
    fun onProjectileImpact(event: ProjectileImpactEvent.Throwable) {
        val throwable = event.throwable
        if (throwable is EntityPotion) {
            val world = throwable.world
            val stack = throwable.potion

            if (stack.item === Items.SPLASH_POTION || stack.item === Items.LINGERING_POTION) {
                val potion = PotionUtils.getPotionFromItem(stack)
                if (potion === PotionTypes.WATER) {
                    val pos = BlockPos(throwable.posX, throwable.posY, throwable.posZ)
                    extinguishNearbyCampfires(world, pos)
                }
            }
        }
    }

    private fun extinguishNearbyCampfires(world: World, center: BlockPos) {
        val radius = 1 // Now checks a 3x3 horizontal area (1 block radius)
        for (x in -radius..radius) {
            for (z in -radius..radius) {
                val pos = center.add(x, 0, z)
                val state = world.getBlockState(pos)
                if (state.block is CampfireBlock && state.getValue(LIT)) {
                    world.setBlockState(pos, state.withProperty(LIT, false), 3)
                    world.playSound(
                        null,
                        pos,
                        SoundEvents.BLOCK_FIRE_EXTINGUISH,
                        SoundCategory.BLOCKS,
                        1.0f,
                        1.0f
                    )
                }
            }
        }
    }
}