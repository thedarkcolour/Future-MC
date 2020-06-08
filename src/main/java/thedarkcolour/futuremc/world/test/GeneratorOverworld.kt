package thedarkcolour.futuremc.world.test

import net.minecraft.entity.EnumCreatureType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.gen.IChunkGenerator

class GeneratorOverworld : IChunkGenerator {
    override fun generateStructures(chunkIn: Chunk, x: Int, z: Int): Boolean {
        TODO("not implemented")
    }

    override fun getPossibleCreatures(
        creatureType: EnumCreatureType,
        pos: BlockPos
    ): MutableList<Biome.SpawnListEntry> {
        TODO("not implemented")
    }

    override fun populate(x: Int, z: Int) {
        TODO("not implemented")
    }

    override fun recreateStructures(chunkIn: Chunk, x: Int, z: Int) {
        TODO("not implemented")
    }

    override fun getNearestStructurePos(
        worldIn: World,
        structureName: String,
        position: BlockPos,
        findUnexplored: Boolean
    ): BlockPos? {
        TODO("not implemented")
    }

    override fun generateChunk(x: Int, z: Int): Chunk {
        TODO("not implemented")
    }

    override fun isInsideStructure(worldIn: World, structureName: String, pos: BlockPos): Boolean {
        TODO("not implemented")
    }
}