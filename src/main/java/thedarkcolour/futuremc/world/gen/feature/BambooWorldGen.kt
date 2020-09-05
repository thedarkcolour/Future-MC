package thedarkcolour.futuremc.world.gen.feature

import net.minecraft.init.Biomes
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldType
import net.minecraft.world.biome.Biome
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.IChunkGenerator
import thedarkcolour.futuremc.registry.FBlocks
import java.util.*

object BambooWorldGen : FWorldGen {
    private val VALID_BIOMES = setOf(
        Biomes.JUNGLE,
        Biomes.JUNGLE_EDGE,
        Biomes.JUNGLE_HILLS,
        Biomes.MUTATED_JUNGLE,
        Biomes.MUTATED_JUNGLE_EDGE
    )

    private fun generate(worldIn: World, random: Random, pos: BlockPos) {
        val bamboo = FBlocks.BAMBOO
        if (
            worldIn.getBlockState(pos).block.isReplaceable(worldIn, pos) &&
            bamboo.canPlaceBlockAt(worldIn, pos) &&
            isBiomeValid(worldIn.getBiome(pos))
        ) {
            worldIn.setBlockState(pos, bamboo.defaultState)
        } else {
            return
        }
        for (j in 0..9) {
            if (worldIn.getBlockState(pos).block == bamboo
                && bamboo.canGrow(worldIn, pos, worldIn.getBlockState(pos), false)
            ) {
                bamboo.grow(worldIn, random, pos, worldIn.getBlockState(pos))
            }
        }
    }

    override fun generate(
        rand: Random,
        chunkX: Int,
        chunkZ: Int,
        worldIn: World,
        chunkGenerator: IChunkGenerator,
        chunkProvider: IChunkProvider
    ) {
        val x = chunkX + rand.nextInt(16) + 8
        val z = chunkZ + rand.nextInt(16) + 8
        val position = BlockPos(x, 0, z)
        if (!worldIn.isBlockLoaded(position))
            return
        val biome = worldIn.getBiomeForCoordsBody(position)
        val chunkPos = worldIn.getChunk(chunkX, chunkZ).pos
        if (isBiomeValid(biome) && worldIn.worldType != WorldType.FLAT) {
            placeAround(worldIn, rand, chunkPos, 0..12, ::generate)
        }
    }

    private fun isBiomeValid(biome: Biome): Boolean {
        return VALID_BIOMES.contains(biome)
    }
}