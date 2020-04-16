package thedarkcolour.futuremc.particle

import net.minecraft.client.particle.*
import net.minecraft.particles.BasicParticleType
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import kotlin.math.max

class AshParticle(
        worldIn: World, x: Double, y: Double, z: Double,
        mX: Double, mY: Double, mZ: Double, private val spriteSet: IAnimatedSprite
) : SpriteTexturedParticle(worldIn, x, y, z, mX, mY, mZ) {
    init {
        motionX *= 1.1f
        motionY *= -1.1f
        motionZ *= 1.1f
        motionX *= 1.1f
        motionY *= -1.1f
        motionZ *= 1.1f
        val f = worldIn.rand.nextFloat() * 0.5f
        particleRed = f
        particleGreen = f
        particleBlue = f
        particleScale *= 0.75f
        maxAge = (20 / worldIn.rand.nextFloat() * 0.8 + 2.0).toInt()
        maxAge = max(maxAge, 1)
        selectSpriteWithAge(spriteSet)
        canCollide = false
    }

    override fun getRenderType() = IParticleRenderType.PARTICLE_SHEET_OPAQUE!!

    override fun getScale(partialTicks: Float): Float {
        return particleScale * MathHelper.clamp(age  * partialTicks / maxAge * 32.0f, 0.0f, 1.0f)
    }

    override fun tick() {
        prevPosX = posX
        prevPosY = posY
        prevPosZ = posZ
        if (age++ >= maxAge) {
            setExpired()
        } else {
            selectSpriteWithAge(spriteSet)
            motionY -= 0.004
            move(motionX, motionY, motionZ)

            if (posY == prevPosY) {
                motionX *= 1.1
                motionZ *= 1.1
            }

            motionX *= 0.9599999785423279
            motionY *= 0.9599999785423279
            motionZ *= 0.9599999785423279

            if (onGround) {
                motionX *= 0.699999988079071
                motionZ *= 0.699999988079071
            }
        }
    }

    class Factory(private val spriteSet: IAnimatedSprite) : IParticleFactory<BasicParticleType> {
        override fun makeParticle(p0: BasicParticleType, p1: World, p2: Double, p3: Double, p4: Double, p5: Double, p6: Double, p7: Double): Particle {
            return AshParticle(p1, p2, p3, p4, p5, p6, p7, spriteSet)
        }
    }
}