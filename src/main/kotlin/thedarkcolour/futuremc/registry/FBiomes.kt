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
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.biome.*
import thedarkcolour.futuremc.biome.provider.NoisePoint
import thedarkcolour.futuremc.compat.checkBiomesOPlenty
import thedarkcolour.futuremc.config.Config
import java.util.*
import java.util.function.ToDoubleFunction

object FBiomes {
    val WARPED_FOREST = WarpedForestBiome().setRegistryKey("warped_forest")
    val CRIMSON_FOREST = CrimsonForestBiome().setRegistryKey("crimson_forest")
    val SOUL_SAND_VALLEY = SoulSandValleyBiome().setRegistryKey("soul_sand_valley")
    val BASALT_DELTAS = BasaltDeltasBiome().setRegistryKey("basalt_deltas")

    fun onBiomeRegistry(biomes: IForgeRegistry<Biome>) {
        if (Config.ancientDebrisGenerates.value) {
            // lapis-style generation
            Biomes.NETHER.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.ORE.configure(OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, FBlocks.ANCIENT_DEBRIS.defaultState, 3)).createDecoratedFeature(Placement.COUNT_DEPTH_AVERAGE.configure(DepthAverageConfig(1, 16, 8))))
            // normal generation
            Biomes.NETHER.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.ORE.configure(OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, FBlocks.ANCIENT_DEBRIS.defaultState, 2)).createDecoratedFeature(Placement.COUNT_RANGE.configure(CountRangeConfig(1, 8, 16, 128))))
        }

        if (FutureMC.DEBUG) {
            registerBiomes(biomes)
            addBiomeNoise()
            addBiomeParticles()
        }
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
        if (FutureMC.DEBUG) {
            biomes.register(BASALT_DELTAS)
            checkBiomesOPlenty()?.addNetherBiome(BASALT_DELTAS)
        }
    }

    private fun addBiomeNoise() {
        addNoisePoints(WARPED_FOREST, NoisePoint(0.0f, 0.5f, 0.0f, 0.0f, 1.0f))
        addNoisePoints(CRIMSON_FOREST, NoisePoint(0.0f, -0.5f, 0.0f, 0.0f, 1.0f))
        addNoisePoints(SOUL_SAND_VALLEY, NoisePoint(0.0f, 0.0f, 0.0f, 0.5f, 1.0f))
        addNoisePoints(BASALT_DELTAS, NoisePoint(-0.7f, 0.0f, 0.0f, 0.0f, 0.3f))
        addNoisePoints(Biomes.NETHER, NoisePoint(0.0f, 0.0f, 0.0f, -0.5f, 1.0f))
    }

    // todo move these effects into BiomeEffects.kt
    private fun addBiomeParticles() {
        addEffects(WARPED_FOREST, BiomeEffects.make { builder ->
            builder.fogColor = 1705242
            builder.particleType = FParticles.WARPED_SPORE
            builder.particleChance = 0.01428f
            builder.mY = ToDoubleFunction { random ->
                random.nextFloat() * -1.9 * random.nextFloat() * 0.1
            }
        })
        addEffects(CRIMSON_FOREST, BiomeEffects.make { builder ->
            builder.fogColor = 3343107
            builder.particleType = FParticles.CRIMSON_SPORE
            builder.particleChance = 0.025f
            builder.mXZ = ToDoubleFunction { random ->
                random.nextGaussian() * 9.999999974752427E-7
            }
            builder.mY = ToDoubleFunction { random ->
                random.nextGaussian() * 9.999999747378752E-5
            }
        })
        addEffects(SOUL_SAND_VALLEY, BiomeEffects.make { builder ->
            builder.fogColor = 1787717
            builder.particleType = FParticles.ASH
            builder.particleChance = 0.00625f
        })
        addEffects(BASALT_DELTAS, BiomeEffects.make { builder ->
            builder.fogColor = 6840176
            builder.particleType = FParticles.WHITE_ASH
            builder.particleChance = 0.118093334f
            builder.mXZ = ToDoubleFunction { random ->
                random.nextFloat() * -1.9 * random.nextFloat() * 0.1
            }
            builder.mY = ToDoubleFunction { random ->
                random.nextFloat() * -0.5 * random.nextFloat() * 0.1 * 5.0
            }
        })
        addEffects(Biomes.NETHER, BiomeEffects.NETHER)
    }

    private val biomeParticles = hashMapOf<Biome, BiomeEffects>()
    private val noisePoints = hashMapOf<Biome, ArrayList<NoisePoint>>()

    @JvmStatic
    fun getBiomeEffects(biome: Biome): BiomeEffects? {
        return biomeParticles[biome]
    }

    @JvmStatic
    fun hasBiomeEffects(biome: Biome): Boolean {
        return biomeParticles.contains(biome)
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