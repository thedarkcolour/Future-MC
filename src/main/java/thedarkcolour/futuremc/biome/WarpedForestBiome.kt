package thedarkcolour.futuremc.biome

import net.minecraft.block.Blocks
import net.minecraft.entity.EntityClassification
import net.minecraft.entity.EntityType
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.DefaultBiomeFeatures
import net.minecraft.world.gen.GenerationStage
import net.minecraft.world.gen.carver.WorldCarver
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.IFeatureConfig
import net.minecraft.world.gen.feature.OreFeatureConfig
import net.minecraft.world.gen.feature.ProbabilityConfig
import net.minecraft.world.gen.placement.CountRangeConfig
import net.minecraft.world.gen.placement.FrequencyConfig
import net.minecraft.world.gen.placement.IPlacementConfig
import net.minecraft.world.gen.placement.Placement
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig
import thedarkcolour.futuremc.registry.FBlocks
import thedarkcolour.futuremc.registry.FFeatures
import thedarkcolour.futuremc.registry.FSurfaceBuilders

class WarpedForestBiome : Biome(Builder()
        .surfaceBuilder(FSurfaceBuilders.NETHER_FOREST, WARPED_NYLIUM_CONFIG)
        .precipitation(RainType.NONE)
        .category(Category.NETHER)
        .depth(0.1F)
        .scale(0.2F)
        .temperature(2.0F)
        .downfall(0.0F)
        .waterColor(4159204)
        .waterFogColor(329011)
) {
    init {
        addCarver(GenerationStage.Carving.AIR, createCarver(WorldCarver.HELL_CAVE, ProbabilityConfig(0.2f)))
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(DefaultBiomeFeatures.LAVA_SPRING_CONFIG).createDecoratedFeature(Placement.COUNT_VERY_BIASED_RANGE.configure(CountRangeConfig(20, 8, 16, 256))))
        DefaultBiomeFeatures.addMushrooms(this)
        addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.NETHER_BRIDGE.configure(IFeatureConfig.NO_FEATURE_CONFIG).createDecoratedFeature(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)))
        addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.SPRING_FEATURE.configure(DefaultBiomeFeatures.NETHER_SPRING_CONFIG).createDecoratedFeature(Placement.COUNT_RANGE.configure(CountRangeConfig(8, 4, 8, 128))))
        addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.RANDOM_PATCH.configure(DefaultBiomeFeatures.NETHER_FIRE_CONFIG).createDecoratedFeature(Placement.HELL_FIRE.configure(FrequencyConfig(10))))
        addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.RANDOM_PATCH.configure(FFeatures.SOUL_FIRE_CONFIG).createDecoratedFeature(Placement.HELL_FIRE.configure(FrequencyConfig(10))))
        addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.GLOWSTONE_BLOB.configure(IFeatureConfig.NO_FEATURE_CONFIG).createDecoratedFeature(Placement.LIGHT_GEM_CHANCE.configure(FrequencyConfig(10))))
        addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.GLOWSTONE_BLOB.configure(IFeatureConfig.NO_FEATURE_CONFIG).createDecoratedFeature(Placement.COUNT_RANGE.configure(CountRangeConfig(10, 0, 0, 128))))
        addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.ORE.configure(OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, Blocks.MAGMA_BLOCK.defaultState, 33)).createDecoratedFeature(Placement.MAGMA.configure(FrequencyConfig(4))))
        addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.SPRING_FEATURE.configure(DefaultBiomeFeatures.ENCLOSED_NETHER_SPRING_CONFIG).createDecoratedFeature(Placement.COUNT_RANGE.configure(CountRangeConfig(16, 10, 20, 128))))
        FFeatures.addWarpedForestVegetation(this)
        FFeatures.addNetherOres(this)
        addSpawn(EntityClassification.MONSTER, SpawnListEntry(EntityType.ENDERMAN, 1, 4, 4))
    }

    companion object {
        private val WARPED_NYLIUM_CONFIG = SurfaceBuilderConfig(FBlocks.WARPED_NYLIUM.defaultState, SurfaceBuilder.NETHERRACK, FBlocks.WARPED_WART_BLOCK.defaultState)
    }
}