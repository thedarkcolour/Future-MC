package thedarkcolour.futuremc.world.gen

import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.Biome
import thedarkcolour.futuremc.world.gen.chunk.IChunk
import thedarkcolour.futuremc.world.gen.config.ICarverConfig
import java.util.*

class ConfiguredCarver<C : AbstractCarver<CC>, CC : ICarverConfig>(val carver: C, val config: CC) {

    fun canCarve(rand: Random, chunkX: Int, chunkZ: Int): Boolean {
        return carver.canCarve(rand, chunkX, chunkZ, config)
    }

    /**
     * Carves with the current [config].
     *
     * @param chunk the chunk to carve in
     * @param rand the rng
     * @param seaLevel the sea level of the [chunk]
     * @param x the x position to carve at
     * @param z the z position to carve at
     * @param chunkX the x position of the [chunk]
     * @param chunkZ the z position of the [chunk]
     * @param mask the carving mask
     * @param getBiome the biome getter function
     */
    fun carve(chunk: IChunk, rand: Random, seaLevel: Int, x: Int, z: Int, chunkX: Int, chunkZ: Int, mask: BitSet, getBiome: (BlockPos) -> Biome) {
        return carver.carve(chunk, rand, seaLevel, x, z, chunkX, chunkZ, mask, config, getBiome)
    }
}