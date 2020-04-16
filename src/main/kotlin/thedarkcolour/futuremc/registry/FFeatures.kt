package thedarkcolour.futuremc.registry

import net.minecraft.block.Blocks
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.GenerationStage
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider
import net.minecraft.world.gen.feature.*
import net.minecraft.world.gen.placement.CountRangeConfig
import net.minecraft.world.gen.placement.DepthAverageConfig
import net.minecraft.world.gen.placement.FrequencyConfig
import net.minecraft.world.gen.placement.Placement
import net.minecraftforge.registries.IForgeRegistry
import thedarkcolour.futuremc.config.Config
import thedarkcolour.futuremc.feature.*

// todo fix vines
object FFeatures {
    val HUGE_FUNGUS = HugeFungusFeature(::HugeFungusFeatureConfig).setRegistryKey("huge_fungus")
    val NETHER_FOREST_VEGETATION = NetherForestVegetationFeature { BlockStateProvidingFeatureConfig.deserialize(it) }.setRegistryKey("nether_forest_vegetation")
    val TWISTING_VINES = TwistingVinesFeature { NoFeatureConfig.deserialize(it) }.setRegistryKey("twisting_vines")
    val WEEPING_VINES = WeepingVinesFeature { NoFeatureConfig.deserialize(it) }.setRegistryKey("weeping_vines")
    val BASALT_PILLAR = BasaltPillarFeature { NoFeatureConfig.deserialize(it) }.setRegistryKey("basalt_pillar")

    val SOUL_FIRE_CONFIG = BlockClusterFeatureConfig.Builder(SimpleBlockStateProvider(FBlocks.SOUL_FIRE.defaultState), SimpleBlockPlacer()).tries(64).whitelist(setOf(FBlocks.SOUL_SOIL)).cannotProject().build()
    val CRIMSON_ROOTS_CONFIG = BlockStateProvidingFeatureConfig(WeightedBlockStateProvider().addState(FBlocks.CRIMSON_ROOTS.defaultState, 87).addState(FBlocks.CRIMSON_FUNGUS.defaultState, 11).addState(FBlocks.WARPED_FUNGUS.defaultState, 1))
    val WARPED_ROOTS_CONFIG = BlockStateProvidingFeatureConfig(WeightedBlockStateProvider().addState(FBlocks.WARPED_ROOTS.defaultState, 85).addState(FBlocks.CRIMSON_ROOTS.defaultState, 1).addState(FBlocks.WARPED_FUNGUS.defaultState, 13).addState(FBlocks.CRIMSON_FUNGUS.defaultState, 1))
    val NETHER_SPROUTS_CONFIG = BlockStateProvidingFeatureConfig(SimpleBlockStateProvider(FBlocks.NETHER_SPROUTS.defaultState))

    fun registerFeatures(features: IForgeRegistry<Feature<*>>) {
        features.register(HUGE_FUNGUS)
        features.register(NETHER_FOREST_VEGETATION)
        // features.register(TWISTING_VINES)
        // features.register(WEEPING_VINES)
        // features.register(BASALT_PILLAR)

        FStructures.registerStructures(features)
    }

    fun addWarpedForestVegetation(biome: Biome) {
        biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, HUGE_FUNGUS.configure(HugeFungusFeatureConfig.WARPED).createDecoratedFeature(Placement.COUNT_HEIGHTMAP.configure(FrequencyConfig(8))))
        biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, NETHER_FOREST_VEGETATION.configure(WARPED_ROOTS_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP.configure(FrequencyConfig(5))))
        biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, NETHER_FOREST_VEGETATION.configure(NETHER_SPROUTS_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP.configure(FrequencyConfig(4))))
        // biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TWISTING_VINES.configure(IFeatureConfig.NO_FEATURE_CONFIG).createDecoratedFeature(Placement.COUNT_RANGE.configure(CountRangeConfig(10, 0, 0, 128))))
    }

    fun addCrimsonForestVegetation(biome: Biome) {
        biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, HUGE_FUNGUS.configure(HugeFungusFeatureConfig.CRIMSON).createDecoratedFeature(Placement.COUNT_HEIGHTMAP.configure(FrequencyConfig(8))))
        biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, NETHER_FOREST_VEGETATION.configure(CRIMSON_ROOTS_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP.configure(FrequencyConfig(6))))
        // biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, WEEPING_VINES.configure(IFeatureConfig.NO_FEATURE_CONFIG).createDecoratedFeature(Placement.COUNT_RANGE.configure(CountRangeConfig(10, 0, 0, 128))))
    }

    fun addNetherOres(biome: Biome) {
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.ORE.configure(OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, FBlocks.NETHER_GOLD_ORE.defaultState, 10)))
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.ORE.configure(OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, Blocks.NETHER_QUARTZ_ORE.defaultState, 14)).createDecoratedFeature(Placement.COUNT_RANGE.configure(CountRangeConfig(16, 10, 20, 128))))
        // TODO Add NO_SURFACE_ORE
        if (Config.ancientDebrisGenerates.value) {
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.ORE.configure(OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, FBlocks.ANCIENT_DEBRIS.defaultState, 2)).createDecoratedFeature(Placement.COUNT_RANGE.configure(CountRangeConfig(1, 8, 16, 128))))
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.ORE.configure(OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, FBlocks.ANCIENT_DEBRIS.defaultState, 3)).createDecoratedFeature(Placement.COUNT_DEPTH_AVERAGE.configure(DepthAverageConfig(1, 16, 8))))
        }
    }
}