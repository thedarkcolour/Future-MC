package thedarkcolour.futuremc.biome.provider

import net.minecraft.util.SharedSeedRandom
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraft.world.gen.OctavesNoiseGenerator
import thedarkcolour.futuremc.registry.FBiomes
import java.util.*

class MultiNoiseBiomeProvider(settings: MultiNoiseBiomeProviderSettings) : BiomeProvider(biomes) {
    private val temperatureNoise = OctavesNoiseGenerator(SharedSeedRandom(settings.seed), 8, -1)
    private val humidityOctaves = OctavesNoiseGenerator(SharedSeedRandom(settings.seed), 8, -1)
    private val hillinessOctaves = OctavesNoiseGenerator(SharedSeedRandom(settings.seed), 9, -1)
    private val styleOctaves = OctavesNoiseGenerator(SharedSeedRandom(settings.seed), 8, -1)

    /**
     * Customize by registering a [NoisePoint] for your biome
     *
     * Use [FBiomes.addNoisePoints] to add your own noise points
     */
    override fun getBiomeForNoiseGen(x: Int, y: Int, z: Int): Biome {
        val f = x * 1.0181268882175227
        val g = z * 1.0181268882175227
        val h = x * 1.0
        val i = z * 1.0
        val noiseEntry = NoisePoint(
                (temperatureNoise.func_205563_a(f, 0.0, g) + temperatureNoise.func_205563_a(h, 0.0, i)).toFloat(),
                (humidityOctaves.func_205563_a(f, 0.0, g) + humidityOctaves.func_205563_a(h, 0.0, i)).toFloat(),
                (hillinessOctaves.func_205563_a(f, 0.0, g) + hillinessOctaves.func_205563_a(h, 0.0, i)).toFloat(),
                (styleOctaves.func_205563_a(f, 0.0, g) + styleOctaves.func_205563_a(h, 0.0, i)).toFloat(),
                1.0f
        )

        return biomes.stream().min(Comparator.comparing { biome: Biome ->
            getDistanceBetween(biome, noiseEntry)
        }).orElse(Biomes.THE_END)
    }

    companion object {
        // TODO Add nether biomes
        //      thru FBiomes
        private val biomes = hashSetOf<Biome>(Biomes.NETHER, FBiomes.WARPED_FOREST, FBiomes.CRIMSON_FOREST, FBiomes.SOUL_SAND_VALLEY)
    }

    private fun getDistanceBetween(biome: Biome, other: NoisePoint): Float {
        return FBiomes.getNoisePoints(biome).map(other::getDistance).min() ?: Float.POSITIVE_INFINITY
    }
}