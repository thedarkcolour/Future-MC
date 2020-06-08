package thedarkcolour.futuremc.world.biome

import net.minecraft.crash.CrashReport
import net.minecraft.util.ReportedException
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraftforge.common.BiomeDictionary
import thedarkcolour.futuremc.world.IWorld
import thedarkcolour.futuremc.world.gen.*
import thedarkcolour.futuremc.world.gen.chunk.ChunkGenerator
import thedarkcolour.futuremc.world.gen.structure.AbstractStructure
import thedarkcolour.futuremc.world.gen.structure.StructureRefs
import thedarkcolour.futuremc.world.gen.structure.StructureStarts
import thedarkcolour.futuremc.world.gen.structure.WorldGenRandom
import thedarkcolour.futuremc.world.gen.surface.ConfiguredSurfaceBuilder
import java.util.*

open class FBiome(properties: FProperties) : Biome(TODO()) {
    private val features: EnumMap<DecorationStage, MutableList<ConfiguredFeature<*, *>>> =
        EnumMap(DecorationStage::class.java)
    private val carvers: EnumMap<CarvingStage, MutableList<ConfiguredCarver<*, *>>> =
        EnumMap(CarvingStage::class.java)

    fun addFeature(stage: DecorationStage, feature: ConfiguredFeature<*, *>) {
        features.computeIfAbsent(stage) {
            arrayListOf()
        }.add(feature)
    }

    fun addCarver(stage: CarvingStage, carver: ConfiguredCarver<*, *>) {
        carvers.computeIfAbsent(stage) {
            arrayListOf()
        }.add(carver)
    }

    fun getFeatures(stage: DecorationStage): List<ConfiguredFeature<*, *>> {
        return features.computeIfAbsent(stage) {
            arrayListOf()
        }
    }

    fun getCarvers(stage: CarvingStage): List<ConfiguredCarver<*, *>> {
        return carvers.computeIfAbsent(stage) {
            arrayListOf()
        }
    }

    override fun decorate(worldIn: World, rand: Random, pos: BlockPos) {
        val x = pos.x + rand.nextInt(16) + 8
        val z = pos.z + rand.nextInt(16) + 8
        val pos1 = BlockPos(x, 0, z)
        val structureStarts = computeStructureStarts(worldIn, rand, pos1)
        val structureRefs = computeStructureReferences(worldIn, rand, pos1)

        for (stage in DecorationStage.values()) {
            for (feature in features[stage] ?: continue) {
                if (feature is AbstractStructure<*>) {
                    feature.place(worldIn, rand, pos1)
                } else {
                    feature.place(worldIn, rand, pos1, structureStarts, structureRefs)
                }
            }
        }
    }

    private fun computeStructureStarts(worldIn: World, rand: Random, pos1: BlockPos): StructureStarts {
        TODO("not implemented")
    }

    private fun computeStructureReferences(worldIn: World, rand: Random, pos1: BlockPos): StructureRefs {
        TODO("not implemented")
    }

    fun decorate(stage: DecorationStage, generator: ChunkGenerator<*>, worldIn: IWorld, seed: Long, rand: WorldGenRandom, pos: BlockPos) {
        getFeatures(stage).forEachIndexed { i, feature ->
            rand.setFeatureSeed(seed, i, stage.ordinal)

            try {
                feature.place(worldIn, generator, rand, pos)
            } catch (e: Exception) {
                val report = CrashReport.makeCrashReport(e, "Feature placement")
                val cat = report.makeCategory("Feature")
                cat.addDetail("Id") { AbstractFeature.REGISTRY[feature.feature] }
                cat.addDetail("Description", feature.feature::toString)
                throw ReportedException(report)
            }
        }
    }

    class FProperties {
        fun surfaceBuilder(category: ConfiguredSurfaceBuilder): FProperties {
            return this
        }

        fun precipitation(category: WeatherType): FProperties {
            return this
        }

        fun category(category: BiomeDictionary.Type): FProperties {
            return this
        }

        fun depth(category: BiomeDictionary.Type): FProperties {
            return this
        }

        fun scale(category: BiomeDictionary.Type): FProperties {
            return this
        }

        fun temperature(category: BiomeDictionary.Type): FProperties {
            return this
        }

        fun downfall(category: BiomeDictionary.Type): FProperties {
            return this
        }
        fun waterColor(category: BiomeDictionary.Type): FProperties {
            return this
        }
        fun waterFogColor(category: BiomeDictionary.Type): FProperties {
            return this
        }
    }

    enum class WeatherType {
        NONE,
        RAIN,
        SNOW
    }
}