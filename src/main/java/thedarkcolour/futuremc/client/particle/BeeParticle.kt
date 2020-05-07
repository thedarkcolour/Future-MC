package thedarkcolour.futuremc.client.particle

import net.minecraft.client.particle.Particle
import net.minecraft.world.World

open class BeeParticle(worldIn: World, posX: Double, posY: Double, posZ: Double) :
    Particle(worldIn, posX, posY, posZ, 0.0, 0.0, 0.0) {
    private var bobTimer = 0

    init {

    }

    override fun onUpdate() {
        prevPosX = posX
        prevPosY = posY
        prevPosZ = posZ

        if (particleMaxAge-- <= 0) {
            setExpired()
            return
        } else {
            motionY -= particleGravity
            move(motionX, motionY, motionZ)
            postMovement()
            if (!isExpired) {
                motionX *= 0.98f.toDouble()
                motionY *= 0.98f.toDouble()
                motionZ *= 0.98f.toDouble()
            }
        }
    }

    protected open fun postMovement() = Unit

    class FallingNectar(worldIn: World, posX: Double, posY: Double, posZ: Double) :
        BeeParticle(worldIn, posX, posY, posZ) {

        init {
            particleMaxAge = (64 / Math.random() * 0.8 + 0.2).toInt()
        }
    }

    class Honey
}