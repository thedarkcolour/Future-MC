package thedarkcolour.futuremc.biome

import net.minecraft.block.Blocks
import net.minecraft.entity.EntityClassification
import net.minecraft.entity.EntityType
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.DefaultBiomeFeatures
import net.minecraft.world.gen.GenerationStage.Carving
import net.minecraft.world.gen.GenerationStage.Decoration
import net.minecraft.world.gen.carver.WorldCarver
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.IFeatureConfig
import net.minecraft.world.gen.feature.OreFeatureConfig
import net.minecraft.world.gen.feature.ProbabilityConfig
import net.minecraft.world.gen.placement.ChanceRangeConfig
import net.minecraft.world.gen.placement.CountRangeConfig
import net.minecraft.world.gen.placement.FrequencyConfig
import net.minecraft.world.gen.placement.Placement
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig
import thedarkcolour.futuremc.feature.BasaltColumnsConfig
import thedarkcolour.futuremc.feature.DeltaFeatureConfig
import thedarkcolour.futuremc.feature.NetherrackBlobReplacementConfig
import thedarkcolour.futuremc.registry.FBlocks
import thedarkcolour.futuremc.registry.FFeatures
import thedarkcolour.futuremc.registry.FSurfaceBuilders

// TODO Basalt Deltas
class BasaltDeltasBiome : Biome(
    Builder()
        .surfaceBuilder(FSurfaceBuilders.BASALT_DELTAS, BASALT_DELTA_CONFIG)
        .precipitation(RainType.NONE)
        .category(Category.NETHER)
        .depth(0.1f)
        .scale(0.2f)
        .temperature(2.0f)
        .downfall(0.0f)
        .waterColor(4159204)
        .waterFogColor(4341314)
) {
    // @formatter:off
    init {
        addStructureFeature(Feature.NETHER_BRIDGE.configure(IFeatureConfig.NO_FEATURE_CONFIG))
        addFeature(Decoration.UNDERGROUND_DECORATION, Feature.NETHER_BRIDGE.configure(IFeatureConfig.NO_FEATURE_CONFIG))
        // todo ruined nether portal

        addCarver(Carving.AIR, createCarver(WorldCarver.HELL_CAVE, ProbabilityConfig(0.2f)))

        addFeature(Decoration.SURFACE_STRUCTURES, FFeatures.DELTA_FEATURE.configure(DELTA_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP.configure(FrequencyConfig(40))))
        addFeature(Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(DefaultBiomeFeatures.LAVA_SPRING_CONFIG).createDecoratedFeature(Placement.COUNT_VERY_BIASED_RANGE.configure(CountRangeConfig(40, 8, 16, 256))))
        addFeature(Decoration.UNDERGROUND_DECORATION, Feature.SPRING_FEATURE.configure(DefaultBiomeFeatures.ENCLOSED_NETHER_SPRING_CONFIG).createDecoratedFeature(Placement.COUNT_RANGE.configure(CountRangeConfig(32, 10, 20, 128))))
        addFeature(Decoration.SURFACE_STRUCTURES, FFeatures.BASALT_COLUMNS.configure(SMALL_BASALT_COLUMNS).createDecoratedFeature(Placement.COUNT_HEIGHTMAP.configure(FrequencyConfig(4))))
        addFeature(Decoration.SURFACE_STRUCTURES, FFeatures.BASALT_COLUMNS.configure(LARGE_BASALT_COLUMNS).createDecoratedFeature(Placement.COUNT_HEIGHTMAP.configure(FrequencyConfig(2))))
        addFeature(Decoration.UNDERGROUND_DECORATION, FFeatures.NETHERRACK_BLOB_REPLACEMENT.configure(BASALT_BLOBS).createDecoratedFeature(Placement.COUNT_RANGE.configure(CountRangeConfig(75, 0, 0, 128))))
        addFeature(Decoration.UNDERGROUND_DECORATION, FFeatures.NETHERRACK_BLOB_REPLACEMENT.configure(BLACKSTONE_BLOBS).createDecoratedFeature(Placement.COUNT_RANGE.configure(CountRangeConfig(25, 0, 0, 128))))
        addFeature(Decoration.UNDERGROUND_DECORATION, Feature.SPRING_FEATURE.configure(DefaultBiomeFeatures.NETHER_SPRING_CONFIG).createDecoratedFeature(Placement.COUNT_RANGE.configure(CountRangeConfig(16, 4, 8, 128))))
        addFeature(Decoration.UNDERGROUND_DECORATION, Feature.RANDOM_PATCH.configure(DefaultBiomeFeatures.NETHER_FIRE_CONFIG).createDecoratedFeature(Placement.HELL_FIRE.configure(FrequencyConfig(10))))
        addFeature(Decoration.UNDERGROUND_DECORATION, Feature.RANDOM_PATCH.configure(FFeatures.SOUL_FIRE_CONFIG).createDecoratedFeature(Placement.HELL_FIRE.configure(FrequencyConfig(10))))
        addFeature(Decoration.UNDERGROUND_DECORATION, Feature.GLOWSTONE_BLOB.configure(IFeatureConfig.NO_FEATURE_CONFIG).createDecoratedFeature(Placement.LIGHT_GEM_CHANCE.configure(FrequencyConfig(10))))
        addFeature(Decoration.UNDERGROUND_DECORATION, Feature.GLOWSTONE_BLOB.configure(IFeatureConfig.NO_FEATURE_CONFIG).createDecoratedFeature(Placement.COUNT_RANGE.configure(CountRangeConfig(10, 0, 0, 128))))
        addFeature(Decoration.UNDERGROUND_DECORATION, Feature.RANDOM_PATCH.configure(DefaultBiomeFeatures.BROWN_MUSHROOM_CONFIG).createDecoratedFeature(Placement.CHANCE_RANGE.configure(ChanceRangeConfig(0.5F, 0, 0, 128))))
        addFeature(Decoration.UNDERGROUND_DECORATION, Feature.RANDOM_PATCH.configure(DefaultBiomeFeatures.RED_MUSHROOM_CONFIG).createDecoratedFeature(Placement.CHANCE_RANGE.configure(ChanceRangeConfig(0.5F, 0, 0, 128))))
        addFeature(Decoration.UNDERGROUND_DECORATION, Feature.ORE.configure(OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, Blocks.MAGMA_BLOCK.defaultState, 33)).createDecoratedFeature(Placement.MAGMA.configure(FrequencyConfig(4))))

        FFeatures.addNetherOres(this, 20, 32)

        addSpawn(EntityClassification.MONSTER, SpawnListEntry(EntityType.GHAST, 40, 1, 1))
        addSpawn(EntityClassification.MONSTER, SpawnListEntry(EntityType.MAGMA_CUBE, 100, 2, 5))
        // todo strider
    }

    private companion object {
        private val BASALT_DELTA_CONFIG = SurfaceBuilderConfig(FBlocks.BLACKSTONE.defaultState, FBlocks.BASALT.defaultState, Blocks.MAGMA_BLOCK.defaultState)
        private val DELTA_CONFIG = DeltaFeatureConfig(Blocks.LAVA.defaultState, Blocks.MAGMA_BLOCK.defaultState, 3, 7, 2)
        private val SMALL_BASALT_COLUMNS = BasaltColumnsConfig(1, 1, 1, 4)
        private val LARGE_BASALT_COLUMNS = BasaltColumnsConfig(2, 3, 5, 10)
        private val BASALT_BLOBS = NetherrackBlobReplacementConfig(3, 4, Blocks.NETHERRACK.defaultState, FBlocks.BASALT.defaultState)
        private val BLACKSTONE_BLOBS = NetherrackBlobReplacementConfig(3, 4, Blocks.NETHERRACK.defaultState, FBlocks.BLACKSTONE.defaultState)
    }
}