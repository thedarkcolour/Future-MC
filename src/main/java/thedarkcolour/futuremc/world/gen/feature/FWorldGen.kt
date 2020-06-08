package thedarkcolour.futuremc.world.gen.feature

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.IChunkGenerator
import net.minecraftforge.fml.common.IWorldGenerator
import thedarkcolour.futuremc.world.gen.structure.IChunkPos
import java.util.*

/**
 * Base class for FutureMC 1.12 world generation features.
 *
 * @author TheDarkColour
 */
interface FWorldGen : IWorldGenerator {
    /**
     * Named arguments with proper nullability.
     */
    override fun generate(
        rand: Random,
        chunkX: Int,
        chunkZ: Int,
        worldIn: World,
        chunkGenerator: IChunkGenerator,
        chunkProvider: IChunkProvider
    )

    /**
     * Used for random patches (bamboo, flowers).
     *
     * @param worldIn the world
     * @param rand the rng
     * @param chunkPos the position of the current chunk
     * @param tries the number of attempts to generate the feature in patch
     * @param placeFunction the function to generate the feature in a patch
     */
    @JvmDefault
    fun placeAround(worldIn: World, rand: Random, chunkPos: ChunkPos, tries: IntRange, placeFunction: (World, Random, BlockPos) -> Unit) {
        for (i in tries) {
            val xPos = rand.nextInt(16) + 8
            val zPos = rand.nextInt(16) + 8
            val yPos = rand.nextInt(worldIn.getHeight(chunkPos.getBlock(0, 0, 0).add(xPos, 0, zPos)).y + 32)
            val pos = chunkPos.getBlock(0, 0, 0).add(xPos, yPos, zPos)
            placeFunction(worldIn, rand, pos)
        }
    }

    /**
     * Places this feature like a normal ore (diamond, iron, coal)
     *
     * @param placeFunction the feature placer
     */
    @JvmDefault
    fun generateNormalOre(worldIn: World, rand: Random, pos: IChunkPos, veinCount: Int, minHeight: Int, maxHeight: Int, placeFunction: (World, Random, BlockPos) -> Unit) {
        if (minHeight > 255 || maxHeight > 255) throw IllegalArgumentException("Height range must be in 0..255")

        for (i in 0 until veinCount) {
            val position = BlockPos((pos.x * 16) + rand.nextInt(16), rand.nextInt(maxHeight - minHeight) + minHeight, (pos.z * 16) + rand.nextInt(16))
            placeFunction(worldIn, rand, position)
        }
    }

    /**
     * Places this feature like Lapis Lazuli
     */
    @JvmDefault
    fun generateLapisStyleOre(worldIn: World, rand: Random, pos: IChunkPos, veinCount: Int, base: Int, spread: Int, placeFunction: (World, Random, BlockPos) -> Unit) {
        for (i in 0 until veinCount) {
            val position = BlockPos((pos.x * 16) + rand.nextInt(16), rand.nextInt(spread) + rand.nextInt(spread) + base - spread, (pos.z * 16) + rand.nextInt(16))
            placeFunction(worldIn, rand, position)
        }
    }
}