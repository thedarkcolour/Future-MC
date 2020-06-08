package thedarkcolour.futuremc.world

import net.minecraft.world.WorldServer
import net.minecraft.world.storage.WorldInfo
import thedarkcolour.futuremc.world.gen.chunk.BiomeManager
import thedarkcolour.futuremc.world.gen.chunk.IChunk
import thedarkcolour.futuremc.world.gen.structure.IChunkPos

class WorldGenPreview(
    worldIn: WorldServer,
    pos: IChunkPos,
    val biomeManager: BiomeManager,
    override val info: WorldInfo
) : IWorld {
    val seed = worldIn.seed
    val mainX = pos.x
    val mainZ = pos.z

    override fun getChunkNullable(x: Int, z: Int): IChunk? {
        TODO()
    }
}