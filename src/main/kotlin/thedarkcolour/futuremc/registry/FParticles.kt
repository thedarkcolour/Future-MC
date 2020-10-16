package thedarkcolour.futuremc.registry

import net.minecraft.client.Minecraft
import net.minecraft.client.particle.FlameParticle
import net.minecraft.particles.BasicParticleType
import net.minecraft.particles.IParticleData
import net.minecraft.particles.ParticleType
import net.minecraftforge.registries.IForgeRegistry
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.client.particle.AshParticle
import thedarkcolour.futuremc.client.particle.EmptyParticleData
import thedarkcolour.futuremc.client.particle.NetherSporeParticle

object FParticles {
    val SOUL_FIRE_FLAME = BasicParticleType(false).setRegistryKey("soul_fire_flame")
    val WARPED_SPORE = BasicParticleType(false).setRegistryKey("warped_spore")
    val CRIMSON_SPORE = BasicParticleType(false).setRegistryKey("crimson_spore")
    val ASH = BasicParticleType(false).setRegistryKey("ash")
    val WHITE_ASH = BasicParticleType(false).setRegistryKey("white_ash")
    val DRIPPING_OBSIDIAN_TEAR = BasicParticleType(false).setRegistryKey("dripping_obsidian_tear")
    val FALLING_OBSIDIAN_TEAR = BasicParticleType(false).setRegistryKey("falling_obsidian_tear")
    val LANDING_OBSIDIAN_TEAR = BasicParticleType(false).setRegistryKey("landing_obsidian_tear")
    val EMPTY = EmptyParticleData

    fun registerParticles(particles: IForgeRegistry<ParticleType<*>>) {
        if (FutureMC.DEBUG) {
            particles.register(SOUL_FIRE_FLAME)
            particles.register(WARPED_SPORE)
            particles.register(CRIMSON_SPORE)
            particles.register(ASH)
            particles.register(WHITE_ASH)
            // particles.register(DRIPPING_OBSIDIAN_TEAR)
            // particles.register(FALLING_OBSIDIAN_TEAR)
            // particles.register(LANDING_OBSIDIAN_TEAR)
        }
    }

    fun registerParticleFactories() {
        val particles = Minecraft.getInstance().particles

        if (FutureMC.DEBUG) {
            particles.registerFactory(SOUL_FIRE_FLAME, FlameParticle::Factory)
            particles.registerFactory(WARPED_SPORE, NetherSporeParticle::WarpedSporeFactory)
            particles.registerFactory(CRIMSON_SPORE, NetherSporeParticle::CrimsonSporeFactory)
            particles.registerFactory(ASH, AshParticle::AshFactory)
            particles.registerFactory(WHITE_ASH, AshParticle::WhiteAshFactory)
            // particles.registerFactory(DRIPPING_OBSIDIAN_TEAR, AshParticle::WhiteAshFactory)
            // particles.registerFactory(FALLING_OBSIDIAN_TEAR, AshParticle::WhiteAshFactory)
            // particles.registerFactory(LANDING_OBSIDIAN_TEAR, AshParticle::WhiteAshFactory)
        }
    }
}