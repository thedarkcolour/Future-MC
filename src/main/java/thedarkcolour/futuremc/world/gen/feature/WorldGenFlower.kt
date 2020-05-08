package thedarkcolour.futuremc.world.gen.feature

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldType
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.IChunkGenerator
import net.minecraftforge.fml.common.IWorldGenerator
import thedarkcolour.futuremc.block.BlockFlower
import java.util.*

class WorldGenFlower(flower: Block) : IWorldGenerator {
    private val state: IBlockState = flower.defaultState
    private val flower = flower as BlockFlower

    private fun generate(world: World, random: Random, targetPos: BlockPos) {
        for (i in 0..63) {
            val pos = targetPos.add(
                random.nextInt(8) - random.nextInt(8),
                random.nextInt(4) - random.nextInt(4),
                random.nextInt(8) - random.nextInt(8)
            )
            // prevent cascading worldgen
            if (!world.isBlockLoaded(pos)) continue
            if (world.isAirBlock(pos) && (!world.provider.isNether || pos.y < 255) && flower.canBlockStay(
                    world, pos, state
                )
            ) {
                world.setBlockState(pos, state, 2)
            }
        }
    }

    override fun generate(
        rand: Random, chunkX: Int, chunkZ: Int, worldIn: World,
        chunkGenerator: IChunkGenerator, chunkProvider: IChunkProvider
    ) {
        val x = chunkX * 16 + 8
        val z = chunkZ * 16 + 8
        val position = BlockPos(x, 0, z)
        if (!worldIn.isBlockLoaded(position))
            return
        val biome = worldIn.getBiomeForCoordsBody(position)
        val chunkPos = worldIn.getChunk(chunkX, chunkZ).pos
        if (rand.nextDouble() < flower.flowerChance) {
            if (flower.isBiomeValid(biome) && worldIn.worldType != WorldType.FLAT) {
                for (i in 0..4) {
                    val xPos = rand.nextInt(16) + 8
                    val zPos = rand.nextInt(16) + 8
                    val yPos = rand.nextInt(worldIn.getHeight(chunkPos.getBlock(0, 0, 0).add(xPos, 0, zPos)).y + 32)
                    val pos = chunkPos.getBlock(0, 0, 0).add(xPos, yPos, zPos)
                    generate(worldIn, rand, pos)
                }
            }
        }
    }
}