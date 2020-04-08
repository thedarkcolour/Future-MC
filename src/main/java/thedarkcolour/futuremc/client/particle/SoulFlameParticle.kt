package thedarkcolour.futuremc.client.particle

import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFlame
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.world.World

class SoulFlameParticle(
    worldIn: World, x: Double, y: Double, z: Double, mX: Double, mY: Double, mZ: Double
) : ParticleFlame(worldIn, x, y, z, mX, mY, mZ) {
    init {
        setParticleTexture(texture)
    }

    override fun getFXLayer() = 1

    override fun setParticleTextureIndex(particleTextureIndex: Int) {} // NO-OP

    class Factory : IParticleFactory {
        override fun createParticle(
            particleID: Int, worldIn: World, x: Double, y: Double, z: Double,
            mX: Double, mY: Double, mZ: Double, vararg args: Int
        ): Particle {
            return SoulFlameParticle(worldIn, x, y, z, mX, mY, mZ)
        }
    }

    companion object {
        lateinit var texture: TextureAtlasSprite
    }
}