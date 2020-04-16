package thedarkcolour.futuremc.block

import net.minecraft.block.BlockState
import net.minecraft.block.TorchBlock
import net.minecraft.block.WallTorchBlock
import net.minecraft.particles.ParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.futuremc.registry.FParticles
import java.util.*

class StandingSoulFireTorchBlock(properties: Properties) : TorchBlock(properties) {
    override fun animateTick(state: BlockState?, worldIn: World, pos: BlockPos, rand: Random?) {
        val d0 = pos.x.toDouble() + 0.5
        val d1 = pos.y.toDouble() + 0.7
        val d2 = pos.z.toDouble() + 0.5
        worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0, 0.0, 0.0)
        worldIn.addParticle(FParticles.SOUL_FIRE_FLAME, d0, d1, d2, 0.0, 0.0, 0.0)
    }
}
class WallSoulFireTorchBlock(properties: Properties) : WallTorchBlock(properties) {
    override fun animateTick(state: BlockState, worldIn: World, pos: BlockPos, rand: Random?) {
        val direction1 = state.get(HORIZONTAL_FACING).opposite
        val d0 = pos.x + 0.5 + 0.27 * direction1.xOffset
        val d1 = pos.y + 0.92
        val d2 = pos.z + 0.5 + 0.27 * direction1.zOffset
        worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0, 0.0, 0.0)
        worldIn.addParticle(FParticles.SOUL_FIRE_FLAME, d0, d1, d2, 0.0, 0.0, 0.0)
    }
}