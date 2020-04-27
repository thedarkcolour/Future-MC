package thedarkcolour.futuremc.particle

import net.minecraft.client.particle.*
import net.minecraft.particles.BasicParticleType
import net.minecraft.world.World

class NetherSporeParticle(
        worldIn: World, x: Double, y: Double, z: Double,
        mX: Double, mY: Double, mZ: Double, private val spriteSet: IAnimatedSprite
) : SpriteTexturedParticle(worldIn, x, y, z, mX, mY, mZ) {
    init {
        setSize(0.01f, 0.01f)
        maxAge = (8.0 / (Math.random() * 0.8 + 0.2)).toInt()
        selectSpriteWithAge(spriteSet)
        particleGravity = 0.0f
    }

    override fun getRenderType(): IParticleRenderType {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE
    }

    override fun tick() {
        prevPosX = posX
        prevPosY = posY
        prevPosZ = posZ
        val i = 60 - maxAge

        if (maxAge-- <= 0) {
            setExpired()
        } else {
            motionY -= particleGravity
            move(motionX, motionY, motionZ)
            motionX *= 0.98f
            motionY *= 0.98f
            motionZ *= 0.98f
            val f = i.toFloat() * 0.001f
            setSize(f, f)
            setSprite(spriteSet.get(i % 4, 4))
        }
    }

    class WarpedSporeFactory(private val spriteSet: IAnimatedSprite) : IParticleFactory<BasicParticleType> {
        override fun makeParticle(typeIn: BasicParticleType, worldIn: World, x: Double, y: Double, z: Double, xSpeed: Double, ySpeed: Double, zSpeed: Double): Particle? {
            val particle = NetherSporeParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet)
            particle.setColor(0.1f, 0.1f, 0.3f)
            return particle
        }
    }

    class CrimsonSporeFactory(private val spriteSet: IAnimatedSprite) : IParticleFactory<BasicParticleType> {
        override fun makeParticle(typeIn: BasicParticleType, worldIn: World, x: Double, y: Double, z: Double, xSpeed: Double, ySpeed: Double, zSpeed: Double): Particle? {
            val particle = NetherSporeParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet)
            particle.setColor(0.9f, 0.4f, 0.5f)
            return particle
        }
    }
}