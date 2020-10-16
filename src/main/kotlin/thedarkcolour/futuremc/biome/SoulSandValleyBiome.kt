package thedarkcolour.futuremc.biome

import net.minecraft.block.Blocks
import net.minecraft.entity.EntityClassification
import net.minecraft.entity.EntityType
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.GenerationStage
import net.minecraft.world.gen.carver.WorldCarver
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.IFeatureConfig.NO_FEATURE_CONFIG
import net.minecraft.world.gen.feature.ProbabilityConfig
import net.minecraft.world.gen.placement.CountRangeConfig
import net.minecraft.world.gen.placement.IPlacementConfig
import net.minecraft.world.gen.placement.Placement
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig
import thedarkcolour.futuremc.registry.FFeatures
import thedarkcolour.futuremc.registry.FStructures
import thedarkcolour.futuremc.registry.FSurfaceBuilders

// TODO
class SoulSandValleyBiome : Biome(Builder()
    .surfaceBuilder(FSurfaceBuilders.SOUL_SAND_VALLEY, SOUL_SAND_CONFIG)
    .precipitation(RainType.NONE)
    .category(Category.NETHER)
    .depth(0.1f)
    .scale(0.2f)
    .temperature(2.0f)
    .downfall(0.0f)
    .waterColor(4159204)
    .waterFogColor(329011)
) {
    init {
        addStructureFeature(Feature.NETHER_BRIDGE.configure(NO_FEATURE_CONFIG))
        addStructureFeature(FStructures.NETHER_FOSSIL.configure(NO_FEATURE_CONFIG))
        addCarver(GenerationStage.Carving.AIR, createCarver(WorldCarver.HELL_CAVE, ProbabilityConfig(0.2f)))
        addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.NETHER_BRIDGE.configure(NO_FEATURE_CONFIG).createDecoratedFeature(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)))
        addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, FStructures.NETHER_FOSSIL.configure(NO_FEATURE_CONFIG).createDecoratedFeature(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)))
        addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, FFeatures.BASALT_PILLAR.configure(NO_FEATURE_CONFIG).createDecoratedFeature(Placement.COUNT_RANGE.configure(CountRangeConfig(10, 0, 0, 128))))
        FFeatures.addNetherOres(this)
        addSpawn(EntityClassification.MONSTER, SpawnListEntry(EntityType.SKELETON, 2, 5, 5))
        addSpawn(EntityClassification.MONSTER, SpawnListEntry(EntityType.GHAST, 50, 4, 4))
        addSpawn(EntityClassification.MONSTER, SpawnListEntry(EntityType.ENDERMAN, 1, 4, 4))
        // addSpawn(EntityClassification.CREATURE, SpawnListEntry(EntityType.STRIDER, 60, 2, 4))
    }

    private companion object {
        private val SOUL_SAND_CONFIG = SurfaceBuilderConfig(Blocks.SOUL_SAND.defaultState, Blocks.SOUL_SAND.defaultState, Blocks.SOUL_SAND.defaultState)
    }
}