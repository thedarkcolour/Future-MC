package thedarkcolour.futuremc.world

import net.minecraft.world.storage.WorldInfo
import thedarkcolour.futuremc.world.gen.chunk.IChunk

interface IWorld {
    val info: WorldInfo

    fun getChunkNullable(x: Int, z: Int): IChunk?

    /**
     * Returns the chunk at the position.
     * @throws NullPointerException if there is no chunk at the position
     */
    fun getChunk(x: Int, z: Int): IChunk {
        return getChunkNullable(x, z) ?: error("Chunk does not exist at x=$x z=$z")
    }
}