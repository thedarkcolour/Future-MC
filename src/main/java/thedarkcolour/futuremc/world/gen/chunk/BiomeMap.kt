package thedarkcolour.futuremc.world.gen.chunk

import net.minecraft.world.biome.BiomeProvider
import thedarkcolour.futuremc.world.gen.structure.IChunkPos

/**
 * Stores the positions of biomes in chunks.
 */
class BiomeMap(
    pos: IChunkPos,
    biomeProvider: BiomeProvider
)