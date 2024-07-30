package thedarkcolour.futuremc.world.gen.feature

import net.minecraft.block.BlockLeaves
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
    private val VALID_BIOMES = hashSetOf(
        Biomes.JUNGLE.registryName,
        Biomes.JUNGLE_EDGE.registryName,
        Biomes.JUNGLE_HILLS.registryName,
        Biomes.MUTATED_JUNGLE.registryName,
        Biomes.MUTATED_JUNGLE_EDGE.registryName,
    )

    private fun generate(worldIn: World, random: Random, pos: BlockPos) {
        val bamboo = FBlocks.BAMBOO
        val block = worldIn.getBlockState(pos).block
        if (block.isReplaceable(worldIn, pos) && bamboo.canPlaceBlockAt(worldIn, pos) && isBiomeValid(worldIn.getBiome(pos))) {
            worldIn.setBlockState(pos, bamboo.defaultState)
        } else {
            return
        }
        for (j in 0..9) {
            if (worldIn.getBlockState(pos).block == bamboo && bamboo.canGrow(worldIn, pos, worldIn.getBlockState(pos), false)) {
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
        val x = (chunkX shl 4) + 8
        val z = (chunkZ shl 4) + 8
        val position = BlockPos(x, 0, z)
        val biome = worldIn.getBiomeForCoordsBody(position)
        val chunkPos = worldIn.getChunk(chunkX, chunkZ).pos
        if (isBiomeValid(biome) && worldIn.worldType != WorldType.FLAT) {
            FWorldGen.placeAround(worldIn, rand, chunkPos, 0..22) { world2, random, pos ->
                generate(world2, random, pos)
            }
        }
    }

    private fun isBiomeValid(biome: Biome): Boolean {
        return VALID_BIOMES.contains(biome.registryName)
    }
}