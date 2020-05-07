package thedarkcolour.futuremc.block

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.Blocks
import net.minecraft.init.MobEffects
import net.minecraft.potion.PotionEffect
import net.minecraft.util.DamageSource
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import thedarkcolour.futuremc.config.FConfig
import java.util.*

class WitherRoseBlock : BlockFlower("wither_rose") {
    override fun randomDisplayTick(stateIn: IBlockState, worldIn: World, pos: BlockPos, rand: Random) {
        val d0 = pos.x.toDouble() + rand.nextDouble() * 0.5 + 0.2
        val d1 = pos.y.toDouble() + rand.nextDouble() * 0.3 + 0.2
        val d2 = pos.z.toDouble() + rand.nextDouble() * 0.5 + 0.2
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0, 0.0, 0.0)
    }

    override fun onEntityCollision(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity) {
        if (FConfig.villageAndPillage.witherRose.damage) {
            if (entityIn is EntityLivingBase) {
                entityIn.addPotionEffect(PotionEffect(MobEffects.WITHER, 40))
                entityIn.attackEntityFrom(DamageSource.WITHER, 1.0f)
            }
        }
    }

    override fun canSustainBush(state: IBlockState): Boolean {
        return super.canSustainBush(state) || state.block == Blocks.SOUL_SAND || state.block == Blocks.NETHERRACK
    }

    override fun isBiomeValid(biome: Biome) = false
    override val flowerChance = 0.0
}