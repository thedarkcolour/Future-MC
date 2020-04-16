package thedarkcolour.futuremc.biome

import net.minecraft.particles.IParticleData
import java.awt.Color
import java.util.*
import java.util.function.ToDoubleFunction

class BiomeEffects private constructor(
        color: Color, val particleType: IParticleData?, private val particleChance: Float,
        private val mX: ToDoubleFunction<Random>,
        private val mY: ToDoubleFunction<Random>,
        private val mZ: ToDoubleFunction<Random>
) {
    val fogR = color.red / 255.0f
    val fogG = color.green / 255.0f
    val fogB = color.blue / 255.0f

    fun shouldAddParticle(rand: Random): Boolean {
        return particleType != null && rand.nextFloat() <= particleChance
    }

    fun getMotionX(rand: Random): Double {
        return mX.applyAsDouble(rand)
    }

    fun getMotionY(rand: Random): Double {
        return mY.applyAsDouble(rand)
    }

    fun getMotionZ(rand: Random): Double {
        return mZ.applyAsDouble(rand)
    }

    companion object {
        private val zero = ToDoubleFunction<Random> { 0.0 }

        @JvmStatic
        fun make(builder: (Builder) -> Unit): BiomeEffects {
            val particleBuilder = Builder()
            builder(particleBuilder)
            return particleBuilder.build()
        }
    }

    class Builder {
        var fogColor = -1
        var mX: ToDoubleFunction<Random>? = null
        var mY: ToDoubleFunction<Random>? = null
        var mZ: ToDoubleFunction<Random>? = null
        var particleType: IParticleData? = null

        var particleChance = -1.0f

        fun build(): BiomeEffects {
            return BiomeEffects(
                    if (fogColor == -1) Color.BLACK else Color(fogColor),
                    particleType,
                    particleChance,
                    mX ?: zero,
                    mY ?: zero,
                    mZ ?: zero
            )
        }
    }
}