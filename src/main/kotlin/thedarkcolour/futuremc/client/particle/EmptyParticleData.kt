package thedarkcolour.futuremc.client.particle

import net.minecraft.network.PacketBuffer
import net.minecraft.particles.IParticleData
import net.minecraft.particles.ParticleType

/**
 * Used to indicate the absence of a particle type
 */
object EmptyParticleData : ParticleType<EmptyParticleData>(false, null), IParticleData {
    override fun getParameters(): String {
        return ""
    }

    override fun getType(): ParticleType<*>? {
        return null
    }

    override fun write(buffer: PacketBuffer) {
    }
}