package thedarkcolour.futuremc.client.particle

import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.world.World

class CampfireParticle(
    worldIn: World, x: Double, y: Double, z: Double,
    mX: Double, mY: Double, mZ: Double, isSignal: Boolean
) : Particle(worldIn, x, y, z) {
    init {
        multipleParticleScaleBy(3.0f)
        setSize(0.25f, 0.25f)
        particleMaxAge = if (isSignal) {
            rand.nextInt(50) + 280
        } else {
            rand.nextInt(50) + 80
        }
        particleGravity = 3.0E-6f
        motionX = mX
        motionY = mY + rand.nextFloat() / 500.0f
        motionZ = mZ
        setParticleTexture(getSprite(rand.nextInt(12))!!)
    }

    override fun getFXLayer() = 1

    private fun getSprite(index: Int): TextureAtlasSprite? {
        return textures[index]
    }

    override fun onUpdate() {
        prevPosX = posX
        prevPosY = posY
        prevPosZ = posZ
        if (particleAge++ < particleMaxAge && particleAlpha > 0.0f) {
            motionX += rand.nextFloat() / 5000.0f * (if (rand.nextBoolean()) 1 else -1)
            motionZ += rand.nextFloat() / 5000.0f * (if (rand.nextBoolean()) 1 else -1)
            motionY -= particleGravity.toDouble()
            move(motionX, motionY, motionZ)
            if (particleAge >= particleMaxAge - 60 && particleAlpha > 0.01f) {
                particleAlpha -= 0.015f
            }
        } else {
            setExpired()
        }
    }

    class CosyFactory : IParticleFactory {
        override fun createParticle(
            particleID: Int, worldIn: World, x: Double, y: Double, z: Double,
            mX: Double, mY: Double, mZ: Double, vararg args: Int
        ): Particle {
            return CampfireParticle(worldIn, x, y, z, mX, mY, mZ, false)
        }
    }

    class SignalFactory : IParticleFactory {
        override fun createParticle(
            particleID: Int, worldIn: World, x: Double, y: Double, z: Double,
            mX: Double, mY: Double, mZ: Double, vararg args: Int
        ): Particle {
            return CampfireParticle(worldIn, x, y, z, mX, mY, mZ, true)
        }
    }

    companion object {
        var textures = arrayOfNulls<TextureAtlasSprite>(12)
    }
}