package thedarkcolour.futuremc.world.gen.structure

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.ChunkGeneratorFlat
import net.minecraft.world.gen.ChunkGeneratorOverworld
import net.minecraft.world.gen.ChunkProviderServer
import net.minecraft.world.gen.structure.MapGenStronghold
import net.minecraft.world.gen.structure.StructureStart
import net.minecraftforge.common.BiomeManager
import thedarkcolour.futuremc.world.gen.config.IFeatureConfig
import java.util.*

class StrongholdStructure : AbstractStructure<IFeatureConfig.Default>() {
    private var ranBiomeCheck = false
    private var structureCoords: Array<ChunkPos>? = null
    private val structureStarts = arrayListOf<StructureStart>()
    private var seed = 0L

    override fun shouldStartAt(manager: BiomeManager, worldIn: World, rand: Random, chunkX: Int, chunkZ: Int, biome: Biome): Boolean {
        if (seed != worldIn.seed) {
            resetData()
        }

        if (!ranBiomeCheck) {
            reinitializeData(worldIn)
            ranBiomeCheck = true
        }

        for (chunkPos in structureCoords!!) {
            if (chunkX == chunkPos.x && chunkZ == chunkPos.z) {
                return true
            }
        }

        return false
    }

    private fun resetData() {
        ranBiomeCheck = false
        structureCoords = null
        structureStarts.clear()
    }

    override fun getStartFactory(): IStartFactory {
        TODO()
    }

    override val structureName = "Stronghold"

    override fun findNearest(worldIn: World, pos: BlockPos, radius: Int, skipExisting: Boolean): BlockPos? {
        if (doesWorldHaveStronghold(worldIn)) {
            return null
        } else {
            if (seed != worldIn.seed) {
                resetData()
            }

            if (!ranBiomeCheck) {
                reinitializeData(worldIn)
                ranBiomeCheck = true
            }

            var pos1: BlockPos? = null
            val mutable = BlockPos.MutableBlockPos()
            var d0 = Double.MAX_VALUE

            for (chunkPos in structureCoords!!) {
                mutable.setPos((chunkPos.x shl 4) + 8, 32, (chunkPos.z shl 4) + 8)
                val d1 = mutable.distanceSq(pos)

                if (pos1 == null) {
                    pos1 = mutable.toImmutable()
                    d0 = d1
                } else if (d1 < d0) {
                    pos1 = mutable.toImmutable()
                    d0 = d1
                }
            }

            return pos1
        }
    }

    private fun reinitializeData(worldIn: World) {
        seed = worldIn.seed
        val allowedBiomes = hashSetOf<Biome>()

        val i2 = getStrongholdDistance(worldIn)
        val j2 = getStrongholdDistance(worldIn)

        // todo have a 1.13 biome registry to avoid 1.12 biomes
        // for ()
    }

    fun doesWorldHaveStronghold(worldIn: World): Boolean = TODO()

    private fun getStrongholdDistance(worldIn: World): Double {
        val provider = worldIn.chunkProvider

        return if (provider is ChunkProviderServer) {
            val generator = provider.chunkGenerator

            when (generator) {
                is ChunkGeneratorOverworld -> generator.strongholdGenerator.distance
                is ChunkGeneratorFlat -> generator.structureGenerators.values.firstOrNull { it is MapGenStronghold }
            }

            32.0
        } else -1.0
    }

    private fun getStrongholdCount(worldIn: World): Int {
        val provider = worldIn.chunkProvider

        return if (provider is ChunkProviderServer) {
            val generator = provider.chunkGenerator

            when (generator) {
                is ChunkGeneratorOverworld -> generator.strongholdGenerator.structureCoords.size
            }

            128
        } else 0
    }
}
