package thedarkcolour.futuremc.biome

import net.minecraft.particles.IParticleData
import thedarkcolour.futuremc.registry.FParticles
import java.awt.Color
import java.util.*
import java.util.function.ToDoubleFunction

class BiomeEffects private constructor(
    color: Color, val particleType: IParticleData, private val particleChance: Float,
    private val mXZ: ToDoubleFunction<Random>,
    private val mY: ToDoubleFunction<Random>
) {
    val fogR = color.red / 255.0
    val fogG = color.green / 255.0
    val fogB = color.blue / 255.0

    fun shouldAddParticle(rand: Random): Boolean {
        return particleType != FParticles.EMPTY && rand.nextFloat() <= particleChance
    }

    fun getMotionXZ(rand: Random): Double {
        return mXZ.applyAsDouble(rand)
    }

    fun getMotionY(rand: Random): Double {
        return mY.applyAsDouble(rand)
    }

    companion object {
        private val zero = ToDoubleFunction<Random> { 0.0 }

        val NETHER = make { builder ->
            builder.fogColor = 3344392
        }

        @JvmStatic
        fun make(builder: (builder: Builder) -> Unit): BiomeEffects {
            val particleBuilder = Builder()
            builder(particleBuilder)
            return particleBuilder.build()
        }
    }

    class Builder {
        var fogColor = -1
        var mXZ: ToDoubleFunction<Random>? = null
        var mY: ToDoubleFunction<Random>? = null
        var particleType: IParticleData? = null

        var particleChance = -1.0f

        fun build(): BiomeEffects {
            return BiomeEffects(
                color          = if (fogColor == -1) Color.BLACK else Color(fogColor),
                particleType   = particleType ?: FParticles.EMPTY,
                particleChance = particleChance,
                mXZ            = mXZ ?: zero,
                mY             = mY ?: zero,
            )
        }
    }
}