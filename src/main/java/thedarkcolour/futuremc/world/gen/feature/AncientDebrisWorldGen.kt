package thedarkcolour.futuremc.world.gen.feature

import net.minecraft.block.state.pattern.BlockMatcher
import net.minecraft.init.Biomes
import net.minecraft.init.Blocks
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.IChunkGenerator
import net.minecraft.world.gen.feature.WorldGenMinable
import net.minecraftforge.common.BiomeDictionary
import thedarkcolour.futuremc.registry.FBlocks
import thedarkcolour.futuremc.world.gen.structure.IChunkPos
import java.util.*

object AncientDebrisWorldGen : FWorldGen {
    private val normalGen = WorldGenMinable(FBlocks.ANCIENT_DEBRIS.defaultState, 2, BlockMatcher.forBlock(Blocks.NETHERRACK))
    private val lapisStyleGen = WorldGenMinable(FBlocks.ANCIENT_DEBRIS.defaultState, 3, BlockMatcher.forBlock(Blocks.NETHERRACK))

    override fun generate(
        rand: Random,
        chunkX: Int,
        chunkZ: Int,
        worldIn: World,
        chunkGenerator: IChunkGenerator,
        chunkProvider: IChunkProvider
    ) {
        if (worldIn.provider.dimension != -1) return

        // compiles to `long`
        val pos = IChunkPos.of(chunkX, chunkZ)

        generateNormalOre(worldIn, rand, pos, 1, 16, 128) { w, r, p ->
            if (BiomeDictionary.areSimilar(w.getBiome(p), Biomes.HELL)) {
                normalGen.generate(w, r, p)
            }
        }

        generateLapisStyleOre(worldIn, rand, pos, 1, 16, 8) { w, r, p ->
            if (BiomeDictionary.areSimilar(w.getBiome(p), Biomes.HELL)) {
                lapisStyleGen.generate(w, r, p)
            }
        }
    }
}