package thedarkcolour.futuremc.client.particle

import net.minecraft.client.Minecraft
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.util.EnumParticleTypes
import net.minecraftforge.common.util.EnumHelper
import thedarkcolour.core.util.runOnClient

object Particles {
    lateinit var CAMPFIRE_COSY_SMOKE: EnumParticleTypes
    lateinit var CAMPFIRE_SIGNAL_SMOKE: EnumParticleTypes

    // Called during FMLInitializationEvent
    fun registerParticle(enumName: String, particleName: String, alwaysShow: Boolean, factory: IParticleFactory): EnumParticleTypes {
        val id = EnumParticleTypes.values().size
        val particle = EnumHelper.addEnum(EnumParticleTypes::class.java, enumName, arrayOf(String::class.java, Int::class.java, Boolean::class.java), particleName, id, alwaysShow)!!

        EnumParticleTypes.PARTICLES[id] = particle
        EnumParticleTypes.BY_NAME[particleName] = particle

        runOnClient {
            Minecraft.getMinecraft().effectRenderer.registerParticle(id, factory)
        }

        return particle
    }
}