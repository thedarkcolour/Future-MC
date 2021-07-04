package thedarkcolour.futuremc.client.particle

import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleCloud
import net.minecraft.world.World

class PandaSneezeParticle(worldIn: World, x: Double, y: Double, z: Double, mX: Double, mY: Double, mZ: Double) : ParticleCloud(worldIn, x, y, z, mX, mY, mZ) {
    init {
        particleRed = 200.0f / 255.0f
        particleGreen = 50.0f / 255.0f
        particleBlue = 120.0f / 255.0f
        particleAlpha = 0.4f
    }

    class Factory : IParticleFactory {
        override fun createParticle(
            particleID: Int, worldIn: World, x: Double, y: Double, z: Double,
            mX: Double, mY: Double, mZ: Double, vararg args: Int
        ): Particle {
            return PandaSneezeParticle(worldIn, x, y, z, mX, mY, mZ)
        }
    }
}