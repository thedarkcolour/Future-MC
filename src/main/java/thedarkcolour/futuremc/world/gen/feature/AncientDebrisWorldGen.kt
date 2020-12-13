package thedarkcolour.futuremc.world.gen.feature

import net.minecraft.block.state.pattern.BlockMatcher
import net.minecraft.init.Blocks
import net.minecraft.world.DimensionType
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.IChunkGenerator
import net.minecraft.world.gen.feature.WorldGenMinable
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.registry.FBlocks
import java.util.*

object AncientDebrisWorldGen : FWorldGen {
    private var normalGen = WorldGenMinable(FBlocks.ANCIENT_DEBRIS.defaultState, 2, BlockMatcher.forBlock(Blocks.NETHERRACK))
    private var lapisStyleGen = WorldGenMinable(FBlocks.ANCIENT_DEBRIS.defaultState, 3, BlockMatcher.forBlock(Blocks.NETHERRACK))

    override fun generate(
        rand: Random,
        chunkX: Int,
        chunkZ: Int,
        worldIn: World,
        chunkGenerator: IChunkGenerator,
        chunkProvider: IChunkProvider
    ) {
        generateNormalOre(worldIn, rand, chunkX, chunkZ, FConfig.netherUpdate.ancientDebris.normalVein.count, FConfig.netherUpdate.ancientDebris.normalVein.minLevel, FConfig.netherUpdate.ancientDebris.normalVein.maxLevel) { w, r, p ->
            if (worldIn.provider.dimensionType == DimensionType.NETHER) {
                normalGen.generate(w, r, p)
            }
        }

        generateLapisStyleOre(worldIn, rand, chunkX, chunkZ, FConfig.netherUpdate.ancientDebris.lapisStyleVein.count, FConfig.netherUpdate.ancientDebris.lapisStyleVein.baseline, FConfig.netherUpdate.ancientDebris.lapisStyleVein.spread) { w, r, p ->
            if (worldIn.provider.dimensionType == DimensionType.NETHER) {
                lapisStyleGen.generate(w, r, p)
            }
        }
    }

    fun refresh() {
        normalGen = WorldGenMinable(FBlocks.ANCIENT_DEBRIS.defaultState, FConfig.netherUpdate.ancientDebris.normalVein.size, BlockMatcher.forBlock(Blocks.NETHERRACK))
        lapisStyleGen = WorldGenMinable(FBlocks.ANCIENT_DEBRIS.defaultState, 3, BlockMatcher.forBlock(Blocks.NETHERRACK))
    }
}