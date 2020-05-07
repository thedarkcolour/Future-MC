package thedarkcolour.futuremc.block

import net.minecraft.block.BlockTorch
import net.minecraft.block.SoundType
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.registry.FParticles
import java.util.*

class BlockSoulFireTorch : BlockTorch() {
    init {
        needsRandomTick = false
        setHardness(0.0F)
        lightValue = 10
        setRegistryName("soul_fire_torch")
        translationKey = "${FutureMC.ID}.soul_fire_torch"
        soundType = SoundType.WOOD
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.DECORATIONS else FutureMC.GROUP
    }

    override fun randomDisplayTick(stateIn: IBlockState, worldIn: World, pos: BlockPos, rand: Random) {
        val facing = stateIn.getValue(FACING)
        val d0 = pos.x.toDouble() + 0.5
        val d1 = pos.y.toDouble() + 0.7
        val d2 = pos.z.toDouble() + 0.5
        val d3 = 0.22
        val d4 = 0.27

        if (facing.axis.isHorizontal) {
            val opposite = facing.opposite
            worldIn.spawnParticle(
                EnumParticleTypes.SMOKE_NORMAL,
                d0 + d4 * opposite.xOffset,
                d1 + d3,
                d2 + d4 * opposite.zOffset,
                0.0,
                0.0,
                0.0
            )
            worldIn.spawnParticle(
                FParticles.SOUL_FLAME,
                d0 + d4 * opposite.xOffset,
                d1 + d3,
                d2 + d4 * opposite.zOffset,
                0.0,
                0.0,
                0.0
            )
        } else {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0, 0.0, 0.0)
            worldIn.spawnParticle(FParticles.SOUL_FLAME, d0, d1, d2, 0.0, 0.0, 0.0)
        }
    }
}