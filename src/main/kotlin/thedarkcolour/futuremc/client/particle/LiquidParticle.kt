@file:Suppress("PackageDirectoryMismatch")

package net.minecraft.client.particle

import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.particles.BasicParticleType
import net.minecraft.particles.IParticleData
import net.minecraft.world.World
import thedarkcolour.futuremc.registry.FParticles

class LiquidParticle {
    private class DrippingObsidianTear(
        worldIn: World, posX: Double, posY: Double,
        posZ: Double, fluid: Fluid, data: IParticleData
    ) : DripParticle.Dripping(worldIn, posX, posY, posZ, fluid, data) {
        init {
            particleGravity *= 0.01f
            maxAge = 100
            setColor(0.51171875f, 0.03125f, 0.890625f)
        }

        override fun getBrightnessForRender(p_189214_1_: Float): Int {
            return 240
        }
    }

    class DrippingObsidianTearFactory(private val provider: IAnimatedSprite) : IParticleFactory<BasicParticleType> {
        override fun makeParticle(
            typeIn: BasicParticleType, worldIn: World,
            posX: Double, posY: Double, posZ: Double,
            xSpeed: Double, ySpeed: Double, zSpeed: Double
        ): Particle {
            val dripping = DrippingObsidianTear(worldIn, posX, posY, posZ, Fluids.EMPTY, FParticles.DRIPPING_OBSIDIAN_TEAR)
            dripping.selectSpriteRandomly(provider)
            return dripping
        }
    }

    private class FallingObsidianTear(
        worldIn: World, posX: Double, posY: Double,
        posZ: Double, fluid: Fluid, data: IParticleData
    ) : DripParticle.FallingLiquidParticle(worldIn, posX, posY, posZ, fluid, data) {
        init {
            particleGravity *= 0.01f
            setColor(0.51171875f, 0.03125f, 0.890625f)
        }

        override fun getBrightnessForRender(p_189214_1_: Float): Int {
            return 240
        }
    }

    class FallingObsidianTearFactory(private val provider: IAnimatedSprite) : IParticleFactory<BasicParticleType> {
        override fun makeParticle(
            typeIn: BasicParticleType, worldIn: World,
            posX: Double, posY: Double, posZ: Double,
            xSpeed: Double, ySpeed: Double, zSpeed: Double
        ): Particle {
            val falling = FallingObsidianTear(worldIn, posX, posY, posZ, Fluids.EMPTY, FParticles.FALLING_OBSIDIAN_TEAR)
            falling.selectSpriteRandomly(provider)
            return falling
        }
    }

    private class LandingObsidianTear(
        worldIn: World, posX: Double, posY: Double,
        posZ: Double, fluid: Fluid
    ) : DripParticle.Landing(worldIn, posX, posY, posZ, fluid) {
        init {

        }

        override fun getBrightnessForRender(tint: Float): Int {
            return 240
        }
    }

    class LandingObsidianTearFactory(private val provider: IAnimatedSprite) : IParticleFactory<BasicParticleType> {
        override fun makeParticle(
            typeIn: BasicParticleType, worldIn: World,
            posX: Double, posY: Double, posZ: Double,
            xSpeed: Double, ySpeed: Double, zSpeed: Double
        ): Particle {
            //val landing = LandingObsidianTear()
            TODO()
        }
    }
}