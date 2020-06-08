package thedarkcolour.futuremc.world.gen.chunk

import net.minecraft.crash.CrashReport
import net.minecraft.util.ReportedException
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.BiomeProvider
import net.minecraft.world.gen.IChunkGenerator
import net.minecraftforge.fml.common.registry.ForgeRegistries
import thedarkcolour.futuremc.world.WorldGenPreview
import thedarkcolour.futuremc.world.biome.FBiome
import thedarkcolour.futuremc.world.gen.CarvingStage
import thedarkcolour.futuremc.world.gen.DecorationStage
import thedarkcolour.futuremc.world.gen.structure.AbstractStructure
import thedarkcolour.futuremc.world.gen.structure.WorldGenRandom

/**
 * Base implementation of [IChunkGenerator].
 */
abstract class ChunkGenerator<T : ChunkGenConfig>(
    val world: World,
    protected val seed: Long,
    val biomeProvider: BiomeProvider,
    protected val config: T
) : IChunkGenerator {

    /**
     * Creates a new [BiomeMap] for the [chunkPreview].
     */
    fun fillBiomeMap(chunkPreview: ChunkGenPreview) {

        chunkPreview.biomeMap = BiomeMap(chunkPreview.pos, biomeProvider)
    }

    /**
     * Used to control the biome of superflat worlds.
     */
    open fun getBiome(manager: BiomeManager, pos: BlockPos): FBiome {

        return manager.getBiome(pos)
    }

    open fun carve(manager: BiomeManager, chunk: IChunk, stage: CarvingStage) {
        val rand = WorldGenRandom()
        val chunkPos = chunk.pos
        val j = chunkPos.x
        val k = chunkPos.x
        val biome = getBiome(manager, chunkPos.toBlockPos())
        val mask = chunk.getCarvingMask()

        for (l in j - 8 .. j + 8) {
            for (i1 in k - 8 .. k + 8) {
                val list = biome.getCarvers(stage)
                val iterator = list.listIterator()

                while (iterator.hasNext()) {
                    val j1 = iterator.nextIndex()
                    val carver = iterator.next()

                    rand.setLargeFeatureSeed(seed + j1, l, i1)

                    if (carver.canCarve(rand, l, i1)) {
                        carver.carve(chunk, rand, world.seaLevel, l, i1, j, k, mask) { pos ->
                            getBiome(manager, pos)
                        }
                    }
                }
            }
        }
    }

    fun findNearestStructure(worldIn: World, name: String, pos: BlockPos, radius: Int, skipExistingChunks: Boolean): BlockPos? {
        val structure = AbstractStructure.REGISTRY[name]
        return structure?.findNearest(worldIn, this, pos, radius, skipExistingChunks)
    }

    fun decorate(preview: WorldGenPreview) {
        val i = preview.mainX
        val j = preview.mainZ
        val k = i * 16
        val l = j * 16
        val pos = BlockPos(k, 0, l)
        val biome = getBiome(preview.biomeManager, pos.add(8, 8, 8))
        val rand = WorldGenRandom()
        val i1 = rand.setDecorationSeed(preview.seed, k, l)

        for (stage in DecorationStage.values()) {
            try {
                biome.decorate(stage, this, preview, i1, rand, pos)
            } catch (e: Exception) {
                val report = CrashReport.makeCrashReport(e, "Biome decoration")
                val cat = report.makeCategory("Generation")
                cat.addDetail("CenterX", i::toString)
                cat.addDetail("CenterZ", j::toString)
                cat.addDetail("Stage", stage::toString)
                cat.addDetail("Seed", i1::toString)
                cat.addDetail("Biome", ForgeRegistries.BIOMES.getKey(biome)::toString)
                throw ReportedException(report)
            }
        }
    }
}