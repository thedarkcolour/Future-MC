package thedarkcolour.futuremc.world.gen

import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.Biome
import thedarkcolour.futuremc.world.gen.chunk.IChunk
import thedarkcolour.futuremc.world.gen.config.ICarverConfig
import java.util.*

abstract class AbstractCarver<CC : ICarverConfig> {

    /**
     *
     */
    abstract fun canCarve(rand: Random, chunkX: Int, chunkZ: Int, config: CC): Boolean

    /**
     * Carves in the current [chunk].
     *
     * @param chunk the chunk to carve in
     * @param rand the rng
     * @param seaLevel the sea level of the [chunk]
     * @param x the x position to carve at
     * @param z the z position to carve at
     * @param chunkX the x position of the [chunk]
     * @param chunkZ the z position of the [chunk]
     * @param mask the carving mask
     * @param config the [CC] config for this carver
     * @param getBiome the biome getter function
     */
    abstract fun carve(
        chunk: IChunk,
        rand: Random,
        seaLevel: Int,
        x: Int,
        z: Int,
        chunkX: Int,
        chunkZ: Int,
        mask: BitSet,
        config: CC,
        getBiome: (BlockPos) -> Biome
    )
}