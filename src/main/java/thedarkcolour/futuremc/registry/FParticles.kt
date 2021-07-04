package thedarkcolour.futuremc.registry

import net.minecraft.client.Minecraft
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.util.EnumParticleTypes
import net.minecraftforge.common.util.EnumHelper
import thedarkcolour.core.util.runOnClient
import thedarkcolour.futuremc.client.particle.CampfireParticle
import thedarkcolour.futuremc.client.particle.PandaSneezeParticle
import thedarkcolour.futuremc.client.particle.SoulFlameParticle

object FParticles {
    lateinit var CAMPFIRE_COSY_SMOKE: EnumParticleTypes
    lateinit var CAMPFIRE_SIGNAL_SMOKE: EnumParticleTypes
    lateinit var SOUL_FLAME: EnumParticleTypes
    lateinit var FALLING_BEE_NECTAR: EnumParticleTypes
    lateinit var PANDA_SNEEZE: EnumParticleTypes

    fun registerParticles() {
        CAMPFIRE_COSY_SMOKE = registerParticle(
            "CAMPFIRE_COSY_SMOKE",
            "campfireCosySmoke",
            true,
            CampfireParticle::CosyFactory
        )
        CAMPFIRE_SIGNAL_SMOKE = registerParticle(
            "CAMPFIRE_SIGNAL_SMOKE",
            "campfireSignalSmoke",
            true,
            CampfireParticle::SignalFactory
        )
        SOUL_FLAME = registerParticle(
            "SOUL_FLAME",
            "soulFlame",
            false,
            SoulFlameParticle::Factory
        )/*
        FALLING_BEE_NECTAR = registerParticle(
            "FALLING_BEE_NECTAR",
            "fallingBeeNectar",
            false,
            BeeParticle::FallingNectarFactory
        )*/
        PANDA_SNEEZE = registerParticle(
            "PANDA_SNEEZE",
            "pandaSneeze",
            false,
            PandaSneezeParticle::Factory
        )
    }

    // Registers a particle
    private fun registerParticle(
        enumName: String, particleName: String,
        alwaysShow: Boolean, factory: () -> Any
    ): EnumParticleTypes {
        val id = EnumParticleTypes.values().size
        val particle = EnumHelper.addEnum(
            EnumParticleTypes::class.java,
            enumName,
            arrayOf(String::class.java, Int::class.java, Boolean::class.java),
            particleName,
            id,
            alwaysShow
        )!!

        EnumParticleTypes.PARTICLES[id] = particle
        EnumParticleTypes.BY_NAME[particleName] = particle

        runOnClient {
            Minecraft.getMinecraft().effectRenderer.registerParticle(id, factory() as IParticleFactory)
        }

        return particle
    }
}