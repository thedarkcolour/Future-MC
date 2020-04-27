package thedarkcolour.futuremc.registry

import net.minecraft.client.Minecraft
import net.minecraft.client.particle.FlameParticle
import net.minecraft.particles.BasicParticleType
import net.minecraft.particles.ParticleType
import net.minecraftforge.registries.IForgeRegistry
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.particle.AshParticle
import thedarkcolour.futuremc.particle.NetherSporeParticle

object FParticles {
    val SOUL_FIRE_FLAME = BasicParticleType(false).setRegistryKey("soul_fire_flame")
    val WARPED_SPORE = BasicParticleType(false).setRegistryKey("warped_spore")
    val CRIMSON_SPORE = BasicParticleType(false).setRegistryKey("crimson_spore")
    val ASH = BasicParticleType(false).setRegistryKey("ash")

    fun registerParticles(particles: IForgeRegistry<ParticleType<*>>) {
        if (FutureMC.DEBUG) {
            particles.register(SOUL_FIRE_FLAME)
            particles.register(WARPED_SPORE)
            particles.register(CRIMSON_SPORE)
            particles.register(ASH)
        }
    }

    fun registerParticleFactories() {
        val particles = Minecraft.getInstance().particles

        if (FutureMC.DEBUG) {
            particles.registerFactory(SOUL_FIRE_FLAME, FlameParticle::Factory)
            particles.registerFactory(WARPED_SPORE, NetherSporeParticle::WarpedSporeFactory)
            particles.registerFactory(CRIMSON_SPORE, NetherSporeParticle::CrimsonSporeFactory)
            particles.registerFactory(ASH, AshParticle::Factory)
        }
    }
}