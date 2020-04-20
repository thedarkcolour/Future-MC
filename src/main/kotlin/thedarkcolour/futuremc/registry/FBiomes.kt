package thedarkcolour.futuremc.registry

import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import net.minecraft.world.gen.GenerationStage
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.OreFeatureConfig
import net.minecraft.world.gen.placement.CountRangeConfig
import net.minecraft.world.gen.placement.DepthAverageConfig
import net.minecraft.world.gen.placement.Placement
import net.minecraftforge.registries.IForgeRegistry
import thedarkcolour.futuremc.biome.BiomeEffects
import thedarkcolour.futuremc.biome.CrimsonForestBiome
import thedarkcolour.futuremc.biome.SoulSandValleyBiome
import thedarkcolour.futuremc.biome.WarpedForestBiome
import thedarkcolour.futuremc.biome.provider.NoisePoint
import thedarkcolour.futuremc.compat.checkBiomesOPlenty
import thedarkcolour.futuremc.config.Config
import java.util.*
import java.util.function.ToDoubleFunction

object FBiomes {
    val WARPED_FOREST = WarpedForestBiome().setRegistryKey("warped_forest")
    val CRIMSON_FOREST = CrimsonForestBiome().setRegistryKey("crimson_forest")
    val SOUL_SAND_VALLEY = SoulSandValleyBiome().setRegistryKey("soul_sand_valley")

    fun onBiomeRegistry(biomes: IForgeRegistry<Biome>) {
        if (Config.ancientDebrisGenerates.value) {
            Biomes.NETHER.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.ORE.configure(OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, FBlocks.ANCIENT_DEBRIS.defaultState, 3)).createDecoratedFeature(Placement.COUNT_DEPTH_AVERAGE.configure(DepthAverageConfig(1, 16, 8))))
            Biomes.NETHER.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.ORE.configure(OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, FBlocks.ANCIENT_DEBRIS.defaultState, 2)).createDecoratedFeature(Placement.COUNT_RANGE.configure(CountRangeConfig(1, 8, 16, 128))))
        }

        registerBiomes(biomes)
        addBiomeNoise()
        addBiomeParticles()
    }

    private fun registerBiomes(biomes: IForgeRegistry<Biome>) {
        if (Config.warpedForest.value) {
            biomes.register(WARPED_FOREST)
            checkBiomesOPlenty()?.addNetherBiome(WARPED_FOREST)
        }
        if (Config.crimsonForest.value) {
            biomes.register(CRIMSON_FOREST)
            checkBiomesOPlenty()?.addNetherBiome(CRIMSON_FOREST)
        }
        if (Config.soulSandValley.value) {
            biomes.register(SOUL_SAND_VALLEY)
            checkBiomesOPlenty()?.addNetherBiome(SOUL_SAND_VALLEY)
        }
    }

    private fun addBiomeNoise() {
        addNoisePoints(WARPED_FOREST, NoisePoint(0.0f, 0.5f, 0.0f, 0.0f, 1.0f))
        addNoisePoints(CRIMSON_FOREST, NoisePoint(0.0f, -0.5f, 0.0f, 0.0f, 1.0f))
        addNoisePoints(SOUL_SAND_VALLEY, NoisePoint(0.0f, 0.0f, 0.0f, 0.5f, 1.0f))
        addNoisePoints(Biomes.NETHER, NoisePoint(0.0f, 0.0f, 0.0f, -0.5f, 1.0f))
    }

    private fun addBiomeParticles() {
        addEffects(WARPED_FOREST, BiomeEffects.make { builder ->
            builder.fogColor = 1705242
            builder.particleType = FParticles.WARPED_SPORE
            builder.particleChance = 0.01428f
            builder.mY = ToDoubleFunction { random ->
                random.nextFloat() * -1.9 * random.nextFloat() * 0.1
            }
        })
        addEffects(SOUL_SAND_VALLEY, BiomeEffects.make { builder ->
            builder.fogColor = 1787717
            builder.particleType = FParticles.ASH
            builder.particleChance = 0.00625f
        })
        addEffects(Biomes.NETHER, BiomeEffects.make { builder ->
            builder.fogColor = 3344392
        })
    }

    private val biomeParticles = hashMapOf<Biome, BiomeEffects>()
    private val noisePoints = hashMapOf<Biome, ArrayList<NoisePoint>>()

    @JvmStatic
    fun getParticles(biome: Biome): BiomeEffects? {
        return biomeParticles[biome]
    }

    @JvmStatic
    fun getNoisePoints(biome: Biome): List<NoisePoint> {
        return noisePoints[biome] ?: emptyList()
    }

    @JvmStatic
    fun addNoisePoints(biome: Biome, vararg points: NoisePoint): Biome {
        noisePoints.getOrPut(biome, ::arrayListOf).addAll(points)
        return biome
    }

    @JvmStatic
    fun addEffects(biome: Biome, effects: BiomeEffects) {
        biomeParticles[biome] = effects
    }
}