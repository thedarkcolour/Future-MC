package thedarkcolour.futuremc.world.gen.feature

import net.minecraft.init.Biomes
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldType
import net.minecraft.world.biome.Biome
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.IChunkGenerator
import net.minecraftforge.fml.common.IWorldGenerator
import thedarkcolour.core.util.matchesAny
import thedarkcolour.futuremc.block.BlockBamboo
import thedarkcolour.futuremc.registry.FBlocks
import java.util.*

object BambooFeature : IWorldGenerator {
    private fun generate(worldIn: World, random: Random, pos: BlockPos) {
        val bamboo = FBlocks.BAMBOO as BlockBamboo
        if (worldIn.getBlockState(pos).block.isReplaceable(worldIn, pos) && bamboo.canPlaceBlockAt(worldIn, pos)) {
            worldIn.setBlockState(pos, bamboo.defaultState)
        }
        for (j in 0..9) {
            if (worldIn.getBlockState(pos).block == bamboo && bamboo.canGrow(
                    worldIn,
                    pos,
                    worldIn.getBlockState(pos),
                    false
                )
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
            for (i in 0..12) {
                val xPos = rand.nextInt(16) + 8
                val zPos = rand.nextInt(16) + 8
                val yPos = rand.nextInt(worldIn.getHeight(chunkPos.getBlock(0, 0, 0).add(xPos, 0, zPos)).y + 32)
                val pos = chunkPos.getBlock(0, 0, 0).add(xPos, yPos, zPos)
                generate(worldIn, rand, pos)
            }
        }
    }

    private fun isBiomeValid(biome: Biome): Boolean {
        return biome.matchesAny(
            Biomes.JUNGLE,
            Biomes.JUNGLE_EDGE,
            Biomes.JUNGLE_HILLS,
            Biomes.MUTATED_JUNGLE,
            Biomes.MUTATED_JUNGLE_EDGE
        )
    }
}