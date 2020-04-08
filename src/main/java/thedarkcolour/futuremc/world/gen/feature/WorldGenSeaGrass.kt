package thedarkcolour.futuremc.world.gen.feature

import net.minecraft.init.Biomes
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.IChunkGenerator
import net.minecraftforge.fml.common.IWorldGenerator
import thedarkcolour.core.util.matchesAny
import java.util.*

object WorldGenSeaGrass : IWorldGenerator {
    override fun generate(
        random: Random,
        chunkX: Int,
        chunkZ: Int,
        worldIn: World,
        chunkGenerator: IChunkGenerator,
        chunkProvider: IChunkProvider
    ) {
        val x = chunkX * 16 + 8
        val z = chunkZ * 16 + 8
        val biome = worldIn.getBiomeForCoordsBody(BlockPos(x, 0, z))
        val chunkPos = worldIn.getChunk(chunkX, chunkZ).pos
        when (biome) {
            Biomes.DEEP_OCEAN -> {

            }
            else -> return
        }
    }

    private fun isBiomeValid(biome: Biome): Boolean {
        return biome.matchesAny(
            Biomes.FROZEN_OCEAN,
            Biomes.DEEP_OCEAN,
            Biomes.OCEAN,
            Biomes.SWAMPLAND,
            Biomes.MUTATED_SWAMPLAND,
            Biomes.RIVER
        )
    }
}